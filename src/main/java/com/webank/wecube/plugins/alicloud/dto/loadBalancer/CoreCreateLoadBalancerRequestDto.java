package com.webank.wecube.plugins.alicloud.dto.loadBalancer;

import com.aliyuncs.slb.model.v20140515.CreateLoadBalancerRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CoreCreateLoadBalancerRequestDto extends CreateLoadBalancerRequest {
    private String identityParams;
    private String cloudParams;
    private String loadBalancerId;

    public CoreCreateLoadBalancerRequestDto(String identityParams, String cloudParams, String loadBalancerId) {
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
        this.loadBalancerId = loadBalancerId;
    }

    public CoreCreateLoadBalancerRequestDto() {
    }

    public static CreateLoadBalancerRequest toSdk(CoreCreateLoadBalancerRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, CreateLoadBalancerRequest.class);
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

    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    public void setLoadBalancerId(String loadBalancerId) {
        this.loadBalancerId = loadBalancerId;
    }
}
