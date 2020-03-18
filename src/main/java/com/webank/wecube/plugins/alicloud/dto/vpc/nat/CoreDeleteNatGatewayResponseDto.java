package com.webank.wecube.plugins.alicloud.dto.vpc.nat;

import com.aliyuncs.vpc.model.v20160428.DeleteNatGatewayResponse;

/**
 * @author howechen
 */
public class CoreDeleteNatGatewayResponseDto extends DeleteNatGatewayResponse {
    private String errorCode;
    private String errorMessage;
    private String guid;
    private String callbackParameter;

    public CoreDeleteNatGatewayResponseDto() {
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
