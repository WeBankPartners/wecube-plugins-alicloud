package com.webank.wecube.plugins.alicloud.service.vpc;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreDeleteVpcRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreDeleteVpcResponseDto;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
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
public class VpcServiceImpl implements VpcService {

    private static final Logger logger = LoggerFactory.getLogger(VpcService.class);

    private final AcsClientStub acsClientStub;
    private final DtoValidator dtoValidator;

    @Autowired
    public VpcServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
    }

    @Override
    public List<CoreCreateVpcResponseDto> createVpc(List<CoreCreateVpcRequestDto> coreCreateVpcRequestDtoList) {
        List<CoreCreateVpcResponseDto> resultList = new ArrayList<>();
        for (CoreCreateVpcRequestDto requestDto : coreCreateVpcRequestDtoList) {
            CoreCreateVpcResponseDto result = new CoreCreateVpcResponseDto();
            try {

                dtoValidator.validate(requestDto);

                logger.info("Sending create VPC request: {} to AliCloud.", requestDto.toString());
                // check region id
                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String vpcId = requestDto.getVpcId();

                if (StringUtils.isEmpty(regionId)) {
                    throw new PluginException("The region id cannot be NULL or empty.");
                }

                if (StringUtils.isNotEmpty(vpcId)) {
                    // if vpc id is not empty, retrieve vpc info
                    final DescribeVpcsResponse retrieveVpcResponse = this.retrieveVpc(client, regionId, vpcId);
                    if (retrieveVpcResponse.getVpcs().size() == 1) {
                        final DescribeVpcsResponse.Vpc foundVpc = retrieveVpcResponse.getVpcs().get(0);
                        result = result.fromSdkCrossLineage(foundVpc);
                        result.setRequestId(retrieveVpcResponse.getRequestId());
                        continue;
                    }
                }

                // if vpc id is empty or cannot find VPC by given vpcId, create vpc
                final CreateVpcRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                CreateVpcResponse response;
                response = this.acsClientStub.request(client, request);
                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Create VPC result: {}", result.toString());
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public DescribeVpcsResponse retrieveVpc(IAcsClient client, String regionId, String vpcId) throws AliCloudException, PluginException {

        if (StringUtils.isAnyEmpty(regionId, vpcId)) {
            throw new PluginException("Either regionid or vpcId cannot be null or empty.");
        }

        logger.info("Retrieving VPC info, the vpc ID: [{}].", vpcId);

        // create new request
        DescribeVpcsRequest describeVpcsRequest = new DescribeVpcsRequest();
        describeVpcsRequest.setRegionId(regionId);
        describeVpcsRequest.setVpcId(vpcId);
        // send the request and handle the error, then return the response
        return this.acsClientStub.request(client, describeVpcsRequest);
    }

    @Override
    public List<CoreDeleteVpcResponseDto> deleteVpc(List<CoreDeleteVpcRequestDto> coreDeleteVpcRequestDtoList) {
        List<CoreDeleteVpcResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteVpcRequestDto requestDto : coreDeleteVpcRequestDtoList) {
            CoreDeleteVpcResponseDto result = new CoreDeleteVpcResponseDto();
            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                if (StringUtils.isEmpty(requestDto.getVpcId())) {
                    throw new PluginException("The VPC id cannot be empty or null.");
                }

                final DescribeVpcsResponse foundedVpcInfo = this.retrieveVpc(client, regionId, requestDto.getVpcId());

                // check if VPC already deleted
                if (0 == foundedVpcInfo.getTotalCount()) {
                    continue;
                }

                // delete VPC
                logger.info("Deleting VPC, VPC ID: [{}], VPC region:[{}]", requestDto.getVpcId(), regionId);
                final DeleteVpcRequest deleteVpcRequest = requestDto.toSdk();
                this.acsClientStub.request(client, deleteVpcRequest);

                // re-check if VPC has already been deleted
                if (0 != this.retrieveVpc(client, regionId, requestDto.getVpcId()).getTotalCount()) {
                    String msg = String.format("The VPC: [%s] from region: [%s] hasn't been deleted", requestDto.getVpcId(), regionId);
                    logger.error(msg);
                    throw new PluginException(msg);
                }
            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Delete VPC result: {}", result.toString());
                resultList.add(result);
            }
        }
        return resultList;
    }
}
