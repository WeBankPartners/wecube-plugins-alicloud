package com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener;

import com.aliyuncs.slb.model.v20140515.CreateLoadBalancerHTTPListenerRequest;
import com.aliyuncs.slb.model.v20140515.CreateLoadBalancerHTTPSListenerRequest;
import com.aliyuncs.slb.model.v20140515.CreateLoadBalancerTCPListenerRequest;
import com.aliyuncs.slb.model.v20140515.CreateLoadBalancerUDPListenerRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateLoadBalancerListenerRequestDto extends CreateLoadBalancerTCPListenerRequest {

    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;
    private String listenerProtocol;

    public CoreCreateLoadBalancerListenerRequestDto() {
    }

    public static CreateLoadBalancerTCPListenerRequest toSdkTCPRequest(CoreCreateLoadBalancerListenerRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, CreateLoadBalancerTCPListenerRequest.class);
    }

    public static CreateLoadBalancerHTTPListenerRequest toSdkHTTPRequest(CoreCreateLoadBalancerListenerRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, CreateLoadBalancerHTTPListenerRequest.class);
    }

    public static CreateLoadBalancerHTTPSListenerRequest toSdkHTTPSRequest(CoreCreateLoadBalancerListenerRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, CreateLoadBalancerHTTPSListenerRequest.class);
    }

    public static CreateLoadBalancerUDPListenerRequest toSdkUDPRequest(CoreCreateLoadBalancerListenerRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, CreateLoadBalancerUDPListenerRequest.class);
    }

    public String getIdentityParams() {
        return identityParams;
    }

    public void setIdentityParams(String identityParams) {
        this.identityParams = identityParams;
    }

    public String getCloudParams() {
        return cloudParams;
    }

    public void setCloudParams(String cloudParams) {
        this.cloudParams = cloudParams;
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

    public String getListenerProtocol() {
        return listenerProtocol;
    }

    public void setListenerProtocol(String listenerProtocol) {
        this.listenerProtocol = listenerProtocol;
    }
}
