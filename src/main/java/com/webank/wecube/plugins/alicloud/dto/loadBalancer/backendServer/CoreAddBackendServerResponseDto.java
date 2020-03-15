package com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer;

import com.aliyuncs.slb.model.v20140515.CreateVServerGroupResponse;

/**
 * @author howechen
 */
public class CoreAddBackendServerResponseDto extends CreateVServerGroupResponse {
    private String guid;
    private String callbackParameter;

    public CoreAddBackendServerResponseDto() {
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
