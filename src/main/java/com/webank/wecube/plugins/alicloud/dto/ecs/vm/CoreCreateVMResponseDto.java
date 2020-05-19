package com.webank.wecube.plugins.alicloud.dto.ecs.vm;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs.SpecInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author howechen
 */
public class CoreCreateVMResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateVMResponseDto, CreateInstanceResponse> {
    private String requestId;
    private String instanceId;
    private String tradePrice;
    private String instanceType;
    private String cpu;
    private String memory;
    @JsonProperty(value = "privateIp")
    private String privateIpAddress;

    @JsonProperty(value = "password")
    private String encryptedPassword;

    private String hostName;

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

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getPrivateIpAddress() {
        return privateIpAddress;
    }

    public void setPrivateIpAddress(String privateIpAddress) {
        this.privateIpAddress = privateIpAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }


    public CoreCreateVMResponseDto fromSdk(CreateInstanceResponse response, String encryptedPassword, String privateIpAddress, SpecInfo specInfo, String hostName) {
        final CoreCreateVMResponseDto result = this.fromSdk(response);
        result.setEncryptedPassword(encryptedPassword);
        result.setInstanceType(specInfo.getResourceClass());
        result.setCpu(String.valueOf(specInfo.getCoreMemorySpec().getCore()));
        result.setMemory(String.valueOf(specInfo.getCoreMemorySpec().getMemory()));
        result.setPrivateIpAddress(privateIpAddress);
        result.setHostName(hostName);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .append("instanceId", instanceId)
                .append("tradePrice", tradePrice)
                .append("instanceType", instanceType)
                .append("cpu", cpu)
                .append("memory", memory)
                .append("privateIpAddress", privateIpAddress)
                .append("encryptedPassword", encryptedPassword)
                .append("hostName", hostName)
                .toString();
    }


}
