package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDtoBkp;
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
    public CoreResponseDtoBkp<CoreCreateCenResponseDto> createCen(@RequestBody CoreRequestDtoBkp<CoreCreateCenRequestDto> request) {
        List<CoreCreateCenResponseDto> result = this.cenService.createCen(request.getInputs());
        return new CoreResponseDtoBkp<CoreCreateCenResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDtoBkp<CoreDeleteCenResponseDto> deleteCen(@RequestBody CoreRequestDtoBkp<CoreDeleteCenRequestDto> request) {
        List<CoreDeleteCenResponseDto> result = this.cenService.deleteCen(request.getInputs());
        return new CoreResponseDtoBkp<CoreDeleteCenResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/attach")
    @ResponseBody
    public CoreResponseDtoBkp<CoreAttachCenChildResponseDto> attachCenChild(@RequestBody CoreRequestDtoBkp<CoreAttachCenChildRequestDto> request) {
        List<CoreAttachCenChildResponseDto> result = this.cenService.attachCenChild(request.getInputs());
        return new CoreResponseDtoBkp<CoreAttachCenChildResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/detach")
    @ResponseBody
    public CoreResponseDtoBkp<CoreDetachCenChildResponseDto> detachCenChild(@RequestBody CoreRequestDtoBkp<CoreDetachCenChildRequestDto> request) {
        List<CoreDetachCenChildResponseDto> result = this.cenService.detachCenChild(request.getInputs());
        return new CoreResponseDtoBkp<CoreDetachCenChildResponseDto>().withErrorCheck(result);
    }
}
