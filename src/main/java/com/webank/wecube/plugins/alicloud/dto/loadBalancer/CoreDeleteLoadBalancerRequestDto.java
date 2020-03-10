package com.webank.wecube.plugins.alicloud.dto.loadBalancer;

import com.aliyuncs.slb.model.v20140515.DeleteLoadBalancerRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreDeleteLoadBalancerRequestDto extends DeleteLoadBalancerRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;

    public static DeleteLoadBalancerRequest toSdk(CoreDeleteLoadBalancerRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, DeleteLoadBalancerRequest.class);
    }

    public CoreDeleteLoadBalancerRequestDto(String identityParams, String cloudParams, String guid, String callbackParameter) {
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
        this.guid = guid;
        this.callbackParameter = callbackParameter;
    }

    public CoreDeleteLoadBalancerRequestDto() {
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
}
