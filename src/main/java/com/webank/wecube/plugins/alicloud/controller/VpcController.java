package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreDeleteVpcRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreDeleteVpcResponseDto;
import com.webank.wecube.plugins.alicloud.service.vpc.VpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/vpc")
public class VpcController {

    @Autowired
    private VpcService vpcService;

    @PostMapping(path = "/create")
    @ResponseBody
    public CoreResponseDto<CoreCreateVpcResponseDto> createVpc(@RequestBody CoreRequestDto<CoreCreateVpcRequestDto> coreRequestDto) {
        List<CoreCreateVpcResponseDto> result = this.vpcService.createVpc(coreRequestDto.getInputs());
        return new CoreResponseDto<CoreCreateVpcResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteVpcResponseDto> deleteVpc(@RequestBody CoreRequestDto<CoreDeleteVpcRequestDto> coreRequestDto) {
        List<CoreDeleteVpcResponseDto> result = this.vpcService.deleteVpc(coreRequestDto.getInputs());
        return new CoreResponseDto<CoreDeleteVpcResponseDto>().withErrorCheck(result);
    }


}
