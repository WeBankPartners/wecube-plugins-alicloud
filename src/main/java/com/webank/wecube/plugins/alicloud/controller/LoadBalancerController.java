package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.service.loadBalancer.LoadBalancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
        try {
            this.loadBalancerService.deleteLoadBalancer(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return CoreResponseDto.okay();
    }
}
