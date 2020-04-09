package com.webank.wecube.plugins.alicloud.service.vpc.nat;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.nat.CoreCreateNatGatewayRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.nat.CoreCreateNatGatewayResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.nat.CoreDeleteNatGatewayRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.nat.CoreDeleteNatGatewayResponseDto;
import com.webank.wecube.plugins.alicloud.service.vpc.eip.EipService;
import com.webank.wecube.plugins.alicloud.service.vpc.eip.EipServiceImpl;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimer;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimerTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
@Service
public class NatGatewayServiceImpl implements NatGatewayService {

    private static final Logger logger = LoggerFactory.getLogger(NatGatewayService.class);

    private AcsClientStub acsClientStub;
    private DtoValidator dtoValidator;
    private EipService eipService;

    @Autowired
    public NatGatewayServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator, EipService eipService) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
        this.eipService = eipService;
    }

    @Override
    public List<CoreCreateNatGatewayResponseDto> createNatGateway(List<CoreCreateNatGatewayRequestDto> requestDtoList) {
        List<CoreCreateNatGatewayResponseDto> resultList = new ArrayList<>();
        for (CoreCreateNatGatewayRequestDto requestDto : requestDtoList) {
            CoreCreateNatGatewayResponseDto result = new CoreCreateNatGatewayResponseDto();
            try {
                this.dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                if (StringUtils.isNotEmpty(requestDto.getNatGatewayId())) {
                    DescribeNatGatewaysRequest retrieveNatGatewayRequest = new DescribeNatGatewaysRequest();
                    retrieveNatGatewayRequest.setNatGatewayId(requestDto.getNatGatewayId());
                    retrieveNatGatewayRequest.setRegionId(regionId);
                    final DescribeNatGatewaysResponse describeNatGatewaysResponse = this.retrieveNatGateway(client, retrieveNatGatewayRequest);

                    if (!describeNatGatewaysResponse.getNatGateways().isEmpty()) {
                        result = result.fromSdkCrossLineage(describeNatGatewaysResponse.getNatGateways().get(0));
                        result.setRequestId(describeNatGatewaysResponse.getRequestId());
                        continue;
                    }
                }

                logger.info("Creating NAT gateway...");

                CreateNatGatewayRequest createNatGatewayRequest = requestDto.toSdk();
                createNatGatewayRequest.setRegionId(regionId);
                CreateNatGatewayResponse response = this.acsClientStub.request(client, createNatGatewayRequest);
                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }

        }
        return resultList;
    }

    @Override
    public List<CoreDeleteNatGatewayResponseDto> deleteNatGateway(List<CoreDeleteNatGatewayRequestDto> requestDtoList) {
        List<CoreDeleteNatGatewayResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteNatGatewayRequestDto requestDto : requestDtoList) {
            CoreDeleteNatGatewayResponseDto result = new CoreDeleteNatGatewayResponseDto();
            try {
                this.dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();
                final DescribeNatGatewaysResponse describeNatGatewaysResponse = this.retrieveNatGateway(client, requestDto.getNatGatewayId(), regionId);
                if (describeNatGatewaysResponse.getNatGateways().isEmpty()) {
                    String msg = String.format("Cannot find Nat gateway info by ID: [%s]", requestDto.getNatGatewayId());
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                // need to un-associate then release the eip first
                final DescribeNatGatewaysResponse.NatGateway foundNatGateway = describeNatGatewaysResponse.getNatGateways().get(0);
                final List<DescribeNatGatewaysResponse.NatGateway.IpList> eipList = foundNatGateway.getIpLists();
                final String natGatewayId = foundNatGateway.getNatGatewayId();
                if (!eipList.isEmpty()) {
                    final List<String> allocationIdList = eipList.stream().map(DescribeNatGatewaysResponse.NatGateway.IpList::getAllocationId).collect(Collectors.toList());

                    logger.info("Un-associating NAT gateway with bound EIp...");
                    this.eipService.unAssociateEipAddress(client, regionId, allocationIdList, natGatewayId, "Nat");

                }

                // setup a timer to poll whether the EIP has already un-associated with the NAT gateway
                Function<?, Boolean> checkIfAllEipUnAssociated = o -> this.eipService.ifEipIsAvailable(client, regionId, EipServiceImpl.AssociatedInstanceType.Nat.toString(), natGatewayId);
                PluginTimer.runTask(new PluginTimerTask(checkIfAllEipUnAssociated));

                logger.info("Deleting NAT gateway...");

                DeleteNatGatewayRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                DeleteNatGatewayResponse response = this.acsClientStub.request(client, request);
                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }

        }
        return resultList;
    }

    private DescribeNatGatewaysResponse retrieveNatGateway(IAcsClient client, String natGatewayId, String regionId) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(natGatewayId, regionId)) {
            throw new PluginException("Either natGatewayId or regionId cannot be null or empty");
        }

        DescribeNatGatewaysRequest request = new DescribeNatGatewaysRequest();
        request.setNatGatewayId(natGatewayId);
        request.setRegionId(regionId);

        return this.retrieveNatGateway(client, request);
    }

    private DescribeNatGatewaysResponse retrieveNatGateway(IAcsClient client, DescribeNatGatewaysRequest request) throws AliCloudException, PluginException {

        if (StringUtils.isAnyEmpty(request.getRegionId(), request.getNatGatewayId())) {
            throw new PluginException("Either regionId or natGatewayId cannot be null or empty.");
        }

        logger.info("Retrieving NAT gateway info...");
        DescribeNatGatewaysResponse response = this.acsClientStub.request(client, request);
        return response;
    }
}
