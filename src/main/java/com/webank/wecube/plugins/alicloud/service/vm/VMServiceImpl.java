package com.webank.wecube.plugins.alicloud.service.vm;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vm.CoreCreateVMRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vm.CoreCreateVMResponseDto;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
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
public class VMServiceImpl implements VMService {
    private static final Logger logger = LoggerFactory.getLogger(VMService.class);

    private AcsClientStub acsClientStub;

    @Autowired
    public VMServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    @Override
    public List<CoreCreateVMResponseDto> createVM(List<CoreCreateVMRequestDto> coreCreateVMRequestDtoList) {
        List<CoreCreateVMResponseDto> resultList = new ArrayList<>();
        for (CoreCreateVMRequestDto request : coreCreateVMRequestDtoList) {
            final String instanceId = request.getInstanceId();
            if (!StringUtils.isEmpty(instanceId)) {
                final DescribeInstancesResponse response = this.retrieveVM(request.getRegionId(), request.getInstanceId());
                if (response.getTotalCount() == 1) {
                    final DescribeInstancesResponse.Instance foundInstance = response.getInstances().get(0);
                    resultList.add(new CoreCreateVMResponseDto(response.getRequestId(), foundInstance.getInstanceId()));

                }
                continue;
            }

            if (StringUtils.isAnyEmpty(request.getImageId(), request.getInstanceType(), request.getZoneId(), request.getRegionId())) {
                String msg = "Any of requested fields: ImageId, InstanceType, ZoneId, RegionId cannot be null or empty";
                logger.error(msg);
                throw new PluginException(msg);
            }

            final IAcsClient client = this.acsClientStub.generateAcsClient(request.getRegionId());

            // create VM instance
            final CreateInstanceRequest aliCloudRequest = CoreCreateVMRequestDto.toSdk(request);
            final CreateInstanceResponse createInstanceResponse = this.acsClientStub.request(client, aliCloudRequest);

            resultList.add(CoreCreateVMResponseDto.fromSdk(createInstanceResponse));

        }
        return resultList;
    }

    @Override
    public DescribeInstancesResponse retrieveVM(String regionId, String instanceId) {
        logger.info("Retrieving created VM instance info.\nValidating regionId field.");
        if (StringUtils.isEmpty(regionId)) {
            String msg = "The regionId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        logger.info(String.format("Retrieving VM instance info, region ID: [%s], VM instance ID: [%s]", regionId, instanceId));

        final IAcsClient client = this.acsClientStub.generateAcsClient(regionId);
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        request.setRegionId(regionId);
        request.setInstanceIds(instanceId);

        return this.acsClientStub.request(client, request);
    }

    @Override
    public void deleteVM(List<DeleteInstanceRequest> deleteInstanceRequestList) {
        for (DeleteInstanceRequest deleteInstanceRequest : deleteInstanceRequestList) {
            logger.info("Deleting VM instance, VM instance ID: [{}], VM instance region:[{}]", deleteInstanceRequest.getInstanceId(), deleteInstanceRequest.getRegionId());
            if (StringUtils.isEmpty(deleteInstanceRequest.getInstanceId())) {
                throw new PluginException("The VM instance id cannot be empty or null.");
            }

            // check if VM instance already deleted
            if (0 == this.retrieveVM(deleteInstanceRequest.getRegionId(), deleteInstanceRequest.getInstanceId()).getTotalCount()) {
                continue;
            }

            // delete VM instance
            final IAcsClient client = this.acsClientStub.generateAcsClient(deleteInstanceRequest.getRegionId());
            this.acsClientStub.request(client, deleteInstanceRequest);

            // re-check if VM instance has already been deleted
            if (0 != this.retrieveVM(deleteInstanceRequest.getRegionId(), deleteInstanceRequest.getInstanceId()).getTotalCount()) {
                String msg = String.format("The VM instance: [%s] from region: [%s] hasn't been deleted", deleteInstanceRequest.getInstanceId(), deleteInstanceRequest.getRegionId());
                logger.error(msg);
                throw new PluginException(msg);
            }
        }
    }

    @Override
    public List<StartInstanceResponse> startVM(List<StartInstanceRequest> startInstanceRequestList) {
        List<StartInstanceResponse> result = new ArrayList<>();
        for (StartInstanceRequest startInstanceRequest : startInstanceRequestList) {
            final IAcsClient client = this.acsClientStub.generateAcsClient(startInstanceRequest.getRegionId());
            final StartInstanceResponse startInstanceResponse = this.acsClientStub.request(client, startInstanceRequest);
            result.add(startInstanceResponse);
        }
        return result;
    }

    @Override
    public List<StopInstanceResponse> stopVM(List<StopInstanceRequest> stopInstanceRequestList) {
        List<StopInstanceResponse> result = new ArrayList<>();
        for (StopInstanceRequest stopInstanceRequest : stopInstanceRequestList) {
            final IAcsClient client = this.acsClientStub.generateAcsClient(stopInstanceRequest.getRegionId());
            final StopInstanceResponse stopInstanceResponse = this.acsClientStub.request(client, stopInstanceRequest);
            result.add(stopInstanceResponse);
        }
        return result;
    }

}
