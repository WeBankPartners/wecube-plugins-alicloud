package com.webank.wecube.plugins.alicloud.dto.ecs.vm;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;

import java.util.List;

/**
 * @author howechen
 */
public class CoreCreateVMRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateInstanceRequest> {
    private String instanceId;

    private String resourceOwnerId;
    private String hpcClusterId;
    private String securityEnhancementStrategy;
    private String keyPairName;
    private String spotPriceLimit;
    private String deletionProtection;
    private String resourceGroupId;
    private String hostName;
    private String password;
    private String storageSetPartitionNumber;
    private List<CreateInstanceRequest.Tag> tags;
    private String autoRenewPeriod;
    private String nodeControllerId;
    private String period;
    private String dryRun;
    private String ownerId;
    private String vSwitchId;
    private String privateIpAddress;
    private String spotStrategy;
    private String periodUnit;
    private String instanceName;
    private String autoRenew;
    private String internetChargeType;
    private String zoneId;
    private String internetMaxBandwidthIn;
    private String useAdditionalService;
    private String affinity;
    private String imageId;
    private String clientToken;
    private String vlanId;
    private String spotInterruptionBehavior;
    private String ioOptimized;
    private String securityGroupId;
    private String internetMaxBandwidthOut;
    private String description;
    private String systemDiskCategory;
    private String systemDiskPerformanceLevel;
    private String userData;
    private String passwordInherit;
    private String instanceType;
    private List<CreateInstanceRequest.Arn> arns;
    private String instanceChargeType;
    private String deploymentSetId;
    private String innerIpAddress;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String tenancy;
    private String systemDiskDiskName;
    private String ramRoleName;
    private String dedicatedHostId;
    private String clusterId;
    private String creditSpecification;
    private String spotDuration;
    private List<CreateInstanceRequest.DataDisk> dataDisks;
    private String storageSetId;
    private String systemDiskSize;
    private String systemDiskDescription;

