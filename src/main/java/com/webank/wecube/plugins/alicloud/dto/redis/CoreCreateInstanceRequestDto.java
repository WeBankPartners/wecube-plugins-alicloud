package com.webank.wecube.plugins.alicloud.dto.redis;

import com.aliyuncs.r_kvstore.model.v20150101.CreateInstanceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import com.webank.wecube.plugins.alicloud.service.redis.ChargeType;
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
public class CoreCreateInstanceRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateInstanceRequest> {

    private String instanceId;

//    // redis instance spec
//    @NotEmpty(message = "seriesType field is mandatory")
//    private String seriesType;
//    @NotEmpty(message = "architecture field is mandatory")
//    private String architecture;
//    @NotEmpty(message = "shardNumber field is mandatory")
//    private String shardNumber;
//    @NotEmpty(message = "supportedNodeType field is mandatory")
//    private String supportedNodeType;
//

    // redis password generation
    @NotEmpty(message = "seed field is mandatory")
    private String seed;

    private String resourceOwnerId;
    private String couponNo;
    private String networkType;
    //    @NotEmpty(message = "engineVersion field is mandatory")
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
    @NotEmpty(message = "chargeType field is mandatory")
    private String chargeType;
    private String config;

    // securityIp config
    private String modifyMode = "Append";
    private String securityIps;

    // security group config
    private String securityGroupId;

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

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getModifyMode() {
        return modifyMode;
    }

    public void setModifyMode(String modifyMode) {
        this.modifyMode = modifyMode;
    }

    public String getSecurityIps() {
        return securityIps;
    }

    public void setSecurityIps(String securityIps) {
        this.securityIps = securityIps;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }
//
//    public String getSeriesType() {
//        return seriesType;
//    }
//
//    public void setSeriesType(String seriesType) {
//        this.seriesType = seriesType;
//    }
//
//    public String getShardNumber() {
//        return shardNumber;
//    }
//
//    public void setShardNumber(String shardNumber) {
//        this.shardNumber = shardNumber;
//    }
//
//    public String getArchitecture() {
//        return architecture;
//    }
//
//    public void setArchitecture(String architecture) {
//        this.architecture = architecture;
//    }
//
//    public String getSupportedNodeType() {
//        return supportedNodeType;
//    }
//
//    public void setSupportedNodeType(String supportedNodeType) {
//        this.supportedNodeType = supportedNodeType;
//    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("instanceId", instanceId)
//                .append("seriesType", seriesType)
//                .append("architecture", architecture)
//                .append("shardNumber", shardNumber)
//                .append("supportedNodeType", supportedNodeType)
                .append("seed", seed)
                .append("resourceOwnerId", resourceOwnerId)
                .append("couponNo", couponNo)
                .append("networkType", networkType)
                .append("engineVersion", engineVersion)
                .append("resourceGroupId", resourceGroupId)
                .append("password", password)
                .append("securityToken", securityToken)
                .append("businessInfo", businessInfo)
                .append("autoRenewPeriod", autoRenewPeriod)
                .append("period", period)
                .append("backupId", backupId)
                .append("ownerId", ownerId)
                .append("vSwitchId", vSwitchId)
                .append("privateIpAddress", privateIpAddress)
                .append("instanceName", instanceName)
                .append("autoRenew", autoRenew)
                .append("zoneId", zoneId)
                .append("nodeType", nodeType)
                .append("autoUseCoupon", autoUseCoupon)
                .append("instanceClass", instanceClass)
                .append("capacity", capacity)
                .append("instanceType", instanceType)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("srcDBInstanceId", srcDBInstanceId)
                .append("ownerAccount", ownerAccount)
                .append("token", token)
                .append("vpcId", vpcId)
                .append("chargeType", chargeType)
                .append("config", config)
                .append("modifyMode", modifyMode)
                .append("securityIps", securityIps)
                .append("securityGroupId", securityGroupId)
                .toString();
    }

    @Override
    public void adaptToAliCloud() throws PluginException {
        if (!StringUtils.isEmpty(chargeType)) {
            final ChargeType type = EnumUtils.getEnumIgnoreCase(ChargeType.class, chargeType);
            if (type == null) {
                throw new PluginException("Invalid charge type");
            }
            chargeType = type.toString();
        }

        if (!StringUtils.isEmpty(securityIps)) {
            securityIps = PluginStringUtils.removeSquareBracket(securityIps);
        }

        if (!StringUtils.isEmpty(securityGroupId)) {
            securityGroupId = PluginStringUtils.removeSquareBracket(securityGroupId);
        }

        if (!StringUtils.isEmpty(zoneId)) {
            String resultZoneId = zoneId;
            final List<String> zoneIdList = PluginStringUtils.splitStringList(zoneId);
            if (zoneIdList.size() == 1) {
                // basic RDS category
                if (!isValidBasicZoneId(zoneId)) {
                    final String rawStr = zoneIdList.get(0);
                    resultZoneId = removeMAZField(rawStr);
                }
            } else {
                // other RDS categories
                if (!isValidMAZZoneId(zoneId)) {
                    resultZoneId = concatHighAvailableZoneId(zoneIdList);
                }
            }
            zoneId = resultZoneId;
        }
    }


}
