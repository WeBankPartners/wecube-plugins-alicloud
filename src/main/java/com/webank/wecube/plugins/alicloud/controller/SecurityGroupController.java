package com.webank.wecube.plugins.alicloud.controller;

import com.aliyuncs.ecs.model.v20140526.DeleteSecurityGroupRequest;
import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.securityGroup.CoreCreateSecurityGroupRequestDto;
import com.webank.wecube.plugins.alicloud.dto.securityGroup.CoreCreateSecurityGroupResponseDto;
import com.webank.wecube.plugins.alicloud.service.securityGroup.SecurityGroupService;
import org.apache.commons.lang3.StringUtils;
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

    @PostMapping(path = StringUtils.EMPTY)
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

    @DeleteMapping(path = StringUtils.EMPTY)
    @ResponseBody
    public CoreResponseDto<?> deleteVSwitch(@RequestBody CoreRequestDto<DeleteSecurityGroupRequest> request) {
        try {
            this.securityGroupService.deleteSecurityGroup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return CoreResponseDto.okay();
    }
}
