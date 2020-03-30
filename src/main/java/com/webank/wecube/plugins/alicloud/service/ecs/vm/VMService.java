package com.webank.wecube.plugins.alicloud.service.ecs.vm;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.ecs.vm.*;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;

import java.util.List;

/**
 * @author howechen
 */
public interface VMService {

    List<CoreCreateVMResponseDto> createVM(List<CoreCreateVMRequestDto> coreCreateVMRequestDtoList);

    List<CoreDeleteVMResponseDto> deleteVM(List<CoreDeleteVMRequestDto> coreDeleteInstanceRequestDtoList);

    DescribeInstancesResponse retrieveVM(IAcsClient client, String regionId, String instanceId) throws PluginException, AliCloudException;

    List<CoreStartVMResponseDto> startVM(List<CoreStartVMRequestDto> startInstanceRequestList);

    List<CoreStopVMResponseDto> stopVM(List<CoreStopVMRequestDto> stopInstanceRequestList);

    List<CoreBindSecurityGroupResponseDto> bindSecurityGroup(List<CoreBindSecurityGroupRequestDto> coreBindSecurityGroupRequestDtoList);

    Boolean checkIfVMInStatus(IAcsClient client, String regionId, String instanceId, InstanceStatus status) throws PluginException, AliCloudException;

    void startVM(IAcsClient client, String regionId, String instanceId) throws PluginException, AliCloudException;
}
