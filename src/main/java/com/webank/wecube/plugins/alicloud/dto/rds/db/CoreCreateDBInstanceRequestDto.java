package com.webank.wecube.plugins.alicloud.dto.rds.db;

import com.aliyuncs.rds.model.v20140815.CreateDBInstanceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import com.webank.wecube.plugins.alicloud.service.rds.RDSCategory;
import com.webank.wecube.plugins.alicloud.support.resourceSeeker.RDSResourceSeeker;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import java.util.List;

import static com.webank.wecube.plugins.alicloud.support.ZoneIdHelper.*;

/**
 * @author howechen
 */
public class CoreCreateDBInstanceRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateDBInstanceRequest> {

    @JsonProperty("dBInstanceId")
    private String dBInstanceId;
    @NotEmpty(message = "dBInstanceSpec field is mandatory.")
    @JsonProperty("dBInstanceSpec")
    private String dBInstanceSpec;

    // security group config
    private String securityGroupId;

    // RDS user management
    private String seed;
    private String accountType = "Super";
    private String accountDescription;
    @NotEmpty(message = "The accountName cannot be empty.")
    private String accountName;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    private String accountPassword;

    private String dBParamGroupId;
    private String resourceOwnerId;
    @NotEmpty(message = "dBInstanceStorage field is mandatory.")
    @JsonProperty("dBInstanceStorage")
    private String dBInstanceStorage;
    private String systemDBCharset;
    @NotEmpty(message = "engineVersion field is mandatory.")
    private String engineVersion;
    private String targetDedicatedHostIdForMaster;
    @JsonProperty("dBInstanceDescription")
    private String dBInstanceDescription;
    private String businessInfo;
    private String period;
    private String encryptionKey;
    @JsonProperty("dBInstanceClass")
    private String dBInstanceClass;
    @NotEmpty(message = "securityIPList field is mandatory.")
    @JsonProperty(value = "securityIPList")
    private String securityIPList;
    @JsonProperty(value = "vSwitchId")
    private String vSwitchId;
    private String privateIpAddress;
    private String targetDedicatedHostIdForLog;
    private String autoRenew;
    private String roleARN;
    private String zoneId;
    private String instanceNetworkType = "VPC";
    private String connectionMode;
    private String clientToken;
    private String targetDedicatedHostIdForSlave;
    @NotEmpty(message = "engine field is mandatory.")
    private String engine;
    @NotEmpty(message = "dBInstanceStorageType field is mandatory")
    @JsonProperty("dBInstanceStorageType")
    private String dBInstanceStorageType;
    private String dedicatedHostGroupId;
    @JsonProperty("dBInstanceNetType")
    private String dBInstanceNetType = "Intranet";
    private String usedTime;
    @JsonProperty(value = "vpcId")
    private String vPCId;
    private String category;
    @NotEmpty(message = "payType field is mandatory.")
    private String payType;
    @NotEmpty(message = "dBIsIgnoreCase field is mandatory")
    @JsonProperty(value = "dBIsIgnoreCase")
    private String dBIsIgnoreCase;

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

    public String getDBInstanceStorage() {
        return dBInstanceStorage;
    }

