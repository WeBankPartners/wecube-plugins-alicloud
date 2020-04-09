package com.webank.wecube.plugins.alicloud.service.ecs.securityGroup;

import com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup.*;

import java.util.List;

/**
 * @author howechen
 */
public interface SecurityGroupService {


    List<CoreCreateSecurityGroupResponseDto> createSecurityGroup(List<CoreCreateSecurityGroupRequestDto> requestDtoList);

    List<CoreDeleteSecurityGroupResponseDto> deleteSecurityGroup(List<CoreDeleteSecurityGroupRequestDto> coreDeleteSecurityGroupRequestDtoList);

    List<CoreAuthorizeSecurityGroupResponseDto> authorizeSecurityGroup(List<CoreAuthorizeSecurityGroupRequestDto> coreAuthorizeSecurityGroupRequestDtoList);

    List<CoreRevokeSecurityGroupResponseDto> revokeSecurityGroup(List<CoreRevokeSecurityGroupRequestDto> coreRevokeSecurityGroupRequestDtoList);
}
