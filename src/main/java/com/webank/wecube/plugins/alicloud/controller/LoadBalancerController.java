package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
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
    public CoreResponseDto<CoreCreateLoadBalancerResponseDto> createLoadBalancer(@RequestBody CoreRequestDto<CoreCreateLoadBalancerRequestDto> request) {
        List<CoreCreateLoadBalancerResponseDto> result = this.loadBalancerService.createLoadBalancer(request.getInputs());
        return new CoreResponseDto<CoreCreateLoadBalancerResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteLoadBalancerResponseDto> deleteLoadBalancer(@RequestBody CoreRequestDto<CoreDeleteLoadBalancerRequestDto> request) {
        List<CoreDeleteLoadBalancerResponseDto> result = this.loadBalancerService.deleteLoadBalancer(request.getInputs());
        return new CoreResponseDto<CoreDeleteLoadBalancerResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/backend_server/add")
    @ResponseBody
    public CoreResponseDto<CoreAddBackendServerResponseDto> addBackendServer(@RequestBody CoreRequestDto<CoreAddBackendServerRequestDto> request) {
        List<CoreAddBackendServerResponseDto> result = this.loadBalancerService.addBackendServer(request.getInputs());
        return new CoreResponseDto<CoreAddBackendServerResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/backend_server/remove")
    @ResponseBody
    public CoreResponseDto<CoreRemoveBackendServerResponseDto> removeBackendServer(@RequestBody CoreRequestDto<CoreRemoveBackendServerRequestDto> request) {
        List<CoreRemoveBackendServerResponseDto> result = this.loadBalancerService.removeBackendServer(request.getInputs());
        return new CoreResponseDto<CoreRemoveBackendServerResponseDto>().withErrorCheck(result);
    }
}
