package com.webank.wecube.plugins.alicloud.service.ecs.vm;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.ecs.vm.*;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.ECSResourceSeeker;
import com.webank.wecube.plugins.alicloud.support.password.PasswordManager;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimer;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimerTask;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author howechen
 */
@Service
public class VMServiceImpl implements VMService {

    private static final String VM_INSTANCE_PENDING = "Pending";

    private static final Logger logger = LoggerFactory.getLogger(VMService.class);

    private final AcsClientStub acsClientStub;
    private final DtoValidator dtoValidator;
    private final PasswordManager passwordManager;
    private final ECSResourceSeeker ecsResourceSeeker;

    @Autowired
    public VMServiceImpl(AcsClientStub acsClientStub, DtoValidator dtoValidator, PasswordManager passwordManager, ECSResourceSeeker ecsResourceSeeker) {
        this.acsClientStub = acsClientStub;
        this.dtoValidator = dtoValidator;
        this.passwordManager = passwordManager;
        this.ecsResourceSeeker = ecsResourceSeeker;
    }

    @Override
    public List<CoreCreateVMResponseDto> createVM(List<CoreCreateVMRequestDto> coreCreateVMRequestDtoList) throws PluginException {
        List<CoreCreateVMResponseDto> resultList = new ArrayList<>();
        for (CoreCreateVMRequestDto requestDto : coreCreateVMRequestDtoList) {
            CoreCreateVMResponseDto result = new CoreCreateVMResponseDto();
            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                String password = requestDto.getPassword();

                final String instanceId = requestDto.getInstanceId();
                if (StringUtils.isNotEmpty(instanceId)) {
                    final DescribeInstancesResponse response = this.retrieveVM(client, regionId, requestDto.getInstanceId());
                    if (response.getTotalCount() == 1) {
                        final DescribeInstancesResponse.Instance foundInstance = response.getInstances().get(0);
                        result = result.fromSdkCrossLineage(foundInstance);
                        result.setRequestId(response.getRequestId());
                        continue;
                    }
                }

                // check password field, if empty, generate one
                if (StringUtils.isEmpty(password)) {
                    password = passwordManager.generatePassword();
                    requestDto.setPassword(password);
                }

                // seek available resource when instanceType is not designated
                if (!StringUtils.isEmpty(requestDto.getInstanceSpec()) && StringUtils.isEmpty(requestDto.getInstanceType())) {
                    final String availableInstanceType = ecsResourceSeeker.findAvailableInstance(client, regionId, requestDto.getZoneId(), requestDto.getInstanceChargeType(), requestDto.getInstanceSpec());
                    requestDto.setInstanceType(availableInstanceType);
                }


                // create VM instance
                logger.info("Creating VM instance: {}", requestDto.toString());

                final CreateInstanceRequest request = requestDto.toSdk();
                request.setRegionId(regionId);

                CreateInstanceResponse response;
                response = this.acsClientStub.request(client, request);

                // wait till VM instance finish its create process
                Function<?, Boolean> checkIfVMFinishCreation = o -> !this.checkIfVMInStatus(client, regionId, response.getInstanceId(), InstanceStatus.PENDING);
                PluginTimer.runTask(new PluginTimerTask(checkIfVMFinishCreation));

                // start the vm
                startVM(client, regionId, response.getInstanceId());

                // encrypt the password then
                final String guid = requestDto.getGuid();
                final String seed = requestDto.getSeed();
                final String encryptedPassword = passwordManager.encryptPassword(guid, seed, password);

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
    public DescribeInstancesResponse retrieveVM(IAcsClient client, String regionId, String instanceId) throws PluginException, AliCloudException {
        logger.info("Retrieving created VM instance info.\nValidating regionId field.");
        if (StringUtils.isAnyEmpty(regionId, instanceId)) {
            String msg = "Either the regionId or instanceId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        logger.info(String.format("Retrieving VM instance info, region ID: [%s], VM instance ID: [%s]", regionId, instanceId));

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        request.setRegionId(regionId);
        request.setInstanceIds(PluginStringUtils.stringifyList(instanceId));

        DescribeInstancesResponse response;
        response = this.acsClientStub.request(client, request);
        return response;
    }

    @Override
    public List<CoreDeleteVMResponseDto> deleteVM(List<CoreDeleteVMRequestDto> coreDeleteVMRequestDtoList) throws PluginException {
        List<CoreDeleteVMResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteVMRequestDto requestDto : coreDeleteVMRequestDtoList) {
            CoreDeleteVMResponseDto result = new CoreDeleteVMResponseDto();
            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String instanceId = requestDto.getInstanceId();

                final DescribeInstancesResponse foundInstanceResponse = this.retrieveVM(client, regionId, instanceId);

                // check if VM instance already deleted
                if (0 == foundInstanceResponse.getTotalCount()) {
                    continue;
                }


                // delete VM instance
                logger.info("Deleting VM instance: {}", requestDto.toString());

                DeleteInstanceRequest deleteInstanceRequest = requestDto.toSdk();
                deleteInstanceRequest.setRegionId(regionId);
                DeleteInstanceResponse response;
                response = this.acsClientStub.request(client, deleteInstanceRequest);


                // re-check if VM instance has already been deleted
                Function<?, Boolean> func = o -> this.ifVMHasBeenDeleted(client, regionId, instanceId);
                PluginTimer.runTask(new PluginTimerTask(func));

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
    public List<CoreStartVMResponseDto> startVM(List<CoreStartVMRequestDto> coreStartVMRequestDtoList) throws PluginException {
        List<CoreStartVMResponseDto> resultList = new ArrayList<>();
        for (CoreStartVMRequestDto requestDto : coreStartVMRequestDtoList) {

            CoreStartVMResponseDto result = new CoreStartVMResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                logger.info("Starting VM instance: {}", requestDto.toString());

                final StartInstanceRequest startInstanceRequest = requestDto.toSdk();
                startInstanceRequest.setRegionId(regionId);
                StartInstanceResponse response;
                response = this.acsClientStub.request(client, startInstanceRequest);

                Function<?, Boolean> checkIfVMFinishCreation = o -> this.checkIfVMInStatus(client, regionId, requestDto.getInstanceId(), InstanceStatus.RUNNING);
                PluginTimer.runTask(new PluginTimerTask(checkIfVMFinishCreation));

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
    public List<CoreStopVMResponseDto> stopVM(List<CoreStopVMRequestDto> coreStopVMRequestDtoList) throws PluginException {
        List<CoreStopVMResponseDto> resultList = new ArrayList<>();
        for (CoreStopVMRequestDto requestDto : coreStopVMRequestDtoList) {
            CoreStopVMResponseDto result = new CoreStopVMResponseDto();
            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                logger.info("Stopping VM instance: {}", requestDto.toString());

                final StopInstanceRequest stopInstanceRequest = requestDto.toSdk();
                stopInstanceRequest.setRegionId(regionId);

                StopInstanceResponse response;
                response = this.acsClientStub.request(client, stopInstanceRequest);

                Function<?, Boolean> checkIfVMFinishCreation = o -> this.checkIfVMInStatus(client, regionId, requestDto.getInstanceId(), InstanceStatus.STOPPED);
                PluginTimer.runTask(new PluginTimerTask(checkIfVMFinishCreation));

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
    public List<CoreBindSecurityGroupResponseDto> bindSecurityGroup(List<CoreBindSecurityGroupRequestDto> coreBindSecurityGroupRequestDtoList) throws PluginException {
        List<CoreBindSecurityGroupResponseDto> resultList = new ArrayList<>();
        for (CoreBindSecurityGroupRequestDto requestDto : coreBindSecurityGroupRequestDtoList) {
            CoreBindSecurityGroupResponseDto result = new CoreBindSecurityGroupResponseDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final String securityGroupId = requestDto.getSecurityGroupId();
                final String instanceId = requestDto.getInstanceId();

                final DescribeInstancesResponse retrieveVMResponse = this.retrieveVM(client, regionId, instanceId);
                if (0 == retrieveVMResponse.getTotalCount()) {
                    String msg = String.format("Cannot retrieve instance info according to given regionId: [%s] and instanceId: [%s]", regionId, instanceId);
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                final DescribeInstancesResponse.Instance foundInstance = retrieveVMResponse.getInstances().get(0);
                List<String> currentSecurityGroupIdList = foundInstance.getSecurityGroupIds();
                currentSecurityGroupIdList.add(securityGroupId);
                currentSecurityGroupIdList = currentSecurityGroupIdList.stream().distinct().collect(Collectors.toList());

                logger.info("Binding security group with VM instance: {}", requestDto.toString());

                ModifyInstanceAttributeRequest request = requestDto.toSdk();
                request.setRegionId(regionId);
                request.setSecurityGroupIdss(currentSecurityGroupIdList);

                ModifyInstanceAttributeResponse response;
                response = this.acsClientStub.request(client, request);
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
    public Boolean checkIfVMInStatus(IAcsClient client, String regionId, String instanceId, InstanceStatus status) throws PluginException, AliCloudException {

        if (StringUtils.isAnyEmpty(regionId, instanceId)) {
            throw new PluginException("Either regionId or instanceId cannot be null or empty.");
        }

        DescribeInstanceStatusRequest request = new DescribeInstanceStatusRequest();
        request.setRegionId("cn-shanghai");
        request.setInstanceIds(Collections.singletonList(instanceId));

        DescribeInstanceStatusResponse foundInstance;
        foundInstance = this.acsClientStub.request(client, request);

        final Optional<DescribeInstanceStatusResponse.InstanceStatus> first = foundInstance.getInstanceStatuses().stream().filter(instance -> StringUtils.equals(instanceId, instance.getInstanceId())).findFirst();

        first.orElseThrow(() -> new PluginException(String.format("Cannot find instance by given Id: %s", instanceId)));

        return StringUtils.equals(status.getStatus(), first.get().getStatus());
    }

    @Override
    public void startVM(IAcsClient client, String regionId, String instanceId) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, instanceId)) {
            throw new PluginException("Either regionId or instanceId cannot be null or empty.");
        }

        StartInstanceRequest request = new StartInstanceRequest();
        request.setRegionId(regionId);
        request.setInstanceId(instanceId);

        this.acsClientStub.request(client, request);

        Function<?, Boolean> checkIfVMFinishCreation = o -> this.checkIfVMInStatus(client, regionId, instanceId, InstanceStatus.RUNNING);
        PluginTimer.runTask(new PluginTimerTask(checkIfVMFinishCreation));
    }

    @Override
    public String getVMIpAddress(IAcsClient client, String regionId, String instanceId) throws PluginException, AliCloudException {
        final DescribeInstancesResponse describeInstancesResponse = retrieveVM(client, regionId, instanceId);
        if (describeInstancesResponse.getTotalCount().equals(0)) {
            throw new PluginException("Cannot find VM instance by given id");
        }

        final DescribeInstancesResponse.Instance instance = describeInstancesResponse.getInstances().get(0);

        if (null != instance.getEipAddress()) {
            return instance.getEipAddress().getIpAddress();
        }

        if (!instance.getPublicIpAddress().isEmpty()) {
            return instance.getPublicIpAddress().get(0);
        }

        if (!instance.getInnerIpAddress().isEmpty()) {
            return instance.getInnerIpAddress().get(0);
        }

        if (!instance.getNetworkInterfaces().isEmpty()) {
            return instance.getNetworkInterfaces().get(0).getPrimaryIpAddress();
        }

        throw new PluginException("The VM instance doesn't have IP address to connect to.");
    }

    private Boolean ifVMHasBeenDeleted(IAcsClient client, String regionId, String instanceId) {
        final DescribeInstancesResponse describeInstancesResponse = this.retrieveVM(client, regionId, instanceId);
        return describeInstancesResponse.getTotalCount() == 0;
    }


}
