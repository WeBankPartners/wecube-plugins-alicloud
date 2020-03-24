package com.webank.wecube.plugins.alicloud.dto.ecs.vm;

import com.aliyuncs.ecs.model.v20140526.DeleteInstanceResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreDeleteVMResponseDto extends DeleteInstanceResponse {
    private String guid;
    private String callbackParameter;


    public CoreDeleteVMResponseDto() {
    }

    public static CoreDeleteVMResponseDto fromSdk(DeleteInstanceResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, CoreDeleteVMResponseDto.class);
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
