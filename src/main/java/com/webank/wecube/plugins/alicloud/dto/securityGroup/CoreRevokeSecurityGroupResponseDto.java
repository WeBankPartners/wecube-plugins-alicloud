package com.webank.wecube.plugins.alicloud.dto.securityGroup;

import com.aliyuncs.ecs.model.v20140526.RevokeSecurityGroupResponse;

/**
 * @author howechen
 */
public class CoreRevokeSecurityGroupResponseDto extends RevokeSecurityGroupResponse {
    private String guid;
    private String callbackParameter;

    public CoreRevokeSecurityGroupResponseDto() {
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
