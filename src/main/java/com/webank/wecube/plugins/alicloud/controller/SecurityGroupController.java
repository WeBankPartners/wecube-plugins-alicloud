package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup.*;
import com.webank.wecube.plugins.alicloud.service.ecs.securityGroup.SecurityGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/security_group")
public class SecurityGroupController {

    private SecurityGroupService securityGroupService;

    @Autowired
    public SecurityGroupController(SecurityGroupService securityGroupService) {
        this.securityGroupService = securityGroupService;
    }

    @PostMapping(path = "/create")
    @ResponseBody
    public CoreResponseDto<CoreCreateSecurityGroupResponseDto> createSecurityGroup(@RequestBody CoreRequestDto<CoreCreateSecurityGroupRequestDto> request) {
        List<CoreCreateSecurityGroupResponseDto> result = this.securityGroupService.createSecurityGroup(request.getInputs());
        return new CoreResponseDto<CoreCreateSecurityGroupResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteSecurityGroupResponseDto> deleteSecurityGroup(@RequestBody CoreRequestDto<CoreDeleteSecurityGroupRequestDto> request) {
        List<CoreDeleteSecurityGroupResponseDto> result = this.securityGroupService.deleteSecurityGroup(request.getInputs());
        return new CoreResponseDto<CoreDeleteSecurityGroupResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/authorize")
    @ResponseBody
    public CoreResponseDto<CoreAuthorizeSecurityGroupResponseDto> authorizeSecurityGroup(@RequestBody CoreRequestDto<CoreAuthorizeSecurityGroupRequestDto> request) {
        List<CoreAuthorizeSecurityGroupResponseDto> result = this.securityGroupService.authorizeSecurityGroup(request.getInputs());
        return new CoreResponseDto<CoreAuthorizeSecurityGroupResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/revoke")
    @ResponseBody
    public CoreResponseDto<CoreRevokeSecurityGroupResponseDto> revokeSecurityGroup(@RequestBody CoreRequestDto<CoreRevokeSecurityGroupRequestDto> request) {
        List<CoreRevokeSecurityGroupResponseDto> result = this.securityGroupService.revokeSecurityGroup(request.getInputs());
        return new CoreResponseDto<CoreRevokeSecurityGroupResponseDto>().withErrorCheck(result);
    }
}
