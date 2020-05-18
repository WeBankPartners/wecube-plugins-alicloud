package com.webank.wecube.plugins.alicloud.service.redis;

import com.aliyuncs.IAcsClient;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.redis.*;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;

import java.util.List;

/**
 * @author howechen
 */
public interface RedisService {

    List<CoreCreateInstanceResponseDto> createInstance(List<CoreCreateInstanceRequestDto> coreCreateInstanceRequestDtoList);

    List<CoreDeleteInstanceResponseDto> deleteInstance(List<CoreDeleteInstanceRequestDto> coreDeleteInstanceRequestDtoList);

    Boolean ifRedisInStatus(IAcsClient client, String regionId, String instanceId, InstanceStatus status) throws PluginException, AliCloudException;

    List<CoreModifySecurityGroupResponseDto> appendSecurityGroup(List<CoreModifySecurityGroupRequestDto> requestDtoList);

    List<CoreModifySecurityGroupResponseDto> removeSecurityGroup(List<CoreModifySecurityGroupRequestDto> requestDtoList);
}
