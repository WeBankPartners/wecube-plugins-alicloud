package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener.CoreCreateLoadBalancerListenerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener.CoreCreateLoadBalancerListenerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener.CoreDeleteLoadBalancerListenerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener.CoreDeleteLoadBalancerListenerResponseDto;
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

    @PostMapping(path = "/listener/create")
    @ResponseBody
    public CoreResponseDto<?> createListener(@RequestBody CoreRequestDto<CoreCreateLoadBalancerListenerRequestDto> request) {
        List<CoreCreateLoadBalancerListenerResponseDto> result;
        try {
            result = this.loadBalancerService.createListener(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreCreateLoadBalancerListenerResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/listener/delete")
    @ResponseBody
    public CoreResponseDto<?> deleteListener(@RequestBody CoreRequestDto<CoreDeleteLoadBalancerListenerRequestDto> request) {
        List<CoreDeleteLoadBalancerListenerResponseDto> result;
        try {
            result = this.loadBalancerService.deleteListener(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreDeleteLoadBalancerListenerResponseDto>().okayWithData(result);
    }
}
