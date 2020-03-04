package com.webank.wecube.plugins.alicloud.controller;

import com.aliyuncs.ecs.model.v20140526.StartInstanceResponse;
import com.aliyuncs.ecs.model.v20140526.StopInstanceResponse;
import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vm.*;
import com.webank.wecube.plugins.alicloud.service.vm.VMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/vm")
public class VMController {

    private VMService vmService;

    @Autowired
    public VMController(VMService vmService) {
        this.vmService = vmService;
    }

    @PostMapping(path = "/create")
    @ResponseBody
    public CoreResponseDto<?> createVM(@RequestBody CoreRequestDto<CoreCreateVMRequestDto> request) {
        List<CoreCreateVMResponseDto> result;
        try {
            result = this.vmService.createVM(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreCreateVMResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<?> deleteVM(@RequestBody CoreRequestDto<CoreDeleteVMRequestDto> request) {
        try {
            this.vmService.deleteVM(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return CoreResponseDto.okay();
    }

    @PostMapping(path = "/start")
    @ResponseBody
    public CoreResponseDto<?> startVM(@RequestBody CoreRequestDto<CoreStartVMRequestDto> request) {
        List<StartInstanceResponse> result;
        try {
            result = this.vmService.startVM(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<StartInstanceResponse>().okayWithData(result);
    }

    @PostMapping(path = "/stop")
    @ResponseBody
    public CoreResponseDto<?> stopVM(@RequestBody CoreRequestDto<CoreStopVMRequestDto> request) {
        List<StopInstanceResponse> result;
        try {
            result = this.vmService.stopVM(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<StopInstanceResponse>().okayWithData(result);
    }
}
