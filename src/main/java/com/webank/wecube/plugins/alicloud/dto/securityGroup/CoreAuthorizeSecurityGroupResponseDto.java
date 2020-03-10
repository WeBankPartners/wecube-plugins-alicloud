package com.webank.wecube.plugins.alicloud.dto.securityGroup;

import com.aliyuncs.ecs.model.v20140526.AuthorizeSecurityGroupEgressResponse;
import com.aliyuncs.ecs.model.v20140526.AuthorizeSecurityGroupResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreAuthorizeSecurityGroupResponseDto extends AuthorizeSecurityGroupResponse {
    private String guid;
    private String callbackParameter;

    public CoreAuthorizeSecurityGroupResponseDto() {
    }

    public static CoreAuthorizeSecurityGroupResponseDto fromSdk(AuthorizeSecurityGroupResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, CoreAuthorizeSecurityGroupResponseDto.class);
    }

    public static CoreAuthorizeSecurityGroupResponseDto fromEgressSdk(AuthorizeSecurityGroupEgressResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, CoreAuthorizeSecurityGroupResponseDto.class);
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
