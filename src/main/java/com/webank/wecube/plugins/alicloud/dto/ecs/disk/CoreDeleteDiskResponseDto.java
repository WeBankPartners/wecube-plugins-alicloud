package com.webank.wecube.plugins.alicloud.dto.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.DeleteDiskResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreDeleteDiskResponseDto extends DeleteDiskResponse {
    private String guid;
    private String callbackParameter;

    public CoreDeleteDiskResponseDto() {
    }

    public static CoreDeleteDiskResponseDto fromSdk(DeleteDiskResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, CoreDeleteDiskResponseDto.class);
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
