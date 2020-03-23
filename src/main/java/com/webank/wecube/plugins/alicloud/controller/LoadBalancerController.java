package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDtoBkp;
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
    public CoreResponseDtoBkp<?> createLoadBalancer(@RequestBody CoreRequestDtoBkp<CoreCreateLoadBalancerRequestDto> request) {
        List<CoreCreateLoadBalancerResponseDto> result;
        try {
            result = this.loadBalancerService.createLoadBalancer(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreCreateLoadBalancerResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDtoBkp<?> deleteLoadBalancer(@RequestBody CoreRequestDtoBkp<CoreDeleteLoadBalancerRequestDto> request) {
        List<CoreDeleteLoadBalancerResponseDto> result;
        try {
            result = this.loadBalancerService.deleteLoadBalancer(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreDeleteLoadBalancerResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/backend_server/add")
    @ResponseBody
    public CoreResponseDtoBkp<?> createListener(@RequestBody CoreRequestDtoBkp<CoreAddBackendServerRequestDto> request) {
        List<CoreAddBackendServerResponseDto> result;
        try {
            result = this.loadBalancerService.addBackendServer(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreAddBackendServerResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/backend_server/remove")
    @ResponseBody
    public CoreResponseDtoBkp<?> deleteListener(@RequestBody CoreRequestDtoBkp<CoreRemoveBackendServerRequestDto> request) {
        List<CoreRemoveBackendServerResponseDto> result;
        try {
            result = this.loadBalancerService.removeBackendServer(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreRemoveBackendServerResponseDto>().okayWithData(result);
    }
}
