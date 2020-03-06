package com.webank.wecube.plugins.alicloud.dto.vm;

import com.aliyuncs.ecs.model.v20140526.StopInstanceRequest;

/**
 * @author howechen
 */
public class CoreStopVMRequestDto extends StopInstanceRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;

    public CoreStopVMRequestDto(String identityParams, String cloudParams) {
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
    }

    public CoreStopVMRequestDto() {
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
}
