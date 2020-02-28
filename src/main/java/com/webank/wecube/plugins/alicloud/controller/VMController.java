package com.webank.wecube.plugins.alicloud.controller;

import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vm.CoreCreateVMRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vm.CoreCreateVMResponseDto;
import com.webank.wecube.plugins.alicloud.service.vm.VMService;
import org.apache.commons.lang3.StringUtils;
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

    @PostMapping(path = StringUtils.EMPTY)
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

    @DeleteMapping(path = StringUtils.EMPTY)
    @ResponseBody
    public CoreResponseDto<?> deleteVM(@RequestBody CoreRequestDto<DeleteInstanceRequest> request) {
        try {
            this.vmService.deleteVM(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return CoreResponseDto.okay();
    }

    @PostMapping(path = "/start")
    @ResponseBody
    public CoreResponseDto<?> startVM(@RequestBody CoreRequestDto<StartInstanceRequest> request) {
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
    public CoreResponseDto<?> stopVM(@RequestBody CoreRequestDto<StopInstanceRequest> request) {
        List<StopInstanceResponse> result;
        try {
            result = this.vmService.stopVM(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<StopInstanceResponse>().okayWithData(result);
    }
}
