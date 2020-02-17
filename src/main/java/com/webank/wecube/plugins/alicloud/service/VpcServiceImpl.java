package com.webank.wecube.plugins.alicloud.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vpc.model.v20160428.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.common.AliCloudProperties;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcResponseDto;
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

    private static Logger logger = LoggerFactory.getLogger(VpcService.class);

    @Autowired
    private AliCloudProperties aliCloudProperties;

    @Override
    public List<CoreCreateVpcResponseDto> createVpc(List<CoreCreateVpcRequestDto> vpcCreateVpcRequestDto) {
        List<CoreCreateVpcResponseDto> resultList = new ArrayList<>();
        for (CoreCreateVpcRequestDto coreCreateVpcRequestDto : vpcCreateVpcRequestDto) {
            logger.info("Sending create VPC request: {} to AliCloud.", coreCreateVpcRequestDto.toString());
            // check region id
            final String regionId = coreCreateVpcRequestDto.getRegionId();
            if (StringUtils.isEmpty(regionId)) {
                throw new PluginException("The region id cannot be NULL or empty.");
            }

            // if vpc id is not empty, retrieve vpc info
            final String vpcId = coreCreateVpcRequestDto.getVpcId();
            if (!StringUtils.isEmpty(vpcId)) {
                final DescribeVpcsResponse.Vpc foundVpc = this.retrieveVpc(regionId, vpcId).getVpcs().get(0);
                resultList.add(CoreCreateVpcResponseDto.fromSdk(foundVpc));
                continue;
            }

            // if vpc id is empty, create vpc
            DefaultProfile defaultProfile = DefaultProfile.getProfile(
                    coreCreateVpcRequestDto.getRegionId(),
                    aliCloudProperties.getAccessId(),
                    aliCloudProperties.getAccessSecret()
            );
            final IAcsClient client = new DefaultAcsClient(defaultProfile);
            CreateVpcResponse response;
            try {
                final CreateVpcRequest createVpcRequest = this.fromCore(coreCreateVpcRequestDto);
                response = client.getAcsResponse(createVpcRequest);
            } catch (ServerException serverEx) {
                logger.error("AliCloud server error: [{}]", serverEx.getMessage());
                throw new PluginException("AliCloud server error: " + serverEx.getMessage());
            } catch (ClientException clientEx) {
                logger.error("Plugin local client error: [{}]", clientEx.getMessage());
                throw new PluginException("AliCloud local client error: " + clientEx.getMessage());
            }
            resultList.add(CoreCreateVpcResponseDto.fromSdk(response));
        }
        return resultList;
    }

    @Override
    public DescribeVpcsResponse retrieveVpc(String regionId, String vpcId) throws PluginException {
        logger.info("Retrieving VPC info, the region ID: [{}], the vpc ID: [{}].", regionId, vpcId);
        if (StringUtils.isEmpty(regionId)) {
            final String msg = String.format("The region ID cannot be empty, your region id is: [%s]", regionId);
            logger.error(msg);
            throw new PluginException(msg);
        }

        DefaultProfile defaultProfile = DefaultProfile.getProfile(
                regionId,
                aliCloudProperties.getAccessId(),
                aliCloudProperties.getAccessSecret()
        );
        final IAcsClient client = new DefaultAcsClient(defaultProfile);
        // create new request
        DescribeVpcsRequest describeVpcsRequest = new DescribeVpcsRequest();
        describeVpcsRequest.setSysRegionId(regionId);
        describeVpcsRequest.setVpcId(vpcId);
        // initiate the response
        DescribeVpcsResponse response;
        // send the request and handle the error, then return the response
        try {
            response = client.getAcsResponse(describeVpcsRequest);
        } catch (ServerException serverEx) {
            logger.error("AliCloud server error! Code: [{}], Msg: [{}]", serverEx.getErrCode(), serverEx.getMessage());
            throw new PluginException("AliCloud server error: " + serverEx.getMessage());
        } catch (ClientException clientEx) {
            logger.error("Plugin local client error! Code: [{}]. Msg: [{}]", clientEx.getErrCode(), clientEx.getMessage());
            throw new PluginException("AliCloud local client error: " + clientEx.getMessage());
        }
        return response;
    }

    @Override
    public void deleteVpc(List<DeleteVpcRequest> deleteVpcRequestList) throws PluginException {
        for (DeleteVpcRequest deleteVpcRequest : deleteVpcRequestList) {
            logger.info("Deleting VPC, VPC ID: [{}], VPC region:[{}]", deleteVpcRequest.getVpcId(), deleteVpcRequest.getRegionId());
            if (StringUtils.isEmpty(deleteVpcRequest.getVpcId())) {
                throw new PluginException("The VPC id cannot be empty or null.");
            }

            // check if VPC already deleted
            if (0 == this.retrieveVpc(deleteVpcRequest.getRegionId(), deleteVpcRequest.getVpcId()).getTotalCount()) {
                continue;
            }

            // delete VPC
            DefaultProfile defaultProfile = DefaultProfile.getProfile(
                    deleteVpcRequest.getRegionId(),
                    aliCloudProperties.getAccessId(),
                    aliCloudProperties.getAccessSecret()
            );
            final IAcsClient client = new DefaultAcsClient(defaultProfile);
            try {
                client.getAcsResponse(deleteVpcRequest);
            } catch (ServerException serverEx) {
                logger.error("AliCloud server error! Code: [{}]. Msg: [{}]", serverEx.getErrCode(), serverEx.getMessage());
                throw new PluginException("AliCloud server error: " + serverEx.getMessage());
            } catch (ClientException clientEx) {
                logger.error("Plugin local client error! Code: [{}]. Msg: [{}]", clientEx.getErrCode(), clientEx.getMessage());
                throw new PluginException("AliCloud local client error: " + clientEx.getMessage());
            }

            // check if VPC has already been deleted
            if (0 != this.retrieveVpc(deleteVpcRequest.getRegionId(), deleteVpcRequest.getVpcId()).getTotalCount()) {
                String msg = String.format("The VPC: [%s] from region: [%s] hasn't been deleted", deleteVpcRequest.getVpcId(), deleteVpcRequest.getRegionId());
                logger.error(msg);
                throw new PluginException(msg);
            }
        }
    }

    private CreateVpcRequest fromCore(CoreCreateVpcRequestDto coreCreateVpcRequestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(coreCreateVpcRequestDto, CreateVpcRequest.class);
    }


}
