package com.webank.wecube.plugins.alicloud.dto.loadBalancer.listener;

import com.aliyuncs.slb.model.v20140515.DeleteLoadBalancerListenerRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreDeleteLoadBalancerListenerRequestDto extends DeleteLoadBalancerListenerRequest {

    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;
    private String listenerId;

    public CoreDeleteLoadBalancerListenerRequestDto() {
    }

    public static DeleteLoadBalancerListenerRequest toSdk(CoreDeleteLoadBalancerListenerRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, DeleteLoadBalancerListenerRequest.class);
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

    public String getListenerId() {
        return listenerId;
    }

    public void setListenerId(String listenerId) {
        this.listenerId = listenerId;
    }
}
