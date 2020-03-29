package com.webank.wecube.plugins.alicloud.service.redis;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.r_kvstore.model.v20150101.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreCreateInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreCreateInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreDeleteInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreDeleteInstanceResponseDto;
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
public class RedisServiceImpl implements RedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisService.class);

    private AcsClientStub acsClientStub;
    private DtoValidator dtoValidator;

    @Autowired
    public RedisServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
    }

    @Override
    public List<CoreCreateInstanceResponseDto> createInstance(List<CoreCreateInstanceRequestDto> coreCreateInstanceRequestDtoList) {
        List<CoreCreateInstanceResponseDto> resultList = new ArrayList<>();
        for (CoreCreateInstanceRequestDto requestDto : coreCreateInstanceRequestDtoList) {
            CoreCreateInstanceResponseDto result = new CoreCreateInstanceResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();
                final String instanceId = requestDto.getInstanceId();

                if (StringUtils.isNotEmpty(instanceId)) {
                    // retrieve InstanceInfo as result;
                    DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
                    describeInstancesRequest.setRegionId(regionId);
                    describeInstancesRequest.setInstanceIds(instanceId);
                    final DescribeInstancesResponse describeInstancesResponse = this.retrieveInstance(client, describeInstancesRequest);
                    if (describeInstancesResponse.getTotalCount() == 1) {
                        final DescribeInstancesResponse.KVStoreInstance foundRedisInstance = describeInstancesResponse.getInstances().get(0);
                        result = result.fromSdkCrossLineage(foundRedisInstance);
                        result.setRequestId(describeInstancesResponse.getRequestId());
                        continue;
                    }
                }

                // create redis instance
                final CreateInstanceRequest createInstanceRequest = requestDto.toSdk();
                createInstanceRequest.setRegionId(regionId);
                CreateInstanceResponse response;
                response = this.acsClientStub.request(client, createInstanceRequest);
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
    public List<CoreDeleteInstanceResponseDto> deleteInstance(List<CoreDeleteInstanceRequestDto> coreDeleteInstanceRequestDtoList) throws PluginException {
        List<CoreDeleteInstanceResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteInstanceRequestDto requestDto : coreDeleteInstanceRequestDtoList) {
            CoreDeleteInstanceResponseDto result = new CoreDeleteInstanceResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String regionId = cloudParamDto.getRegionId();
                final String instanceId = requestDto.getInstanceId();

                DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
                describeInstancesRequest.setRegionId(regionId);
                describeInstancesRequest.setInstanceIds(instanceId);
                DescribeInstancesResponse describeInstancesResponse = this.retrieveInstance(client, describeInstancesRequest);
                if (0 == describeInstancesResponse.getTotalCount()) {
                    String msg = String.format("The given redis instanceID: [%s] has already been deleted.", instanceId);
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                final DeleteInstanceRequest deleteInstanceRequest = requestDto.toSdk();
                deleteInstanceRequest.setRegionId(regionId);
                DeleteInstanceResponse response;
                response = this.acsClientStub.request(client, deleteInstanceRequest);

                describeInstancesResponse = this.retrieveInstance(client, describeInstancesRequest);
                if (0 != describeInstancesResponse.getTotalCount()) {
                    String msg = String.format("The given redis instanceID: [%s] hasn't been deleted.", instanceId);
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                result = PluginSdkBridge.fromSdk(response, CoreDeleteInstanceResponseDto.class);

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

    private DescribeInstancesResponse retrieveInstance(IAcsClient client, DescribeInstancesRequest request) throws PluginException, AliCloudException {
        if (StringUtils.isEmpty(request.getRegionId())) {
            logger.error("Cannot retrieve redis instance while regionId is null or empty");
        }

        DescribeInstancesResponse response;
        response = this.acsClientStub.request(client, request);
        return response;
    }
}
