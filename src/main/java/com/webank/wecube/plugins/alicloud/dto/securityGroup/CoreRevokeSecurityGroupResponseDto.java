package com.webank.wecube.plugins.alicloud.dto.securityGroup;

import com.aliyuncs.ecs.model.v20140526.RevokeSecurityGroupEgressResponse;
import com.aliyuncs.ecs.model.v20140526.RevokeSecurityGroupResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreRevokeSecurityGroupResponseDto extends RevokeSecurityGroupResponse {
    private String guid;
    private String callbackParameter;

    public CoreRevokeSecurityGroupResponseDto() {
    }

    public static CoreRevokeSecurityGroupResponseDto fromSdk(RevokeSecurityGroupResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, CoreRevokeSecurityGroupResponseDto.class);
    }

    public static CoreRevokeSecurityGroupResponseDto fromEgressSdk(RevokeSecurityGroupEgressResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, CoreRevokeSecurityGroupResponseDto.class);
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
