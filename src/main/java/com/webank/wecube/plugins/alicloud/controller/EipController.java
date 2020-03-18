package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.eip.*;
import com.webank.wecube.plugins.alicloud.service.vpc.eip.EipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/vpc/eip")
public class EipController {

    private EipService eipService;

    @Autowired
    public EipController(EipService eipService) {
        this.eipService = eipService;
    }

    @PostMapping(path = "/allocate")
    @ResponseBody
    public CoreResponseDto<CoreAllocateEipResponseDto> allocateEipAddress(@RequestBody CoreRequestDto<CoreAllocateEipRequestDto> request) {
        List<CoreAllocateEipResponseDto> result = this.eipService.allocateEipAddress(request.getInputs());
        return new CoreResponseDto<CoreAllocateEipResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/release")
    @ResponseBody
    public CoreResponseDto<CoreReleaseEipResponseDto> releaseEipAddress(@RequestBody CoreRequestDto<CoreReleaseEipRequestDto> request) {
        List<CoreReleaseEipResponseDto> result = this.eipService.releaseEipAddress(request.getInputs());
        return new CoreResponseDto<CoreReleaseEipResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/associate")
    @ResponseBody
    public CoreResponseDto<CoreAssociateEipResponseDto> associateEipAddress(@RequestBody CoreRequestDto<CoreAssociateEipRequestDto> request) {
        List<CoreAssociateEipResponseDto> result = this.eipService.associateEipAddress(request.getInputs());
        return new CoreResponseDto<CoreAssociateEipResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/un-associate")
    @ResponseBody
    public CoreResponseDto<CoreUnAssociateEipResponseDto> unAssociateEipAddress(@RequestBody CoreRequestDto<CoreUnAssociateEipRequestDto> request) {
        List<CoreUnAssociateEipResponseDto> result = this.eipService.unAssociateEipAddress(request.getInputs());
        return new CoreResponseDto<CoreUnAssociateEipResponseDto>().withErrorCheck(result);
    }
}
