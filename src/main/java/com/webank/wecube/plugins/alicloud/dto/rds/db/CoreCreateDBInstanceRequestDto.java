package com.webank.wecube.plugins.alicloud.dto.rds.db;

import com.aliyuncs.rds.model.v20140815.CreateDBInstanceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;

/**
 * @author howechen
 */
public class CoreCreateDBInstanceRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateDBInstanceRequest> {

    @JsonProperty("dBInstanceId")
    private String dBInstanceId;

    private String resourceOwnerId;
    private String dBInstanceStorage;
    private String systemDBCharset;
    private String engineVersion;
    private String targetDedicatedHostIdForMaster;
    private String dBInstanceDescription;
    private String businessInfo;
    private String period;
    private String encryptionKey;
    private String dBInstanceClass;
    private String securityIPList;
    private String vSwitchId;
    private String privateIpAddress;
    private String targetDedicatedHostIdForLog;
    private String autoRenew;
    private String roleARN;
    private String zoneId;
    private String instanceNetworkType;
    private String connectionMode;
    private String clientToken;
    private String targetDedicatedHostIdForSlave;
    private String engine;
    private String dBInstanceStorageType;
    private String dedicatedHostGroupId;
    private String dBInstanceNetType;
    private String usedTime;
    private String vPCId;
    private String category;
    private String payType;

    public CoreCreateDBInstanceRequestDto() {
    }

    public String getDBInstanceId() {
        return dBInstanceId;
    }

    public void setDBInstanceId(String dBInstanceId) {
        this.dBInstanceId = dBInstanceId;
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getdBInstanceStorage() {
        return dBInstanceStorage;
    }

    public void setdBInstanceStorage(String dBInstanceStorage) {
        this.dBInstanceStorage = dBInstanceStorage;
    }

    public String getSystemDBCharset() {
        return systemDBCharset;
    }

    public void setSystemDBCharset(String systemDBCharset) {
        this.systemDBCharset = systemDBCharset;
    }

    public String getEngineVersion() {
        return engineVersion;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public String getTargetDedicatedHostIdForMaster() {
        return targetDedicatedHostIdForMaster;
    }

    public void setTargetDedicatedHostIdForMaster(String targetDedicatedHostIdForMaster) {
        this.targetDedicatedHostIdForMaster = targetDedicatedHostIdForMaster;
    }

    public String getdBInstanceDescription() {
        return dBInstanceDescription;
    }

    public void setdBInstanceDescription(String dBInstanceDescription) {
        this.dBInstanceDescription = dBInstanceDescription;
    }

    public String getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getdBInstanceClass() {
        return dBInstanceClass;
    }

    public void setdBInstanceClass(String dBInstanceClass) {
        this.dBInstanceClass = dBInstanceClass;
    }

    public String getSecurityIPList() {
        return securityIPList;
    }

    public void setSecurityIPList(String securityIPList) {
        this.securityIPList = securityIPList;
    }

    public String getvSwitchId() {
        return vSwitchId;
    }

    public void setvSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    public String getPrivateIpAddress() {
        return privateIpAddress;
    }

    public void setPrivateIpAddress(String privateIpAddress) {
        this.privateIpAddress = privateIpAddress;
    }

    public String getTargetDedicatedHostIdForLog() {
        return targetDedicatedHostIdForLog;
    }

    public void setTargetDedicatedHostIdForLog(String targetDedicatedHostIdForLog) {
        this.targetDedicatedHostIdForLog = targetDedicatedHostIdForLog;
    }

    public String getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(String autoRenew) {
        this.autoRenew = autoRenew;
    }

    public String getRoleARN() {
        return roleARN;
    }

    public void setRoleARN(String roleARN) {
        this.roleARN = roleARN;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getInstanceNetworkType() {
        return instanceNetworkType;
    }

    public void setInstanceNetworkType(String instanceNetworkType) {
        this.instanceNetworkType = instanceNetworkType;
    }

    public String getConnectionMode() {
        return connectionMode;
    }

    public void setConnectionMode(String connectionMode) {
        this.connectionMode = connectionMode;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getTargetDedicatedHostIdForSlave() {
        return targetDedicatedHostIdForSlave;
    }

    public void setTargetDedicatedHostIdForSlave(String targetDedicatedHostIdForSlave) {
        this.targetDedicatedHostIdForSlave = targetDedicatedHostIdForSlave;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getdBInstanceStorageType() {
        return dBInstanceStorageType;
    }

    public void setdBInstanceStorageType(String dBInstanceStorageType) {
        this.dBInstanceStorageType = dBInstanceStorageType;
    }

    public String getDedicatedHostGroupId() {
        return dedicatedHostGroupId;
    }

    public void setDedicatedHostGroupId(String dedicatedHostGroupId) {
        this.dedicatedHostGroupId = dedicatedHostGroupId;
    }

    public String getdBInstanceNetType() {
        return dBInstanceNetType;
    }

    public void setdBInstanceNetType(String dBInstanceNetType) {
        this.dBInstanceNetType = dBInstanceNetType;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(String usedTime) {
        this.usedTime = usedTime;
    }

    public String getvPCId() {
        return vPCId;
    }

    public void setvPCId(String vPCId) {
        this.vPCId = vPCId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