    public void setDBInstanceStorage(String dBInstanceStorage) {
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

    public String getDBInstanceDescription() {
        return dBInstanceDescription;
    }

    public void setDBInstanceDescription(String dBInstanceDescription) {
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

    public String getDBInstanceClass() {
        return dBInstanceClass;
    }

    public void setDBInstanceClass(String dBInstanceClass) {
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

    public String getDBInstanceStorageType() {
        return dBInstanceStorageType;
    }

    public void setDBInstanceStorageType(String dBInstanceStorageType) {
        this.dBInstanceStorageType = dBInstanceStorageType;
    }

    public String getDedicatedHostGroupId() {
        return dedicatedHostGroupId;
    }

    public void setDedicatedHostGroupId(String dedicatedHostGroupId) {
        this.dedicatedHostGroupId = dedicatedHostGroupId;
    }

    public String getDBInstanceNetType() {
        return dBInstanceNetType;
    }

    public void setDBInstanceNetType(String dBInstanceNetType) {
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

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getResourceOwnerAccount() {
        return resourceOwnerAccount;
    }

    public void setResourceOwnerAccount(String resourceOwnerAccount) {
        this.resourceOwnerAccount = resourceOwnerAccount;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getDBIsIgnoreCase() {
        return dBIsIgnoreCase;
    }

    public void setDBIsIgnoreCase(String dBIsIgnoreCase) {
        this.dBIsIgnoreCase = dBIsIgnoreCase;
    }

    public String getdBInstanceSpec() {
        return dBInstanceSpec;
    }

    public void setDBInstanceSpec(String dBInstanceSpec) {
        this.dBInstanceSpec = dBInstanceSpec;
    }

    public String getDBParamGroupId() {
        return dBParamGroupId;
    }

    public void setdBParamGroupId(String dBParamGroupId) {
        this.dBParamGroupId = dBParamGroupId;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("dBInstanceId", dBInstanceId)
                .append("dBInstanceSpec", dBInstanceSpec)
                .append("securityGroupId", securityGroupId)
                .append("seed", seed)
                .append("accountType", accountType)
                .append("accountDescription", accountDescription)
                .append("accountName", accountName)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("accountPassword", accountPassword)
                .append("dBParamGroupId", dBParamGroupId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("dBInstanceStorage", dBInstanceStorage)
                .append("systemDBCharset", systemDBCharset)
                .append("engineVersion", engineVersion)
                .append("targetDedicatedHostIdForMaster", targetDedicatedHostIdForMaster)
                .append("dBInstanceDescription", dBInstanceDescription)
                .append("businessInfo", businessInfo)
                .append("period", period)
                .append("encryptionKey", encryptionKey)
                .append("dBInstanceClass", dBInstanceClass)
                .append("securityIPList", securityIPList)
                .append("vSwitchId", vSwitchId)
                .append("privateIpAddress", privateIpAddress)
                .append("targetDedicatedHostIdForLog", targetDedicatedHostIdForLog)
                .append("autoRenew", autoRenew)
                .append("roleARN", roleARN)
                .append("zoneId", zoneId)
                .append("instanceNetworkType", instanceNetworkType)
                .append("connectionMode", connectionMode)
                .append("clientToken", clientToken)
                .append("targetDedicatedHostIdForSlave", targetDedicatedHostIdForSlave)
                .append("engine", engine)
                .append("dBInstanceStorageType", dBInstanceStorageType)
                .append("dedicatedHostGroupId", dedicatedHostGroupId)
                .append("dBInstanceNetType", dBInstanceNetType)
                .append("usedTime", usedTime)
                .append("vPCId", vPCId)
                .append("category", category)
                .append("payType", payType)
                .append("dBIsIgnoreCase", dBIsIgnoreCase)
                .toString();
    }

    @Override
    public void adaptToAliCloud() throws PluginException {
        if (StringUtils.isNotEmpty(period)) {
            period = StringUtils.capitalize(period);
        }

        if (StringUtils.isNotEmpty(zoneId)) {
            String resultZoneId = zoneId;
            final List<String> strings = PluginStringUtils.splitStringList(zoneId);
            if (StringUtils.equalsIgnoreCase(RDSCategory.Basic.toString(), category)) {
                // basic RDS category
                if (!isValidBasicZoneId(this.getZoneId())) {
                    if (strings.size() != 1) {
                        throw new PluginException("RDS basic category support one zone only.");
                    } else {
                        final String rawStr = strings.get(0);
                        resultZoneId = removeMAZField(rawStr);
                    }
                }
            } else {
                // other RDS categories
                if (!isValidMAZZoneId(this.getZoneId())) {
                    resultZoneId = concatHighAvailableZoneId(strings);
                }
            }
            zoneId = resultZoneId;
        }

        if (StringUtils.isNotEmpty(securityIPList) && PluginStringUtils.isListStr(securityIPList)) {
            securityIPList = PluginStringUtils.removeSquareBracket(securityIPList);
        }

        if (StringUtils.isNotEmpty(engine)) {
            final RDSResourceSeeker.RDSEngine engineType = EnumUtils.getEnumIgnoreCase(RDSResourceSeeker.RDSEngine.class, engine);
            if (null == engineType) {
                throw new PluginException("Invalid engine type.");
            }
            engine = engineType.getEngine();
        }

        if (StringUtils.isNotEmpty(payType)) {
            payType = StringUtils.capitalize(payType.toLowerCase());
        }

        if (StringUtils.isNotEmpty(period)) {
            period = StringUtils.capitalize(period.toLowerCase());
        }

        if (StringUtils.isNotEmpty(securityGroupId)) {
            securityGroupId = PluginStringUtils.removeSquareBracket(securityGroupId);
        }
    }
}
