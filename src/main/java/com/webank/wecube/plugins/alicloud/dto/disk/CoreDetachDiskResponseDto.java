package com.webank.wecube.plugins.alicloud.dto.disk;

import com.aliyuncs.ecs.model.v20140526.DetachDiskResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreDetachDiskResponseDto extends DetachDiskResponse {

    private String guid;
    private String callbackParameter;

    public CoreDetachDiskResponseDto() {
    }

    public static CoreDetachDiskResponseDto fromSdk(DetachDiskResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, CoreDetachDiskResponseDto.class);
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
