package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.nat.*;
import com.webank.wecube.plugins.alicloud.service.vpc.nat.NatGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/vpc/nat")
public class NatGatewayController {


    private final NatGatewayService natGatewayService;

    @Autowired
    public NatGatewayController(NatGatewayService natGatewayService) {
        this.natGatewayService = natGatewayService;
    }

    @PostMapping(path = "/create")
    @ResponseBody
    public CoreResponseDto<CoreCreateNatGatewayResponseDto> createNatGateway(@RequestBody CoreRequestDto<CoreCreateNatGatewayRequestDto> request) {
        List<CoreCreateNatGatewayResponseDto> result = this.natGatewayService.createNatGateway(request.getInputs());
        return new CoreResponseDto<CoreCreateNatGatewayResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteNatGatewayResponseDto> releaseEipAddress(@RequestBody CoreRequestDto<CoreDeleteNatGatewayRequestDto> request) {
        List<CoreDeleteNatGatewayResponseDto> result = this.natGatewayService.deleteNatGateway(request.getInputs());
        return new CoreResponseDto<CoreDeleteNatGatewayResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/snat_entry/create")
    @ResponseBody
    public CoreResponseDto<CoreCreateSnatEntryResponseDto> createSnatEntry(@RequestBody CoreRequestDto<CoreCreateSnatEntryRequestDto> request) {
        List<CoreCreateSnatEntryResponseDto> result = this.natGatewayService.createSnatEntry(request.getInputs());
        return new CoreResponseDto<CoreCreateSnatEntryResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/snat_entry/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteSnatEntryResponseDto> deleteSnatEntry(@RequestBody CoreRequestDto<CoreDeleteSnatEntryRequestDto> request) {
        List<CoreDeleteSnatEntryResponseDto> result = this.natGatewayService.deleteSnatEntry(request.getInputs());
        return new CoreResponseDto<CoreDeleteSnatEntryResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/snat_entry/modify/append")
    @ResponseBody
    public CoreResponseDto<CoreModifySnatEntryResponseDto> appendSnatEntry(@RequestBody CoreRequestDto<CoreModifySnatEntryRequestDto> request) {
        List<CoreModifySnatEntryResponseDto> result = this.natGatewayService.appendSnatEntry(request.getInputs());
        return new CoreResponseDto<CoreModifySnatEntryResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/snat_entry/modify/remove")
    @ResponseBody
    public CoreResponseDto<CoreModifySnatEntryResponseDto> pruneSnatEntry(@RequestBody CoreRequestDto<CoreModifySnatEntryRequestDto> request) {
        List<CoreModifySnatEntryResponseDto> result = this.natGatewayService.pruneSnatEntry(request.getInputs());
        return new CoreResponseDto<CoreModifySnatEntryResponseDto>().withErrorCheck(result);
    }
}

