package com.webank.wecube.plugins.alicloud.dto.vm;

import com.aliyuncs.ecs.model.v20140526.ModifyInstanceAttributeRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreBindSecurityGroupRequestDto extends ModifyInstanceAttributeRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;
    private String securityGroupId;

    public CoreBindSecurityGroupRequestDto() {
    }

    public static ModifyInstanceAttributeRequest toSdk(CoreBindSecurityGroupRequestDto coreBindSecurityGroupRequestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(coreBindSecurityGroupRequestDto, ModifyInstanceAttributeRequest.class);
    }

    public String getIdentityParams() {
        return identityParams;
    }

    public void setIdentityParams(String identityParams) {
        this.identityParams = identityParams;
    }

    public String getCloudParams() {
        return cloudParams;
    }

    public void setCloudParams(String cloudParams) {
        this.cloudParams = cloudParams;
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

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }
}
