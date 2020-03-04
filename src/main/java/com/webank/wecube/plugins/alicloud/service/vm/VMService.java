package com.webank.wecube.plugins.alicloud.service.vm;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.ecs.model.v20140526.StartInstanceResponse;
import com.aliyuncs.ecs.model.v20140526.StopInstanceResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vm.*;

import java.util.List;

/**
 * @author howechen
 */
public interface VMService {

    List<CoreCreateVMResponseDto> createVM(List<CoreCreateVMRequestDto> coreCreateVMRequestDtoList) throws PluginException;

    void deleteVM(List<CoreDeleteVMRequestDto> coreDeleteInstanceRequestDtoList) throws PluginException;

    DescribeInstancesResponse retrieveVM(IAcsClient client, String regionId, String instanceId) throws PluginException;

    List<StartInstanceResponse> startVM(List<CoreStartVMRequestDto> startInstanceRequestList) throws PluginException;

    List<StopInstanceResponse> stopVM(List<CoreStopVMRequestDto> stopInstanceRequestList) throws PluginException;

    void bindSecurityGroup(IAcsClient client, String regionId, String instanceId, String securityGroupId) throws PluginException;

    boolean checkIfVMStopped(IAcsClient client, String regionId, String instanceId) throws PluginException;
}
