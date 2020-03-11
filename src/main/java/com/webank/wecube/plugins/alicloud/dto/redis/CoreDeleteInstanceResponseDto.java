package com.webank.wecube.plugins.alicloud.dto.redis;

import com.aliyuncs.r_kvstore.model.v20150101.DeleteInstanceResponse;
import com.webank.wecube.plugins.alicloud.support.PluginSdkBridge;

/**
 * @author howechen
 */
public class CoreDeleteInstanceResponseDto extends DeleteInstanceResponse implements PluginSdkBridge {
    private String guid;
    private String callbackParameter;

    public CoreDeleteInstanceResponseDto() {
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
