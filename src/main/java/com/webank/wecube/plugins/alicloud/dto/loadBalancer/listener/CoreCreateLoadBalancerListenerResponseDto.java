package com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener;

import com.aliyuncs.AcsResponse;
import com.aliyuncs.slb.model.v20140515.CreateLoadBalancerTCPListenerResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateLoadBalancerListenerResponseDto extends CreateLoadBalancerTCPListenerResponse {
    private String guid;
    private String callbackParameter;

    public CoreCreateLoadBalancerListenerResponseDto(String guid, String callbackParameter) {
        this.guid = guid;
        this.callbackParameter = callbackParameter;
    }


    public CoreCreateLoadBalancerListenerResponseDto() {
    }

    public static CoreCreateLoadBalancerListenerResponseDto fromSdk(AcsResponse createInstanceResponse) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(createInstanceResponse, CoreCreateLoadBalancerListenerResponseDto.class);
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCallbackParameter() {
        return callbackParameter;
    }

    public void setCallbackParameter(String callbackParameter) {
        this.callbackParameter = callbackParameter;
    }
}
