package com.webank.wecube.plugins.alicloud.controller;

import com.aliyuncs.ecs.model.v20140526.DeleteInstanceRequest;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vm.CoreCreateVMRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vm.CoreCreateVMResponseDto;
import com.webank.wecube.plugins.alicloud.service.vm.VMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
public class VMController {

    private VMService vmService;

    @Autowired
    public VMController(VMService vmService) {
        this.vmService = vmService;
    }

    @PostMapping
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

    @DeleteMapping
    @ResponseBody
    public CoreResponseDto<?> deleteVM(@RequestBody CoreRequestDto<DeleteInstanceRequest> request) {
        try {
            this.vmService.deleteVM(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return CoreResponseDto.okay();
    }
}
