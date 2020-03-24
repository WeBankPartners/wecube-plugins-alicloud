package com.webank.wecube.plugins.alicloud.service.ecs.vm;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.ecs.vm.*;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.webank.wecube.plugins.alicloud.support.AliCloudConstant.VM_INSTANCE_STOP_STATUS;

/**
 * @author howechen
 */
@Service
public class VMServiceImpl implements VMService {
    private static final Logger logger = LoggerFactory.getLogger(VMService.class);

    private AcsClientStub acsClientStub;

    @Autowired
    public VMServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    @Override
    public List<CoreCreateVMResponseDto> createVM(List<CoreCreateVMRequestDto> coreCreateVMRequestDtoList) throws PluginException {
        List<CoreCreateVMResponseDto> resultList = new ArrayList<>();
        for (CoreCreateVMRequestDto requestDto : coreCreateVMRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();

            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            final String instanceId = requestDto.getInstanceId();
            if (StringUtils.isNotEmpty(instanceId)) {
                final DescribeInstancesResponse response = this.retrieveVM(client, regionId, requestDto.getInstanceId());
                if (response.getTotalCount() == 1) {
                    final DescribeInstancesResponse.Instance foundInstance = response.getInstances().get(0);
                    resultList.add(new CoreCreateVMResponseDto(response.getRequestId(), foundInstance.getInstanceId()));
                }
            } else {
                if (StringUtils.isAnyEmpty(requestDto.getImageId(), requestDto.getInstanceType(), requestDto.getZoneId(), regionId)) {
                    String msg = "Any of requested fields: ImageId, InstanceType, ZoneId, RegionId cannot be null or empty";
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                // create VM instance
                final CreateInstanceRequest aliCloudRequest = CoreCreateVMRequestDto.toSdk(requestDto);
                aliCloudRequest.setRegionId(regionId);
                CreateInstanceResponse createInstanceResponse;
                try {
                    createInstanceResponse = this.acsClientStub.request(client, aliCloudRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                CoreCreateVMResponseDto result = CoreCreateVMResponseDto.fromSdk(createInstanceResponse);
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public DescribeInstancesResponse retrieveVM(IAcsClient client, String regionId, String instanceId) throws PluginException {
        logger.info("Retrieving created VM instance info.\nValidating regionId field.");
        if (StringUtils.isEmpty(regionId)) {
            String msg = "The regionId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        logger.info(String.format("Retrieving VM instance info, region ID: [%s], VM instance ID: [%s]", regionId, instanceId));

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        request.setRegionId(regionId);
        request.setInstanceIds(PluginStringUtils.stringifyList(instanceId));

        DescribeInstancesResponse response;
        try {
            response = this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }
        return response;
    }

    @Override
    public List<CoreDeleteVMResponseDto> deleteVM(List<CoreDeleteVMRequestDto> coreDeleteVMRequestDtoList) throws PluginException {
        List<CoreDeleteVMResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteVMRequestDto requestDto : coreDeleteVMRequestDtoList) {

            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            final String instanceId = requestDto.getInstanceId();

            logger.info("Deleting VM instance, VM instance ID: [{}], VM instance region:[{}]", instanceId, regionId);
            if (StringUtils.isEmpty(instanceId)) {
                throw new PluginException("The VM instance id cannot be empty or null.");
            }

            final DescribeInstancesResponse foundInstanceResponse = this.retrieveVM(client, regionId, instanceId);

            // check if VM instance already deleted
            if (0 == foundInstanceResponse.getTotalCount()) {
                continue;
            }


            // delete VM instance
            DeleteInstanceRequest deleteInstanceRequest = CoreDeleteVMRequestDto.toSdk(requestDto);
            deleteInstanceRequest.setRegionId(regionId);
            DeleteInstanceResponse response;
            try {
                response = this.acsClientStub.request(client, deleteInstanceRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }


            // re-check if VM instance has already been deleted
            if (0 != this.retrieveVM(client, regionId, instanceId).getTotalCount()) {
                String msg = String.format("The VM instance: [%s] from region: [%s] hasn't been deleted", instanceId, regionId);
                logger.error(msg);
                throw new PluginException(msg);
            }

            CoreDeleteVMResponseDto result = CoreDeleteVMResponseDto.fromSdk(response);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<CoreStartVMResponseDto> startVM(List<CoreStartVMRequestDto> coreStartVMRequestDtoList) throws PluginException {
        List<CoreStartVMResponseDto> resultList = new ArrayList<>();
        for (CoreStartVMRequestDto requestDto : coreStartVMRequestDtoList) {

            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            StartInstanceResponse response;
            try {
                response = this.acsClientStub.request(client, requestDto);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }

            final CoreStartVMResponseDto result = CoreStartVMResponseDto.fromSdk(response);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<CoreStopVMResponseDto> stopVM(List<CoreStopVMRequestDto> coreStopVMRequestDtoList) throws PluginException {
        List<CoreStopVMResponseDto> resultList = new ArrayList<>();
        for (CoreStopVMRequestDto requestDto : coreStopVMRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            StopInstanceResponse response;
            try {
                response = this.acsClientStub.request(client, requestDto);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }
            CoreStopVMResponseDto result = CoreStopVMResponseDto.fromSdk(response);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<CoreBindSecurityGroupResponseDto> bindSecurityGroup(List<CoreBindSecurityGroupRequestDto> coreBindSecurityGroupRequestDtoList) throws PluginException {
        List<CoreBindSecurityGroupResponseDto> resultList = new ArrayList<>();
        for (CoreBindSecurityGroupRequestDto requestDto : coreBindSecurityGroupRequestDtoList) {
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

            ModifyInstanceAttributeRequest request = CoreBindSecurityGroupRequestDto.toSdk(requestDto);
            request.setSecurityGroupIdss(currentSecurityGroupIdList);

            ModifyInstanceAttributeResponse response;
            try {
                response = this.acsClientStub.request(client, request);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }

            CoreBindSecurityGroupResponseDto result = CoreBindSecurityGroupResponseDto.fromSdk(response);
            result.setGuid(requestDto.getGuid());
            result.setCallbackParameter(requestDto.getCallbackParameter());
            resultList.add(result);

        }
        return resultList;
    }

    @Override
    public boolean checkIfVMStopped(IAcsClient client, String regionId, String instanceId) {
        logger.info("Retrieving if VM instance has already been stopped.");
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        request.setRegionId(regionId);
        request.setInstanceIds(instanceId);

        DescribeInstancesResponse foundInstance;
        try {
            foundInstance = this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }

        if (null == foundInstance || foundInstance.getTotalCount() == 0) {
            throw new PluginException(String.format("Cannot found instance info with given regionId: [%s] and instance Id: [%s]", regionId, instanceId));
        }


        return StringUtils.equals(VM_INSTANCE_STOP_STATUS, foundInstance.getInstances().get(0).getStatus());
    }

}