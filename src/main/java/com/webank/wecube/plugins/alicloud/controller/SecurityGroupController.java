package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup.*;
import com.webank.wecube.plugins.alicloud.service.securityGroup.SecurityGroupService;
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
    public CoreResponseDtoBkp<?> createSecurityGroup(@RequestBody CoreRequestDtoBkp<CoreCreateSecurityGroupRequestDto> request) {
        List<CoreCreateSecurityGroupResponseDto> result;
        try {
            result = this.securityGroupService.createSecurityGroup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreCreateSecurityGroupResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDtoBkp<?> deleteSecurityGroup(@RequestBody CoreRequestDtoBkp<CoreDeleteSecurityGroupRequestDto> request) {
        try {
            this.securityGroupService.deleteSecurityGroup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return CoreResponseDtoBkp.okay();
    }

    @PostMapping(path = "/authorize")
    @ResponseBody
    public CoreResponseDtoBkp<?> authorizeSecurityGroup(@RequestBody CoreRequestDtoBkp<CoreAuthorizeSecurityGroupRequestDto> request) {
        List<CoreAuthorizeSecurityGroupResponseDto> result;
        try {
            result = this.securityGroupService.authorizeSecurityGroup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreAuthorizeSecurityGroupResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/revoke")
    @ResponseBody
    public CoreResponseDtoBkp<?> revokeSecurityGroup(@RequestBody CoreRequestDtoBkp<CoreRevokeSecurityGroupRequestDto> request) {
        List<CoreRevokeSecurityGroupResponseDto> result;
        try {
            result = this.securityGroupService.revokeSecurityGroup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreRevokeSecurityGroupResponseDto>().okayWithData(result);
    }
}
