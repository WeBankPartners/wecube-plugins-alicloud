package com.webank.wecube.plugins.alicloud.service.vm;

import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vm.CoreCreateVMRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vm.CoreCreateVMResponseDto;

import java.util.List;

/**
 * @author howechen
 */
public interface VMService {

    List<CoreCreateVMResponseDto> createVM(List<CoreCreateVMRequestDto> coreCreateVMRequestDtoList) throws PluginException;

    void deleteVM(List<DeleteInstanceRequest> deleteInstanceRequestList) throws PluginException;

    DescribeInstancesResponse retrieveVM(String regionId, String instanceId) throws PluginException;

    List<StartInstanceResponse> startVM(List<StartInstanceRequest> startInstanceRequestList) throws PluginException;

    List<StopInstanceResponse> stopVM(List<StopInstanceRequest> stopInstanceRequestList) throws PluginException;

    void bindSecurityGroup(String regionId, String instanceId, String securityGroupId) throws PluginException;
}
