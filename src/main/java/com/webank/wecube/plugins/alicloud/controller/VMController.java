package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.ecs.vm.*;
import com.webank.wecube.plugins.alicloud.service.ecs.vm.VMService;
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
    public CoreResponseDto<CoreCreateVMResponseDto> createVM(@RequestBody CoreRequestDto<CoreCreateVMRequestDto> request) {
        List<CoreCreateVMResponseDto> result = this.vmService.createVM(request.getInputs());
        return new CoreResponseDto<CoreCreateVMResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteVMResponseDto> deleteVM(@RequestBody CoreRequestDto<CoreDeleteVMRequestDto> request) {
        List<CoreDeleteVMResponseDto> result = this.vmService.deleteVM(request.getInputs());
        return new CoreResponseDto<CoreDeleteVMResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/start")
    @ResponseBody
    public CoreResponseDto<CoreStartVMResponseDto> startVM(@RequestBody CoreRequestDto<CoreStartVMRequestDto> request) {
        List<CoreStartVMResponseDto> result = this.vmService.startVM(request.getInputs());
        return new CoreResponseDto<CoreStartVMResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/stop")
    @ResponseBody
    public CoreResponseDto<CoreStopVMResponseDto> stopVM(@RequestBody CoreRequestDto<CoreStopVMRequestDto> request) {
        List<CoreStopVMResponseDto> result = this.vmService.stopVM(request.getInputs());
        return new CoreResponseDto<CoreStopVMResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/security-group/bind")
    @ResponseBody
    public CoreResponseDto<CoreBindSecurityGroupResponseDto> bindSecurityGroup(@RequestBody CoreRequestDto<CoreBindSecurityGroupRequestDto> request) {
        List<CoreBindSecurityGroupResponseDto> result = this.vmService.bindSecurityGroup(request.getInputs());
        return new CoreResponseDto<CoreBindSecurityGroupResponseDto>().withErrorCheck(result);
    }
}
