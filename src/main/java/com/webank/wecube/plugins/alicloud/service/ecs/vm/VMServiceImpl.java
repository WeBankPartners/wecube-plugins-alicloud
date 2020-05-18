package com.webank.wecube.plugins.alicloud.service.ecs.vm;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.cloudParam.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.ecs.vm.*;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
import com.webank.wecube.plugins.alicloud.support.password.PasswordManager;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.ECSResourceSeeker;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs.SpecInfo;
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
                SpecInfo fitSpec = new SpecInfo();
                if (!StringUtils.isEmpty(requestDto.getInstanceSpec()) && StringUtils.isEmpty(requestDto.getInstanceType())) {
                    fitSpec = ecsResourceSeeker.findAvailableInstance(client, regionId, requestDto.getZoneId(), requestDto.getInstanceChargeType(), requestDto.getInstanceSpec(), requestDto.getInstanceFamily());
                    requestDto.setInstanceType(fitSpec.getResourceClass());
                }


                // create VM instance
                logger.info("Creating VM instance: {}", requestDto.toString());

                final CreateInstanceRequest request = requestDto.toSdk();

                CreateInstanceResponse response;
                response = this.acsClientStub.request(client, request, regionId);

                // wait till VM instance finish its create process
                Function<?, Boolean> checkIfVMFinishCreation = o -> !this.checkIfVMInStatus(client, regionId, response.getInstanceId(), InstanceStatus.PENDING);
                PluginTimer.runTask(new PluginTimerTask(checkIfVMFinishCreation));

                // start the vm
                startVM(client, regionId, response.getInstanceId());

                // encrypt the password then
                final String guid = requestDto.getGuid();
                final String seed = requestDto.getSeed();
                final String encryptedPassword = passwordManager.encryptPassword(guid, seed, password);

                // query created instance and get the private ip
                final String instancePrivateIp = getInstancePrivateIp(client, regionId, response.getInstanceId());

                result = result.fromSdk(response, encryptedPassword, instancePrivateIp, fitSpec);
            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
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
        request.setInstanceIds(PluginStringUtils.stringifyList(instanceId));

        DescribeInstancesResponse response;
        response = this.acsClientStub.request(client, request, regionId);
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
                DeleteInstanceResponse response;
                response = this.acsClientStub.request(client, deleteInstanceRequest, regionId);


                // re-check if VM instance has already been deleted
                Function<?, Boolean> func = o -> this.ifVMHasBeenDeleted(client, regionId, instanceId);
                PluginTimer.runTask(new PluginTimerTask(func));

                result = result.fromSdk(response);


            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
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
                StartInstanceResponse response;
                response = this.acsClientStub.request(client, startInstanceRequest, regionId);

                Function<?, Boolean> checkIfVMFinishCreation = o -> this.checkIfVMInStatus(client, regionId, requestDto.getInstanceId(), InstanceStatus.RUNNING);
                PluginTimer.runTask(new PluginTimerTask(checkIfVMFinishCreation));

                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
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

                StopInstanceResponse response;
                response = this.acsClientStub.request(client, stopInstanceRequest, regionId);

                Function<?, Boolean> checkIfVMFinishCreation = o -> this.checkIfVMInStatus(client, regionId, requestDto.getInstanceId(), InstanceStatus.STOPPED);
                PluginTimer.runTask(new PluginTimerTask(checkIfVMFinishCreation));

                result = result.fromSdk(response);
            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
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
    public List<CoreModifyInstanceAttributeResponesDto> bindSecurityGroup(List<CoreModifyInstanceAttributeRequestDto> coreModifyInstanceAttributeRequestDtoList) throws PluginException {
        List<CoreModifyInstanceAttributeResponesDto> resultList = new ArrayList<>();
        for (CoreModifyInstanceAttributeRequestDto requestDto : coreModifyInstanceAttributeRequestDtoList) {
            CoreModifyInstanceAttributeResponesDto result = new CoreModifyInstanceAttributeResponesDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final List<String> securityGroupIdList = PluginStringUtils.splitStringList(requestDto.getSecurityGroupId());
                final String instanceId = requestDto.getInstanceId();

                final DescribeInstancesResponse retrieveVMResponse = this.retrieveVM(client, regionId, instanceId);
                if (0 == retrieveVMResponse.getTotalCount()) {
                    String msg = String.format("Cannot retrieve instance info according to given regionId: [%s] and instanceId: [%s]", regionId, instanceId);
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                final DescribeInstancesResponse.Instance foundInstance = retrieveVMResponse.getInstances().get(0);
                List<String> currentSecurityGroupIdList = foundInstance.getSecurityGroupIds();
                currentSecurityGroupIdList.addAll(securityGroupIdList);
                currentSecurityGroupIdList = currentSecurityGroupIdList.stream().distinct().collect(Collectors.toList());

                logger.info("Binding security group with VM instance: {}", requestDto.toString());

                ModifyInstanceAttributeRequest request = requestDto.toSdk();
                request.setSecurityGroupIdss(currentSecurityGroupIdList);

                ModifyInstanceAttributeResponse response;
                response = this.acsClientStub.request(client, request, regionId);
                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
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
    public List<CoreModifyInstanceAttributeResponesDto> unbindSecurityGroup(List<CoreModifyInstanceAttributeRequestDto> coreModifyInstanceAttributeRequestDtoList) {
        List<CoreModifyInstanceAttributeResponesDto> resultList = new ArrayList<>();
        for (CoreModifyInstanceAttributeRequestDto requestDto : coreModifyInstanceAttributeRequestDtoList) {
            CoreModifyInstanceAttributeResponesDto result = new CoreModifyInstanceAttributeResponesDto();

            try {

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
                final List<String> securityGroupIdList = PluginStringUtils.splitStringList(requestDto.getSecurityGroupId());
                final String instanceId = requestDto.getInstanceId();

                final DescribeInstancesResponse retrieveVMResponse = this.retrieveVM(client, regionId, instanceId);
                if (0 == retrieveVMResponse.getTotalCount()) {
                    String msg = String.format("Cannot retrieve instance info according to given regionId: [%s] and instanceId: [%s]", regionId, instanceId);
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                // current list - target list
                final DescribeInstancesResponse.Instance foundInstance = retrieveVMResponse.getInstances().get(0);
                List<String> currentSecurityGroupIdList = foundInstance.getSecurityGroupIds();
                currentSecurityGroupIdList.removeAll(securityGroupIdList);
                if (currentSecurityGroupIdList.isEmpty()) {
                    throw new PluginException("At least one security group should be remained.");
                }
                currentSecurityGroupIdList = currentSecurityGroupIdList.stream().distinct().collect(Collectors.toList());

                logger.info("Unbinding security group with VM instance: {}", requestDto.toString());

                ModifyInstanceAttributeRequest request = requestDto.toSdk();
                request.setSecurityGroupIdss(currentSecurityGroupIdList);

                ModifyInstanceAttributeResponse response;
                response = this.acsClientStub.request(client, request, regionId);
                result = result.fromSdk(response);

            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } catch (Exception ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setUnhandledErrorMessage(ex.getMessage());
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

        logger.info("Retrieve if VM: [{}] instance in status: [{}]...", instanceId, status.getStatus());

        DescribeInstanceStatusRequest request = new DescribeInstanceStatusRequest();
        request.setInstanceIds(Collections.singletonList(instanceId));

        DescribeInstanceStatusResponse foundInstance;
        foundInstance = this.acsClientStub.request(client, request, regionId);

        final Optional<DescribeInstanceStatusResponse.InstanceStatus> first = foundInstance.getInstanceStatuses().stream().filter(instance -> StringUtils.equals(instanceId, instance.getInstanceId())).findFirst();

        first.orElseThrow(() -> new PluginException(String.format("Cannot find instance by given Id: %s", instanceId)));

        return StringUtils.equals(status.getStatus(), first.get().getStatus());
    }

    @Override
    public void startVM(IAcsClient client, String regionId, String instanceId) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, instanceId)) {
            throw new PluginException("Either regionId or instanceId cannot be null or empty.");
        }

        logger.info("Starting VM instance...");

        StartInstanceRequest request = new StartInstanceRequest();
        request.setInstanceId(instanceId);

        this.acsClientStub.request(client, request, regionId);

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

    private Boolean ifVMHasBeenDeleted(IAcsClient client, String regionId, String instanceId) throws PluginException, AliCloudException {
        final DescribeInstancesResponse describeInstancesResponse = this.retrieveVM(client, regionId, instanceId);
        return describeInstancesResponse.getTotalCount() == 0;
    }

    private String getInstancePrivateIp(IAcsClient client, String regionId, String instanceId) throws AliCloudException {

        logger.info("Query instance private ip...");

        final DescribeInstancesResponse foundInstance = retrieveVM(client, regionId, instanceId);

        if (foundInstance.getInstances().isEmpty()) {
            throw new PluginException(String.format("Cannot find instance by given instanceId: [%s]", instanceId));
        }

        final DescribeInstancesResponse.Instance instance = foundInstance.getInstances().get(0);
        final List<String> innerIpAddressList = instance.getInnerIpAddress();

        if (innerIpAddressList.isEmpty()) {
            return StringUtils.EMPTY;
        }

        if (innerIpAddressList.size() == 1) {
            return innerIpAddressList.get(0);
        } else {
            return PluginStringUtils.stringifyListWithoutBracket(innerIpAddressList);
        }
    }
}
