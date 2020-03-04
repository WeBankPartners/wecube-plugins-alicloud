package com.webank.wecube.plugins.alicloud.service.securityGroup;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.securityGroup.CoreCreateSecurityGroupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.securityGroup.CoreCreateSecurityGroupResponseDto;
import com.webank.wecube.plugins.alicloud.dto.securityGroup.CoreDeleteSecurityGroupRequestDto;

import java.util.List;

/**
 * @author howechen
 */
public interface SecurityGroupService {


    List<CoreCreateSecurityGroupResponseDto> createSecurityGroup(List<CoreCreateSecurityGroupRequestDto> requestDtoList) throws PluginException;

    void deleteSecurityGroup(List<CoreDeleteSecurityGroupRequestDto> coreDeleteSecurityGroupRequestDtoList) throws PluginException;
}
