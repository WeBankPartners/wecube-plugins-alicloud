package com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener;

import com.aliyuncs.slb.model.v20140515.DeleteLoadBalancerListenerResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreDeleteLoadBalancerListenerResponseDto extends DeleteLoadBalancerListenerResponse {
    private String guid;
    private String callbackParameter;

    public CoreDeleteLoadBalancerListenerResponseDto(String guid, String callbackParameter) {
        this.guid = guid;
        this.callbackParameter = callbackParameter;
    }

    public CoreDeleteLoadBalancerListenerResponseDto() {
    }

    public static CoreDeleteLoadBalancerListenerResponseDto fromSdk(DeleteLoadBalancerListenerResponse createInstanceResponse) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(createInstanceResponse, CoreDeleteLoadBalancerListenerResponseDto.class);
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
