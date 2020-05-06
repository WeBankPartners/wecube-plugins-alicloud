package com.webank.wecube.plugins.alicloud.service.vpc.nat;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.nat.*;
import com.webank.wecube.plugins.alicloud.service.vpc.eip.AssociatedInstanceType;
import com.webank.wecube.plugins.alicloud.service.vpc.eip.EipService;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimer;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimerTask;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
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

    private final AcsClientStub acsClientStub;
    private final DtoValidator dtoValidator;
    private final EipService eipService;

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

                dtoValidator.validate(requestDto);

                logger.info("Creating NAT gateway: {}", requestDto.toString());

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
                logger.info("Result: {}", result.toString());
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

                dtoValidator.validate(requestDto);

                logger.info("Deleting NAT gateway: {}", requestDto.toString());

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
                Function<?, Boolean> checkIfAllEipUnAssociated = o -> this.eipService.ifEipIsAvailable(client, regionId, AssociatedInstanceType.Nat.toString(), natGatewayId);
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
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }

        }
        return resultList;
    }

    @Override
    public List<CoreCreateSnatEntryResponseDto> createSnatEntry(List<CoreCreateSnatEntryRequestDto> requestDtoList) {
        List<CoreCreateSnatEntryResponseDto> resultList = new ArrayList<>();
        for (CoreCreateSnatEntryRequestDto requestDto : requestDtoList) {
            CoreCreateSnatEntryResponseDto result = new CoreCreateSnatEntryResponseDto();
            try {

                dtoValidator.validate(requestDto);

                logger.info("Creating SNAT entry: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();
                bindEipToNat(client, regionId, requestDto.getNatId(), requestDto.getSnatIp());
                final String listStr = PluginStringUtils.handleCoreListStr(requestDto.getSnatIp(), true);
                requestDto.setSnatIp(listStr);

                CreateSnatEntryRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                CreateSnatEntryResponse response = this.acsClientStub.request(client, request);
                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
                resultList.add(result);
            }

        }
        return resultList;
    }

    @Override
    public List<CoreDeleteSnatEntryResponseDto> deleteSnatEntry(List<CoreDeleteSnatEntryRequestDto> requestDtoList) {
        List<CoreDeleteSnatEntryResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteSnatEntryRequestDto requestDto : requestDtoList) {
            CoreDeleteSnatEntryResponseDto result = new CoreDeleteSnatEntryResponseDto();
            try {

                dtoValidator.validate(requestDto);

                logger.info("Deleting SNAT entry: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();

                DeleteSnatEntryRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                DeleteSnatEntryResponse response = this.acsClientStub.request(client, request);
                result = result.fromSdk(response);

                // unbind snat ip from nat
                unbindSnatIpFromNat(client, regionId, requestDto.getSnatTableId(), requestDto.getSnatEntryId(), requestDto.getNatId());

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
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
        return this.acsClientStub.request(client, request);
    }

    /**
     * Check if EIP bind NAT, if not, bind them
     *
     * @param client   alicloud client
     * @param regionId regionId
     * @param natId    natId
     * @param snatIp   could be list snatIp
     */
    private void bindEipToNat(IAcsClient client, String regionId, String natId, String snatIp) {
        final String[] ipArray = PluginStringUtils.splitStringList(snatIp).toArray(new String[0]);
        eipService.bindIpToInstance(client, regionId, natId, AssociatedInstanceType.Nat, ipArray);
    }

    private void unbindSnatIpFromNat(IAcsClient client, String regionId, String snatTableId, String snatEntryId, String natId) throws PluginException, AliCloudException {
        DescribeSnatTableEntriesRequest request = new DescribeSnatTableEntriesRequest();
        request.setRegionId(regionId);
        request.setSnatTableId(snatTableId);
        request.setSnatEntryId(snatEntryId);

        final DescribeSnatTableEntriesResponse response = acsClientStub.request(client, request);
        if (response.getSnatTableEntries().isEmpty()) {
            throw new PluginException("Cannot find SNAT table entry by given info");
        }
        final DescribeSnatTableEntriesResponse.SnatTableEntry foundSnatEntry = response.getSnatTableEntries().get(0);
        final String[] ips = foundSnatEntry.getSnatIp().split(",");
        eipService.unbindIpFromInstance(client, regionId, natId, AssociatedInstanceType.Nat, ips);
    }
}
