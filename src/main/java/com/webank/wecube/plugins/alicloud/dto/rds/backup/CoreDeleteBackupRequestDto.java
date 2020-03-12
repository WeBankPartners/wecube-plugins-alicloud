package com.webank.wecube.plugins.alicloud.dto.rds.backup;

import com.aliyuncs.rds.model.v20140815.DeleteBackupRequest;

/**
 * @author howechen
 */
public class CoreDeleteBackupRequestDto extends DeleteBackupRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;

    public CoreDeleteBackupRequestDto() {
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
