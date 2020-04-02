package com.webank.wecube.plugins.alicloud.dto.ecs.vm;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreCreateVMResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateVMResponseDto, CreateInstanceResponse> {
    private String requestId;
    private String instanceId;
    private String tradePrice;

    @JsonProperty(value = "password")
    private String encryptedPassword;

    public CoreCreateVMResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public CoreCreateVMResponseDto fromSdk(CreateInstanceResponse response, String encryptedPassword) {
        final CoreCreateVMResponseDto result = this.fromSdk(response);
        result.setEncryptedPassword(encryptedPassword);
        return result;
    }
}
