package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.cbn.cen.*;
import com.webank.wecube.plugins.alicloud.service.cbn.cen.CenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/cen")
public class CenController {

    private CenService cenService;

    @Autowired
    public CenController(CenService cenService) {
        this.cenService = cenService;
    }

    @PostMapping(path = "/create")
    @ResponseBody
    public CoreResponseDto<CoreCreateCenResponseDto> createCen(@RequestBody CoreRequestDto<CoreCreateCenRequestDto> request) {
        List<CoreCreateCenResponseDto> result = this.cenService.createCen(request.getInputs());
        return new CoreResponseDto<CoreCreateCenResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteCenResponseDto> deleteCen(@RequestBody CoreRequestDto<CoreDeleteCenRequestDto> request) {
        List<CoreDeleteCenResponseDto> result = this.cenService.deleteCen(request.getInputs());
        return new CoreResponseDto<CoreDeleteCenResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/attach")
    @ResponseBody
    public CoreResponseDto<CoreAttachCenChildResponseDto> attachCenChild(@RequestBody CoreRequestDto<CoreAttachCenChildRequestDto> request) {
        List<CoreAttachCenChildResponseDto> result = this.cenService.attachCenChild(request.getInputs());
        return new CoreResponseDto<CoreAttachCenChildResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/detach")
    @ResponseBody
    public CoreResponseDto<CoreDetachCenChildResponseDto> detachCenChild(@RequestBody CoreRequestDto<CoreDetachCenChildRequestDto> request) {
        List<CoreDetachCenChildResponseDto> result = this.cenService.detachCenChild(request.getInputs());
        return new CoreResponseDto<CoreDetachCenChildResponseDto>().withErrorCheck(result);
    }
}
