package com.webank.wecube.plugins.alicloud.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author howechen
 */
public class CoreResponseOutputDto {
    private String errorCode;
    private String errorMessage;
    private String guid;
    private String callbackParameter;

    public CoreResponseOutputDto() {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("errorCode", errorCode)
                .append("errorMessage", errorMessage)
                .append("guid", guid)
                .append("callbackParameter", callbackParameter)
                .toString();
    }
}
