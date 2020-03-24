package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.ecs.vm.*;
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
    public CoreResponseDtoBkp<?> createVM(@RequestBody CoreRequestDtoBkp<CoreCreateVMRequestDto> request) {
        List<CoreCreateVMResponseDto> result;
        try {
            result = this.vmService.createVM(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreCreateVMResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDtoBkp<?> deleteVM(@RequestBody CoreRequestDtoBkp<CoreDeleteVMRequestDto> request) {
        try {
            this.vmService.deleteVM(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return CoreResponseDtoBkp.okay();
    }

    @PostMapping(path = "/start")
    @ResponseBody
    public CoreResponseDtoBkp<?> startVM(@RequestBody CoreRequestDtoBkp<CoreStartVMRequestDto> request) {
        List<CoreStartVMResponseDto> result;
        try {
            result = this.vmService.startVM(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreStartVMResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/stop")
    @ResponseBody
    public CoreResponseDtoBkp<?> stopVM(@RequestBody CoreRequestDtoBkp<CoreStopVMRequestDto> request) {
        List<CoreStopVMResponseDto> result;
        try {
            result = this.vmService.stopVM(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreStopVMResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/security-group/bind")
    @ResponseBody
    public CoreResponseDtoBkp<?> bindSecurityGroup(@RequestBody CoreRequestDtoBkp<CoreBindSecurityGroupRequestDto> request) {
        List<CoreBindSecurityGroupResponseDto> result;
        try {
            result = this.vmService.bindSecurityGroup(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreBindSecurityGroupResponseDto>().okayWithData(result);
    }
}
