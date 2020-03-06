package com.webank.wecube.plugins.alicloud.dto.loadBalancer;

import com.aliyuncs.slb.model.v20140515.CreateLoadBalancerResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateLoadBalancerResponseDto extends CreateLoadBalancerResponse {

    private String guid;
    private String callbackParameter;

    public CoreCreateLoadBalancerResponseDto() {
    }

    public CoreCreateLoadBalancerResponseDto(String requestId, String instanceId) {
        super();
        this.setRequestId(requestId);
    }

    public static CoreCreateLoadBalancerResponseDto fromSdk(CreateLoadBalancerResponse createInstanceResponse) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(createInstanceResponse, CoreCreateLoadBalancerResponseDto.class);
    }

    public static CoreCreateLoadBalancerResponseDto fromSdk(DescribeLoadBalancersResponse.LoadBalancer loadBalancer) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(loadBalancer, CoreCreateLoadBalancerResponseDto.class);
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
