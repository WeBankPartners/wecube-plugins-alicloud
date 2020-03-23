package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDtoBkp;
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
    public CoreResponseDtoBkp<CoreCreateNatGatewayResponseDto> createNatGateway(@RequestBody CoreRequestDtoBkp<CoreCreateNatGatewayRequestDto> request) {
        List<CoreCreateNatGatewayResponseDto> result = this.natGatewayService.createNatGateway(request.getInputs());
        return new CoreResponseDtoBkp<CoreCreateNatGatewayResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDtoBkp<CoreDeleteNatGatewayResponseDto> releaseEipAddress(@RequestBody CoreRequestDtoBkp<CoreDeleteNatGatewayRequestDto> request) {
        List<CoreDeleteNatGatewayResponseDto> result = this.natGatewayService.deleteNatGateway(request.getInputs());
        return new CoreResponseDtoBkp<CoreDeleteNatGatewayResponseDto>().withErrorCheck(result);
    }
}

