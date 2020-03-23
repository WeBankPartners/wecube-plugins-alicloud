package com.webank.wecube.plugins.alicloud.dto.vm;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @author howechen
 */
public class CoreCreateVMResponseDto extends CreateInstanceResponse {
    private String guid;
    private String callbackParameter;

    @JsonSerialize(using = ToStringSerializer.class)
    @Override
    public void setTradePrice(Float tradePrice) {
        super.setTradePrice(tradePrice);
    }

    public CoreCreateVMResponseDto() {
    }

    public CoreCreateVMResponseDto(String requestId, String instanceId) {
        this.setRequestId(requestId);
        this.setInstanceId(instanceId);
    }

    public static CoreCreateVMResponseDto fromSdk(CreateInstanceResponse createInstanceResponse) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(createInstanceResponse, CoreCreateVMResponseDto.class);
    }

    public static CoreCreateVMResponseDto fromSdk(DescribeInstancesResponse.Instance instance) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(instance, CoreCreateVMResponseDto.class);
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
