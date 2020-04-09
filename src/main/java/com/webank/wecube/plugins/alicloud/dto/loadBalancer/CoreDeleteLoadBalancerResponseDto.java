package com.webank.wecube.plugins.alicloud.dto.loadBalancer;

import com.aliyuncs.slb.model.v20140515.DeleteLoadBalancerResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreDeleteLoadBalancerResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreDeleteLoadBalancerResponseDto, DeleteLoadBalancerResponse> {

    private String requestId;

    public CoreDeleteLoadBalancerResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
