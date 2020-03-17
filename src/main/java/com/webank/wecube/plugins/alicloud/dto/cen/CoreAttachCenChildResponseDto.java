package com.webank.wecube.plugins.alicloud.dto.cen;

import com.aliyuncs.cbn.model.v20170912.AttachCenChildInstanceResponse;

/**
 * @author howechen
 */
public class CoreAttachCenChildResponseDto extends AttachCenChildInstanceResponse {
    private String errorCode;
    private String errorMessage;
    private String guid;
    private String callbackParameter;

    public CoreAttachCenChildResponseDto() {
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
