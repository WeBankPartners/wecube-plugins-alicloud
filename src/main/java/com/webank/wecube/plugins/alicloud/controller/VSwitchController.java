package com.webank.wecube.plugins.alicloud.controller;

import com.aliyuncs.vpc.model.v20160428.DeleteVSwitchRequest;
import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.service.vswitch.VSwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX)
public class VSwitchController {

    private VSwitchService vSwitchService;

    @Autowired
    public VSwitchController(VSwitchService vSwitchService) {
        this.vSwitchService = vSwitchService;
    }

    @PostMapping(path = "vswitch")
    @ResponseBody
    public CoreResponseDto<?> createVSwitch(@RequestBody CoreRequestDto<CoreCreateVSwitchRequestDto> coreCreateVSwitchDtoCoreResponseDto) {
        List<CoreCreateVSwitchResponseDto> result;
        try {
            result = this.vSwitchService.createVSwitch(coreCreateVSwitchDtoCoreResponseDto.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreCreateVSwitchResponseDto>().okayWithData(result);
    }

    @DeleteMapping(path = "vswitch")
    @ResponseBody
    public CoreResponseDto<?> deleteVSwitch(@RequestBody CoreRequestDto<DeleteVSwitchRequest> coreDeleteVSwitchRequestDto) {
        try {
            this.vSwitchService.deleteVSwitch(coreDeleteVSwitchRequestDto.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return CoreResponseDto.okay();
    }
}
