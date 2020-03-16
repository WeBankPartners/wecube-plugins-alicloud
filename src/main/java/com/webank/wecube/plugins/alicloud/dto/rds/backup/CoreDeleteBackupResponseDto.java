package com.webank.wecube.plugins.alicloud.dto.rds.backup;

import com.aliyuncs.rds.model.v20140815.DeleteBackupResponse;

/**
 * @author howechen
 */
public class CoreDeleteBackupResponseDto extends DeleteBackupResponse {
    private String errorCode;
    private String errorMessage;
    private String guid;
    private String callbackParameter;

    public CoreDeleteBackupResponseDto() {
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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
