package com.webank.wecube.plugins.alicloud.dto.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.CreateDiskResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateDiskResponseDto extends CreateDiskResponse {
    private String guid;
    private String callbackParameter;

    public CoreCreateDiskResponseDto() {
    }

    public static CoreCreateDiskResponseDto fromSdk(DescribeDisksResponse.Disk diskResponse) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(diskResponse, CoreCreateDiskResponseDto.class);
    }

    public static CoreCreateDiskResponseDto fromSdk(CreateDiskResponse diskResponse) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(diskResponse, CoreCreateDiskResponseDto.class);
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
