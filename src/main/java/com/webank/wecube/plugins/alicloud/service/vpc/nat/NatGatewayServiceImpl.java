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
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
import com.webank.wecube.plugins.alicloud.support.PluginSdkBridge;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author howechen
 */
@Service
public class NatGatewayServiceImpl implements NatGatewayService {

    private static final Logger logger = LoggerFactory.getLogger(NatGatewayService.class);

    private AcsClientStub acsClientStub;
    private DtoValidator dtoValidator;

    @Autowired
    public NatGatewayServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
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
                requestDto.setRegionId(regionId);

                if (StringUtils.isNotEmpty(requestDto.getNatGatewayId())) {
                    DescribeNatGatewaysRequest retrieveNatGatewayRequest = new DescribeNatGatewaysRequest();
                    retrieveNatGatewayRequest.setNatGatewayId(requestDto.getNatGatewayId());
                    retrieveNatGatewayRequest.setRegionId(regionId);
                    final DescribeNatGatewaysResponse.NatGateway foundNatGateway = this.retrieveNatGateway(client, retrieveNatGatewayRequest);
                    if (null != foundNatGateway) {
                        result = PluginSdkBridge.fromSdk(foundNatGateway, CoreCreateNatGatewayResponseDto.class);
                        continue;
                    }
                }

                logger.info("Creating NAT gateway...");

                CreateNatGatewayRequest createNatGatewayRequest = PluginSdkBridge.toSdk(requestDto, CreateNatGatewayRequest.class);
                CreateNatGatewayResponse response = this.acsClientStub.request(client, createNatGatewayRequest);
                result = PluginSdkBridge.fromSdk(response, CoreCreateNatGatewayResponseDto.class);

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
                requestDto.setRegionId(regionId);

                DescribeNatGatewaysRequest retrieveNatGatewayRequest = new DescribeNatGatewaysRequest();
                retrieveNatGatewayRequest.setNatGatewayId(requestDto.getNatGatewayId());
                retrieveNatGatewayRequest.setRegionId(regionId);
                final DescribeNatGatewaysResponse.NatGateway foundNatGateway = this.retrieveNatGateway(client, retrieveNatGatewayRequest);
                if (null == foundNatGateway) {
                    String msg = String.format("Cannot find Nat gateway info by ID: [%s]", requestDto.getNatGatewayId());
                    logger.error(msg);
                    throw new PluginException(msg);
                }


                logger.info("Deleting NAT gateway...");

                DeleteNatGatewayRequest request = PluginSdkBridge.toSdk(requestDto, DeleteNatGatewayRequest.class);
                DeleteNatGatewayResponse response = this.acsClientStub.request(client, request);
                result = PluginSdkBridge.fromSdk(response, CoreDeleteNatGatewayResponseDto.class);

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

    private DescribeNatGatewaysResponse.NatGateway retrieveNatGateway(IAcsClient client, DescribeNatGatewaysRequest request) throws AliCloudException, PluginException {
        logger.info("Retrieving NAT gateway info...");
        DescribeNatGatewaysResponse response = this.acsClientStub.request(client, request);
        final List<DescribeNatGatewaysResponse.NatGateway> foundNatGatewayList = response.getNatGateways();
        if (foundNatGatewayList.isEmpty()) {
            return null;
        } else {
            return foundNatGatewayList.get(0);
        }
    }
}
