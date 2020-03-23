package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreDeleteVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.service.vswitch.VSwitchService;
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
    public CoreResponseDtoBkp<?> createVSwitch(@RequestBody CoreRequestDtoBkp<CoreCreateVSwitchRequestDto> coreCreateVSwitchDtoCoreResponseDto) {
        List<CoreCreateVSwitchResponseDto> result;
        try {
            result = this.vSwitchService.createVSwitch(coreCreateVSwitchDtoCoreResponseDto.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreCreateVSwitchResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "delete")
    @ResponseBody
    public CoreResponseDtoBkp<?> deleteVSwitch(@RequestBody CoreRequestDtoBkp<CoreDeleteVSwitchRequestDto> coreDeleteVSwitchRequestDto) {
        try {
            this.vSwitchService.deleteVSwitch(coreDeleteVSwitchRequestDto.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return CoreResponseDtoBkp.okay();
    }
}
