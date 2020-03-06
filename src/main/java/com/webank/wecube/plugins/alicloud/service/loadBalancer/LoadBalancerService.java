package com.webank.wecube.plugins.alicloud.service.loadBalancer;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreCreateLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.CoreDeleteLoadBalancerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener.CoreCreateLoadBalancerListenerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener.CoreCreateLoadBalancerListenerResponseDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener.CoreDeleteLoadBalancerListenerRequestDto;
import com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener.CoreDeleteLoadBalancerListenerResponseDto;

import java.util.List;

/**
 * @author howechen
 */
public interface LoadBalancerService {

    List<CoreCreateLoadBalancerResponseDto> createLoadBalancer(List<CoreCreateLoadBalancerRequestDto> coreCreateLoadBalancerRequestDtoList) throws PluginException;

    List<CoreDeleteLoadBalancerResponseDto> deleteLoadBalancer(List<CoreDeleteLoadBalancerRequestDto> coreDeleteLoadBalancerRequestDtoList) throws PluginException;

    List<CoreCreateLoadBalancerListenerResponseDto> createListener(List<CoreCreateLoadBalancerListenerRequestDto> coreCreateLoadBalancerListenerRequestDtoList) throws PluginException;

    List<CoreDeleteLoadBalancerListenerResponseDto> deleteListener(List<CoreDeleteLoadBalancerListenerRequestDto> coreDeleteLoadBalancerListenerRequestDtoList) throws PluginException;
}
