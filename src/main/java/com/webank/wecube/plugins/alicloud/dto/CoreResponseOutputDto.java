package com.webank.wecube.plugins.alicloud.dto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.webank.wecube.plugins.alicloud.dto.CoreResponseDto.STATUS_OK;

/**
 * @author howechen
 */
public class CoreResponseOutputDto {
    private String errorCode = STATUS_OK;
    private String errorMessage = StringUtils.EMPTY;
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

    public void setUnhandledErrorMessage(String errorMessage) {
        this.errorMessage = "Plugin not properly handled error: " + errorMessage;
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
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("errorCode", errorCode)
                .append("errorMessage", errorMessage)
                .append("guid", guid)
                .append("callbackParameter", callbackParameter)
                .toString();
    }
}
