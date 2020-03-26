package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.nat.CoreCreateNatGatewayRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.nat.CoreCreateNatGatewayResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.nat.CoreDeleteNatGatewayRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.nat.CoreDeleteNatGatewayResponseDto;
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


    private NatGatewayService natGatewayService;

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
}

