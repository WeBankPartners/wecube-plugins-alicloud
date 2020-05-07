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
import com.webank.wecube.plugins.alicloud.support.password.PasswordManager;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.RedisResourceSeeker;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimer;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimerTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author howechen
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    private final AcsClientStub acsClientStub;
    private final DtoValidator dtoValidator;
    private final PasswordManager passwordManager;
    private final RedisResourceSeeker redisResourceSeeker;

    @Autowired
    public RedisServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator, PasswordManager passwordManager, RedisResourceSeeker redisResourceSeeker) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
        this.passwordManager = passwordManager;
        this.redisResourceSeeker = redisResourceSeeker;
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

                if (StringUtils.isEmpty(requestDto.getPassword())) {
                    // no password specified, create one
                    final String generatedRedisPassword = passwordManager.generateRedisPassword();
                    requestDto.setPassword(generatedRedisPassword);
                }
                String encryptedPassword = passwordManager.encryptPassword(requestDto.getGuid(), requestDto.getSeed(), requestDto.getPassword());

                // create redis instance
//                logger.info("Creating instance: {}", requestDto.toString());
//                final String foundAvailableResource = redisResourceSeeker.findAvailableResource(
//                        client,
//                        regionId,
//                        requestDto.getZoneId(),
//                        requestDto.getChargeType(),
//                        RedisResourceSeeker.Engine.REDIS.toString(),
//                        RedisResourceSeeker.EditionType.COMMUNITY.toString(),
//                        requestDto.getSeriesType(),
//                        requestDto.getEngineVersion(),
//                        requestDto.getArchitecture(),
//                        requestDto.getShardNumber(),
//                        requestDto.getSupportedNodeType(),
//                        requestDto.getCapacity());
//                requestDto.setInstanceClass(foundAvailableResource);

                final CreateInstanceRequest createInstanceRequest = requestDto.toSdk();
                createInstanceRequest.setRegionId(regionId);
                CreateInstanceResponse response;
                response = this.acsClientStub.request(client, createInstanceRequest);

                logger.info("Retrieving created redis until it's available to be used.");

                Function<?, Boolean> func = o -> ifRedisInStatus(client, regionId, response.getInstanceId(), InstanceStatus.NORMAL);
                PluginTimer.runTask(new PluginTimerTask(func));


                // append security ips to the created instance
                if (StringUtils.isNotEmpty(requestDto.getSecurityIps())) {
                    appendSecurityIps(client, regionId, requestDto.getSecurityIps(), response.getInstanceId(), requestDto.getModifyMode());
                }

                // bind security group to the created instance
                if (StringUtils.isNotEmpty(requestDto.getSecurityGroupId())) {
                    bindSecurityGroup(client, regionId, requestDto.getSecurityGroupId(), response.getInstanceId());
                }


                result = result.fromSdk(response, encryptedPassword);

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

                // delete instance
                logger.info("Deleting instance: {}", requestDto.toString());

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

                logger.info("Retrieving created redis until it has been successfully released.");

                Function<?, Boolean> func = o -> ifRedisIsDeleted(client, regionId, requestDto.getInstanceId());
                PluginTimer.runTask(new PluginTimerTask(func));

                result = PluginSdkBridge.fromSdk(response, CoreDeleteInstanceResponseDto.class);

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
    public Boolean ifRedisInStatus(IAcsClient client, String regionId, String instanceId, InstanceStatus status) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, instanceId)) {
            throw new PluginException("Either regionId or instanceId cannot be null or empty.");
        }

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        request.setRegionId(regionId);
        request.setInstanceIds(instanceId);


        final DescribeInstancesResponse response = this.acsClientStub.request(client, request);

        final Optional<DescribeInstancesResponse.KVStoreInstance> foundRedisOpt = response.getInstances().stream().filter(instance -> StringUtils.equals(instanceId, instance.getInstanceId())).findFirst();

        foundRedisOpt.orElseThrow(() -> new PluginException(String.format("Cannot found instance by instanceId: [%s]", instanceId)));

        final DescribeInstancesResponse.KVStoreInstance foundInstance = foundRedisOpt.get();

        return StringUtils.equals(status.getStatus(), foundInstance.getInstanceStatus());
    }

    public Boolean ifRedisIsDeleted(IAcsClient client, String regionId, String instanceId) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, instanceId)) {
            throw new PluginException("Either regionId or instanceId cannot be null or empty.");
        }

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        request.setRegionId(regionId);
        request.setInstanceIds(instanceId);


        final DescribeInstancesResponse response = this.acsClientStub.request(client, request);

        return response.getTotalCount() == 0;

    }

    private DescribeInstancesResponse retrieveInstance(IAcsClient client, DescribeInstancesRequest request) throws PluginException, AliCloudException {
        if (StringUtils.isEmpty(request.getRegionId())) {
            logger.error("Cannot retrieve redis instance while regionId is null or empty");
        }

        DescribeInstancesResponse response;
        response = this.acsClientStub.request(client, request);
        return response;
    }


    private void bindSecurityGroup(IAcsClient client, String regionId, String securityGroupId, String instanceId) throws AliCloudException {
        ModifySecurityGroupConfigurationRequest request = new ModifySecurityGroupConfigurationRequest();
        request.setSecurityGroupId(securityGroupId);
        request.setDBInstanceId(instanceId);

        acsClientStub.request(client, request, regionId);
    }

    private void appendSecurityIps(IAcsClient client, String regionId, String securityIps, String instanceId, String modifyMode) throws AliCloudException {
        ModifySecurityIpsRequest request = new ModifySecurityIpsRequest();
        request.setSecurityIps(securityIps);
        request.setInstanceId(instanceId);
        request.setModifyMode(modifyMode);

        acsClientStub.request(client, request, regionId);
    }
}
