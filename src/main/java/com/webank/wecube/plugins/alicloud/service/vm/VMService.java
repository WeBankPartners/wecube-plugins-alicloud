package com.webank.wecube.plugins.alicloud.service.vm;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vm.*;

import java.util.List;

/**
 * @author howechen
 */
public interface VMService {

    List<CoreCreateVMResponseDto> createVM(List<CoreCreateVMRequestDto> coreCreateVMRequestDtoList) throws PluginException;

    List<CoreDeleteVMResponseDto> deleteVM(List<CoreDeleteVMRequestDto> coreDeleteInstanceRequestDtoList) throws PluginException;

    DescribeInstancesResponse retrieveVM(IAcsClient client, String regionId, String instanceId) throws PluginException;

    List<CoreStartVMResponseDto> startVM(List<CoreStartVMRequestDto> startInstanceRequestList) throws PluginException;

    List<CoreStopVMResponseDto> stopVM(List<CoreStopVMRequestDto> stopInstanceRequestList) throws PluginException;

    List<CoreBindSecurityGroupResponseDto> bindSecurityGroup(List<CoreBindSecurityGroupRequestDto> coreBindSecurityGroupRequestDtoList) throws PluginException;

    boolean checkIfVMStopped(IAcsClient client, String regionId, String instanceId) throws PluginException;
}
