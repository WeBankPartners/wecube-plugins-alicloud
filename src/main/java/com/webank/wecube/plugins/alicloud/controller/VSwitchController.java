package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreCreateVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreCreateVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreDeleteVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreDeleteVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.service.vpc.vswitch.VSwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/vswitch")
public class VSwitchController {

    private VSwitchService vSwitchService;

    @Autowired
    public VSwitchController(VSwitchService vSwitchService) {
        this.vSwitchService = vSwitchService;
    }

    @PostMapping(path = "/create")
    @ResponseBody
    public CoreResponseDto<CoreCreateVSwitchResponseDto> createVSwitch(@RequestBody CoreRequestDto<CoreCreateVSwitchRequestDto> coreCreateVSwitchDtoCoreResponseDto) {
        List<CoreCreateVSwitchResponseDto> result = this.vSwitchService.createVSwitch(coreCreateVSwitchDtoCoreResponseDto.getInputs());
        return new CoreResponseDto<CoreCreateVSwitchResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteVSwitchResponseDto> deleteVSwitch(@RequestBody CoreRequestDtoBkp<CoreDeleteVSwitchRequestDto> coreDeleteVSwitchRequestDto) {
        List<CoreDeleteVSwitchResponseDto> result = this.vSwitchService.deleteVSwitch(coreDeleteVSwitchRequestDto.getInputs());
        return new CoreResponseDto<CoreDeleteVSwitchResponseDto>().withErrorCheck(result);
    }
}
