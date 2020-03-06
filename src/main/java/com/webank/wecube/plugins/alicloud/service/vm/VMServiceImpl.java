package com.webank.wecube.plugins.alicloud.service.vm;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.vm.*;
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
        for (CoreCreateVMRequestDto request : coreCreateVMRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(request.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(request.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();

            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            final String instanceId = request.getInstanceId();
            if (StringUtils.isNotEmpty(instanceId)) {
                final DescribeInstancesResponse response = this.retrieveVM(client, regionId, request.getInstanceId());
                if (response.getTotalCount() == 1) {
                    final DescribeInstancesResponse.Instance foundInstance = response.getInstances().get(0);
                    resultList.add(new CoreCreateVMResponseDto(response.getRequestId(), foundInstance.getInstanceId()));
                }
            } else {
                //            if (PluginStringUtils.isAnyEmpty(request.getImageId(), request.getInstanceType(), request.getZoneId(), request.getRegionId())) {
//                String msg = "Any of requested fields: ImageId, InstanceType, ZoneId, RegionId cannot be null or empty";
//                logger.error(msg);
//                throw new PluginException(msg);
//            }

                // create VM instance
                final CreateInstanceRequest aliCloudRequest = CoreCreateVMRequestDto.toSdk(request);
                aliCloudRequest.setRegionId(regionId);
                CreateInstanceResponse createInstanceResponse;
                try {
                    createInstanceResponse = this.acsClientStub.request(client, aliCloudRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                resultList.add(CoreCreateVMResponseDto.fromSdk(createInstanceResponse));
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
//        request.setInstanceIds(instanceId);
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
    public void deleteVM(List<CoreDeleteVMRequestDto> coreDeleteVMRequestDtoList) throws PluginException {
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
            try {
                this.acsClientStub.request(client, deleteInstanceRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }


            // re-check if VM instance has already been deleted
            if (0 != this.retrieveVM(client, regionId, instanceId).getTotalCount()) {
                String msg = String.format("The VM instance: [%s] from region: [%s] hasn't been deleted", instanceId, regionId);
                logger.error(msg);
                throw new PluginException(msg);
            }
        }
    }

    @Override
    public List<StartInstanceResponse> startVM(List<CoreStartVMRequestDto> coreStartVMRequestDtoList) throws PluginException {
        List<StartInstanceResponse> result = new ArrayList<>();
        for (CoreStartVMRequestDto request : coreStartVMRequestDtoList) {

            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(request.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(request.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            StartInstanceResponse startInstanceResponse;
            try {
                startInstanceResponse = this.acsClientStub.request(client, request);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }
            result.add(startInstanceResponse);
        }
        return result;
    }

    @Override
    public List<StopInstanceResponse> stopVM(List<CoreStopVMRequestDto> coreStopVMRequestDtoList) throws PluginException {
        List<StopInstanceResponse> result = new ArrayList<>();
        for (CoreStopVMRequestDto request : coreStopVMRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(request.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(request.getCloudParams());
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            StopInstanceResponse stopInstanceResponse;
            try {
                stopInstanceResponse = this.acsClientStub.request(client, request);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }
            result.add(stopInstanceResponse);
        }
        return result;
    }

    @Override
    public void bindSecurityGroup(IAcsClient client, String regionId, String instanceId, String securityGroupId) throws PluginException {

        if (StringUtils.isAnyEmpty(instanceId, securityGroupId)) {
            String msg = "Either instance ID or security group ID cannot be null or empty";
            logger.error(msg);
            throw new PluginException(msg);
        }

        ModifyInstanceAttributeRequest request = new ModifyInstanceAttributeRequest();
        request.setRegionId(regionId);
        request.setInstanceId(instanceId);
        request.getSecurityGroupIdss().add(securityGroupId);

        try {
            this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }

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
