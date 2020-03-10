package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.securityGroup.*;
import com.webank.wecube.plugins.alicloud.service.securityGroup.SecurityGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/security-group")
public class SecurityGroupController {

    private SecurityGroupService securityGroupService;

    @Autowired
    public SecurityGroupController(SecurityGroupService securityGroupService) {
        this.securityGroupService = securityGroupService;
    }

    @PostMapping(path = "/create")
    @ResponseBody
    public CoreResponseDto<?> createSecurityGroup(@RequestBody CoreRequestDto<CoreCreateSecurityGroupRequestDto> request) {
        List<CoreCreateSecurityGroupResponseDto> result;
        try {
            result = this.securityGroupService.createSecurityGroup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreCreateSecurityGroupResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<?> deleteSecurityGroup(@RequestBody CoreRequestDto<CoreDeleteSecurityGroupRequestDto> request) {
        try {
            this.securityGroupService.deleteSecurityGroup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return CoreResponseDto.okay();
    }

    @PostMapping(path = "/authorize")
    @ResponseBody
    public CoreResponseDto<?> authorizeSecurityGroup(@RequestBody CoreRequestDto<CoreAuthorizeSecurityGroupRequestDto> request) {
        List<CoreAuthorizeSecurityGroupResponseDto> result;
        try {
            result = this.securityGroupService.authorizeSecurityGroup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreAuthorizeSecurityGroupResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/revoke")
    @ResponseBody
    public CoreResponseDto<?> revokeSecurityGroup(@RequestBody CoreRequestDto<CoreRevokeSecurityGroupRequestDto> request) {
        List<CoreRevokeSecurityGroupResponseDto> result;
        try {
            result = this.securityGroupService.revokeSecurityGroup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreRevokeSecurityGroupResponseDto>().okayWithData(result);
    }
}
