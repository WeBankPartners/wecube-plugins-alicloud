package com.webank.wecube.plugins.alicloud.dto.redis;

import com.aliyuncs.r_kvstore.model.v20150101.CreateInstanceResponse;

/**
 * @author howechen
 */
public class CoreCreateInstanceResponseDto extends CreateInstanceResponse {
    private String guid;
    private String callbackParameter;

    public CoreCreateInstanceResponseDto() {
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
