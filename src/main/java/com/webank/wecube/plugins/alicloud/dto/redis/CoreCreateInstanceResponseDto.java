package com.webank.wecube.plugins.alicloud.dto.redis;

import com.aliyuncs.r_kvstore.model.v20150101.CreateInstanceResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreCreateInstanceResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateInstanceResponseDto, CreateInstanceResponse> {

    @JsonProperty(value = "password")
    private String encryptedPassword;

    private String requestId;
    private String instanceId;
    private String instanceName;
    private String connectionDomain;
    private String port;
    private String userName;
    private String instanceStatus;
    private String regionId;
    private String capacity;
    private String qPS;
    private String bandwidth;
    private String connections;
    private String zoneId;
    private String config;
    private String chargeType;
    private String endTime;
    private String nodeType;
    private String networkType;
    private String vpcId;
    private String vSwitchId;
    private String privateIpAddr;

    public CoreCreateInstanceResponseDto fromSdk(CreateInstanceResponse response, String encryptedPassword) {
        final CoreCreateInstanceResponseDto result = this.fromSdk(response);
        result.setEncryptedPassword(encryptedPassword);
        return result;
    }

    public CoreCreateInstanceResponseDto() {
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

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getConnectionDomain() {
        return connectionDomain;
    }

    public void setConnectionDomain(String connectionDomain) {
        this.connectionDomain = connectionDomain;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(String instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getqPS() {
        return qPS;
    }

    public void setqPS(String qPS) {
        this.qPS = qPS;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getConnections() {
        return connections;
    }

    public void setConnections(String connections) {
        this.connections = connections;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getVSwitchId() {
        return this.vSwitchId;
    }

    public void setVSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    public String getPrivateIpAddr() {
        return privateIpAddr;
    }

    public void setPrivateIpAddr(String privateIpAddr) {
        this.privateIpAddr = privateIpAddr;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
