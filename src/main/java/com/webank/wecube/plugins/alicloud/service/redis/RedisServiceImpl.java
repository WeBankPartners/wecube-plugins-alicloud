package com.webank.wecube.plugins.alicloud.service.redis;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.r_kvstore.model.v20150101.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreCreateInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreCreateInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreDeleteInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreDeleteInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
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
public class RedisServiceImpl implements RedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisService.class);

    private AcsClientStub acsClientStub;

    @Autowired
    public RedisServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    @Override
    public List<CoreCreateInstanceResponseDto> createInstance(List<CoreCreateInstanceRequestDto> coreCreateInstanceRequestDtoList) throws PluginException {
        List<CoreCreateInstanceResponseDto> resultList = new ArrayList<>();
        for (CoreCreateInstanceRequestDto requestDto : coreCreateInstanceRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String regionId = cloudParamDto.getRegionId();
            requestDto.setRegionId(regionId);
            final String instanceId = requestDto.getInstanceId();

            CoreCreateInstanceResponseDto result = new CoreCreateInstanceResponseDto();
            if (StringUtils.isNotEmpty(instanceId)) {
                // retrieve InstanceInfo as result;
                DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
                describeInstancesRequest.setRegionId(regionId);
                describeInstancesRequest.setInstanceIds(instanceId);
                final DescribeInstancesResponse describeInstancesResponse = this.retrieveInstance(client, describeInstancesRequest);
                if (describeInstancesResponse.getTotalCount() == 1) {
                    final DescribeInstancesResponse.KVStoreInstance foundRedisInstance = describeInstancesResponse.getInstances().get(0);
                    result = PluginSdkBridge.fromSdk(foundRedisInstance, CoreCreateInstanceResponseDto.class);
                    result.setRequestId(describeInstancesResponse.getRequestId());
                }

            } else {
                // create redis instance
                final CreateInstanceRequest createInstanceRequest = PluginSdkBridge.toSdk(requestDto, CreateInstanceRequest.class);
                CreateInstanceResponse response;
                try {
                    response = this.acsClientStub.request(client, createInstanceRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                result = PluginSdkBridge.fromSdk(response, CoreCreateInstanceResponseDto.class);
            }
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<CoreDeleteInstanceResponseDto> deleteInstance(List<CoreDeleteInstanceRequestDto> coreDeleteInstanceRequestDtoList) throws PluginException {
        List<CoreDeleteInstanceResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteInstanceRequestDto requestDto : coreDeleteInstanceRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String regionId = cloudParamDto.getRegionId();
            final String instanceId = requestDto.getInstanceId();
            requestDto.setRegionId(regionId);

            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
            describeInstancesRequest.setRegionId(regionId);
            describeInstancesRequest.setInstanceIds(instanceId);
            DescribeInstancesResponse describeInstancesResponse = this.retrieveInstance(client, describeInstancesRequest);
            if (0 == describeInstancesResponse.getTotalCount()) {
                String msg = String.format("The given redis instanceID: [%s] has already been deleted.", instanceId);
                logger.error(msg);
                throw new PluginException(msg);
            }

            final DeleteInstanceRequest createInstanceRequest = PluginSdkBridge.toSdk(requestDto, DeleteInstanceRequest.class);
            DeleteInstanceResponse response;
            try {
                response = this.acsClientStub.request(client, createInstanceRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }

            describeInstancesResponse = this.retrieveInstance(client, describeInstancesRequest);
            if (0 != describeInstancesResponse.getTotalCount()) {
                String msg = String.format("The given redis instanceID: [%s] hasn't been deleted.", instanceId);
                logger.error(msg);
                throw new PluginException(msg);
            }

            CoreDeleteInstanceResponseDto result = PluginSdkBridge.fromSdk(response, CoreDeleteInstanceResponseDto.class);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }
        return resultList;
    }

    private DescribeInstancesResponse retrieveInstance(IAcsClient client, DescribeInstancesRequest request) throws PluginException {
        if (StringUtils.isEmpty(request.getRegionId())) {
            logger.error("Cannot retrieve redis instance while regionId is null or empty");
        }

        DescribeInstancesResponse response;
        try {
            response = this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }
        return response;
    }
}
