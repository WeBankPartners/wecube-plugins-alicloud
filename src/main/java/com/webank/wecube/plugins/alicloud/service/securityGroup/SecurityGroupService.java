package com.webank.wecube.plugins.alicloud.service.securityGroup;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup.*;

import java.util.List;

/**
 * @author howechen
 */
public interface SecurityGroupService {


    List<CoreCreateSecurityGroupResponseDto> createSecurityGroup(List<CoreCreateSecurityGroupRequestDto> requestDtoList) throws PluginException;

    void deleteSecurityGroup(List<CoreDeleteSecurityGroupRequestDto> coreDeleteSecurityGroupRequestDtoList) throws PluginException;

    List<CoreAuthorizeSecurityGroupResponseDto> authorizeSecurityGroup(List<CoreAuthorizeSecurityGroupRequestDto> coreAuthorizeSecurityGroupRequestDtoList) throws PluginException;

    List<CoreRevokeSecurityGroupResponseDto> revokeSecurityGroup(List<CoreRevokeSecurityGroupRequestDto> coreRevokeSecurityGroupRequestDtoList) throws PluginException;
}
