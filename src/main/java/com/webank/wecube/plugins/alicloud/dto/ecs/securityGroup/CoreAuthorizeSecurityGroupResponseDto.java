package com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup;

import com.aliyuncs.ecs.model.v20140526.AuthorizeSecurityGroupResponse;

/**
 * @author howechen
 */
public class CoreAuthorizeSecurityGroupResponseDto extends AuthorizeSecurityGroupResponse {
    private String guid;
    private String callbackParameter;

    public CoreAuthorizeSecurityGroupResponseDto() {
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
