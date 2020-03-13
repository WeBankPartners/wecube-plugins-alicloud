package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer.CoreAddBackendServerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer.CoreAddBackendServerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer.CoreRemoveBackendServerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer.CoreRemoveBackendServerResponseDto;
import com.webank.wecube.plugins.alicloud.service.loadBalancer.LoadBalancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/load_balancer")
public class LoadBalancerController {
    @Autowired
    private LoadBalancerService loadBalancerService;

    @PostMapping(path = "/create")
    @ResponseBody
    public CoreResponseDto<?> createLoadBalancer(@RequestBody CoreRequestDto<CoreCreateLoadBalancerRequestDto> request) {
        List<CoreCreateLoadBalancerResponseDto> result;
        try {
            result = this.loadBalancerService.createLoadBalancer(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreCreateLoadBalancerResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<?> deleteLoadBalancer(@RequestBody CoreRequestDto<CoreDeleteLoadBalancerRequestDto> request) {
        List<CoreDeleteLoadBalancerResponseDto> result;
        try {
            result = this.loadBalancerService.deleteLoadBalancer(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreDeleteLoadBalancerResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/backend_server/add")
    @ResponseBody
    public CoreResponseDto<?> createListener(@RequestBody CoreRequestDto<CoreAddBackendServerRequestDto> request) {
        List<CoreAddBackendServerResponseDto> result;
        try {
            result = this.loadBalancerService.addBackendServer(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreAddBackendServerResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/backend_server/remove")
    @ResponseBody
    public CoreResponseDto<?> deleteListener(@RequestBody CoreRequestDto<CoreRemoveBackendServerRequestDto> request) {
        List<CoreRemoveBackendServerResponseDto> result;
        try {
            result = this.loadBalancerService.removeBackendServer(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreRemoveBackendServerResponseDto>().okayWithData(result);
    }
}
