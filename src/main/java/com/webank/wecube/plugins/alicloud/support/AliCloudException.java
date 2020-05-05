package com.webank.wecube.plugins.alicloud.support;

import com.aliyuncs.exceptions.ErrorType;

public class AliCloudException extends RuntimeException {

    private String requestId;

    private String errCode;

    private String errMsg;

    private ErrorType errorType;

    private String errorDescription;

    public AliCloudException(String requestId, String errCode, String errMsg, ErrorType errorType, String errorDescription) {
        super(String.format("AliCloud server error: [%s]", errMsg));
        this.requestId = requestId;
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.errorType = errorType;
        this.errorDescription = errorDescription;
    }

    public AliCloudException(String message) {
        super(message);
    }

    public AliCloudException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