    public CoreCreateVMRequestDto() {
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

    public String getHpcClusterId() {
        return hpcClusterId;
    }

    public void setHpcClusterId(String hpcClusterId) {
        this.hpcClusterId = hpcClusterId;
    }

    public String getSecurityEnhancementStrategy() {
        return securityEnhancementStrategy;
    }

    public void setSecurityEnhancementStrategy(String securityEnhancementStrategy) {
        this.securityEnhancementStrategy = securityEnhancementStrategy;
    }

    public String getKeyPairName() {
        return keyPairName;
    }

    public void setKeyPairName(String keyPairName) {
        this.keyPairName = keyPairName;
    }

    public String getSpotPriceLimit() {
        return spotPriceLimit;
    }

    public void setSpotPriceLimit(String spotPriceLimit) {
        this.spotPriceLimit = spotPriceLimit;
    }

    public String getDeletionProtection() {
        return deletionProtection;
    }

    public void setDeletionProtection(String deletionProtection) {
        this.deletionProtection = deletionProtection;
    }

    public String getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStorageSetPartitionNumber() {
        return storageSetPartitionNumber;
    }

    public void setStorageSetPartitionNumber(String storageSetPartitionNumber) {
        this.storageSetPartitionNumber = storageSetPartitionNumber;
    }

    public List<CreateInstanceRequest.Tag> getTags() {
        return tags;
    }

    public void setTags(List<CreateInstanceRequest.Tag> tags) {
        this.tags = tags;
    }

    public String getAutoRenewPeriod() {
        return autoRenewPeriod;
    }

    public void setAutoRenewPeriod(String autoRenewPeriod) {
        this.autoRenewPeriod = autoRenewPeriod;
    }

    public String getNodeControllerId() {
        return nodeControllerId;
    }

    public void setNodeControllerId(String nodeControllerId) {
        this.nodeControllerId = nodeControllerId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDryRun() {
        return dryRun;
    }

    public void setDryRun(String dryRun) {
        this.dryRun = dryRun;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public String getSpotStrategy() {
        return spotStrategy;
    }

    public void setSpotStrategy(String spotStrategy) {
        this.spotStrategy = spotStrategy;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
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

    public String getInternetChargeType() {
        return internetChargeType;
    }

    public void setInternetChargeType(String internetChargeType) {
        this.internetChargeType = internetChargeType;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getInternetMaxBandwidthIn() {
        return internetMaxBandwidthIn;
    }

    public void setInternetMaxBandwidthIn(String internetMaxBandwidthIn) {
        this.internetMaxBandwidthIn = internetMaxBandwidthIn;
    }

    public String getUseAdditionalService() {
        return useAdditionalService;
    }

    public void setUseAdditionalService(String useAdditionalService) {
        this.useAdditionalService = useAdditionalService;
    }

    public String getAffinity() {
        return affinity;
    }

    public void setAffinity(String affinity) {
        this.affinity = affinity;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getVlanId() {
        return vlanId;
    }

    public void setVlanId(String vlanId) {
        this.vlanId = vlanId;
    }

    public String getSpotInterruptionBehavior() {
        return spotInterruptionBehavior;
    }

    public void setSpotInterruptionBehavior(String spotInterruptionBehavior) {
        this.spotInterruptionBehavior = spotInterruptionBehavior;
    }

    public String getIoOptimized() {
        return ioOptimized;
    }

    public void setIoOptimized(String ioOptimized) {
        this.ioOptimized = ioOptimized;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public String getInternetMaxBandwidthOut() {
        return internetMaxBandwidthOut;
    }

    public void setInternetMaxBandwidthOut(String internetMaxBandwidthOut) {
        this.internetMaxBandwidthOut = internetMaxBandwidthOut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemDiskCategory() {
        return systemDiskCategory;
    }

    public void setSystemDiskCategory(String systemDiskCategory) {
        this.systemDiskCategory = systemDiskCategory;
    }

    public String getSystemDiskPerformanceLevel() {
        return systemDiskPerformanceLevel;
    }

    public void setSystemDiskPerformanceLevel(String systemDiskPerformanceLevel) {
        this.systemDiskPerformanceLevel = systemDiskPerformanceLevel;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getPasswordInherit() {
        return passwordInherit;
    }

    public void setPasswordInherit(String passwordInherit) {
        this.passwordInherit = passwordInherit;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public List<CreateInstanceRequest.Arn> getArns() {
        return arns;
    }

    public void setArns(List<CreateInstanceRequest.Arn> arns) {
        this.arns = arns;
    }

    public String getInstanceChargeType() {
        return instanceChargeType;
    }

    public void setInstanceChargeType(String instanceChargeType) {
        this.instanceChargeType = instanceChargeType;
    }

    public String getDeploymentSetId() {
        return deploymentSetId;
    }

    public void setDeploymentSetId(String deploymentSetId) {
        this.deploymentSetId = deploymentSetId;
    }

    public String getInnerIpAddress() {
        return innerIpAddress;
    }

    public void setInnerIpAddress(String innerIpAddress) {
        this.innerIpAddress = innerIpAddress;
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

    public String getTenancy() {
        return tenancy;
    }

    public void setTenancy(String tenancy) {
        this.tenancy = tenancy;
    }

    public String getSystemDiskDiskName() {
        return systemDiskDiskName;
    }

    public void setSystemDiskDiskName(String systemDiskDiskName) {
        this.systemDiskDiskName = systemDiskDiskName;
    }

    public String getRamRoleName() {
        return ramRoleName;
    }

    public void setRamRoleName(String ramRoleName) {
        this.ramRoleName = ramRoleName;
    }

    public String getDedicatedHostId() {
        return dedicatedHostId;
    }

    public void setDedicatedHostId(String dedicatedHostId) {
        this.dedicatedHostId = dedicatedHostId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getCreditSpecification() {
        return creditSpecification;
    }

    public void setCreditSpecification(String creditSpecification) {
        this.creditSpecification = creditSpecification;
    }

    public String getSpotDuration() {
        return spotDuration;
    }

    public void setSpotDuration(String spotDuration) {
        this.spotDuration = spotDuration;
    }

    public List<CreateInstanceRequest.DataDisk> getDataDisks() {
        return dataDisks;
    }

    public void setDataDisks(List<CreateInstanceRequest.DataDisk> dataDisks) {
        this.dataDisks = dataDisks;
    }

    public String getStorageSetId() {
        return storageSetId;
    }

    public void setStorageSetId(String storageSetId) {
        this.storageSetId = storageSetId;
    }

    public String getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(String systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public String getSystemDiskDescription() {
        return systemDiskDescription;
    }

    public void setSystemDiskDescription(String systemDiskDescription) {
        this.systemDiskDescription = systemDiskDescription;
    }
}
