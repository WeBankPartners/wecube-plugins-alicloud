package com.webank.wecube.plugins.alicloud.dto.loadBalancer;

import com.aliyuncs.slb.model.v20140515.CreateLoadBalancerRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author howechen
 */
public class CoreCreateLoadBalancerRequestDto extends CreateLoadBalancerRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;
    private String loadBalancerId;
    private String loadBalancerProtocol;

    @JsonDeserialize(as = Integer.class)
    @Override
    public void setDuration(Integer duration) {
        super.setDuration(duration);
    }

    @JsonDeserialize(as = Integer.class)
    @Override
    public void setBandwidth(Integer bandwidth) {
        super.setBandwidth(bandwidth);
    }

    @JsonDeserialize(as = Boolean.class)
    @Override
    public void setAutoPay(Boolean autoPay) {
        super.setAutoPay(autoPay);
    }

    @JsonDeserialize(as = Long.class)
    @Override
    public void setResourceOwnerId(Long resourceOwnerId) {
        super.setResourceOwnerId(resourceOwnerId);
    }

    @JsonDeserialize(as = Long.class)
    @Override
    public void setOwnerId(Long ownerId) {
        super.setOwnerId(ownerId);
    }

    public CoreCreateLoadBalancerRequestDto(String identityParams, String cloudParams, String loadBalancerId, String guid, String callbackParameter, String loadBalancerProtocol) {
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
        this.loadBalancerId = loadBalancerId;
        this.guid = guid;
        this.callbackParameter = callbackParameter;
        this.loadBalancerProtocol = loadBalancerProtocol;
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

    public String getLoadBalancerProtocol() {
        return loadBalancerProtocol;
    }

    public void setLoadBalancerProtocol(String loadBalancerProtocol) {
        this.loadBalancerProtocol = loadBalancerProtocol;
    }
}
