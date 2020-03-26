package com.webank.wecube.plugins.alicloud.dto.redis;

import com.aliyuncs.r_kvstore.model.v20150101.CreateInstanceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;

/**
 * @author howechen
 */
public class CoreCreateInstanceRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateInstanceRequest> {

    private String instanceId;

    private String resourceOwnerId;
    private String couponNo;
    private String networkType;
    private String engineVersion;
    private String resourceGroupId;
    private String password;
    private String securityToken;
    private String businessInfo;
    private String autoRenewPeriod;
    private String period;
    private String backupId;
    private String ownerId;
    @JsonProperty(value = "vSwitchId")
    private String vSwitchId;
    private String privateIpAddress;
    private String instanceName;
    private String autoRenew;
    private String zoneId;
    private String nodeType;
    private String autoUseCoupon;
    private String instanceClass;
    private String capacity;
    private String instanceType;
    private String resourceOwnerAccount;
    private String srcDBInstanceId;
    private String ownerAccount;
    private String token;
    private String vpcId;
    private String chargeType;
    private String config;

    public CoreCreateInstanceRequestDto() {
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getEngineVersion() {
        return engineVersion;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public String getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo;
    }

    public String getAutoRenewPeriod() {
        return autoRenewPeriod;
    }

    public void setAutoRenewPeriod(String autoRenewPeriod) {
        this.autoRenewPeriod = autoRenewPeriod;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getBackupId() {
        return backupId;
    }

    public void setBackupId(String backupId) {
        this.backupId = backupId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getVSwitchId() {
        return this.vSwitchId;
    }

    public void setVSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    public String getPrivateIpAddress() {
        return privateIpAddress;
    }

    public void setPrivateIpAddress(String privateIpAddress) {
        this.privateIpAddress = privateIpAddress;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(String autoRenew) {
        this.autoRenew = autoRenew;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getAutoUseCoupon() {
        return autoUseCoupon;
    }

    public void setAutoUseCoupon(String autoUseCoupon) {
        this.autoUseCoupon = autoUseCoupon;
    }

    public String getInstanceClass() {
        return instanceClass;
    }

    public void setInstanceClass(String instanceClass) {
        this.instanceClass = instanceClass;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getResourceOwnerAccount() {
        return resourceOwnerAccount;
    }

    public void setResourceOwnerAccount(String resourceOwnerAccount) {
        this.resourceOwnerAccount = resourceOwnerAccount;
    }

    public String getSrcDBInstanceId() {
        return srcDBInstanceId;
    }

    public void setSrcDBInstanceId(String srcDBInstanceId) {
        this.srcDBInstanceId = srcDBInstanceId;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
