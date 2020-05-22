package com.webank.wecube.plugins.alicloud.dto.ecs.vm;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import com.webank.wecube.plugins.alicloud.service.ecs.vm.InstanceChargeType;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.Pair;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @author howechen
 */
public class CoreCreateVMRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateInstanceRequest> {
    @NotEmpty
    private String seed;

    @NotEmpty(message = "instanceSpec field is mandatory.")
    private String instanceSpec;
    @NotEmpty(message = "instanceFamily field is mandatory")
    private String instanceFamily;

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
    private String resourceTag;
    private String autoRenewPeriod;
    private String nodeControllerId;
    private String period;
    private String dryRun;
    private String ownerId;
    @JsonProperty(value = "vSwitchId")
    private String vSwitchId;
    private String privateIpAddress;
    private String spotStrategy;
    private String periodUnit;
    private String instanceName;
    private String autoRenew;
    private String internetChargeType;
    @NotEmpty(message = "zoneId field is mandatory.")
    private String zoneId;
    private String internetMaxBandwidthIn;
    private String useAdditionalService;
    private String affinity;
    @NotEmpty(message = "imageId field is mandatory.")
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

    public String getVSwitchId() {
        return vSwitchId;
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

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getInstanceSpec() {
        return instanceSpec;
    }

    public void setInstanceSpec(String instanceSpec) {
        this.instanceSpec = instanceSpec;
    }

    public String getResourceTag() {
        return resourceTag;
    }

    public void setResourceTag(String resourceTag) {
        this.resourceTag = resourceTag;
    }


    public String getInstanceFamily() {
        return instanceFamily;
    }

    public void setInstanceFamily(String instanceFamily) {
        this.instanceFamily = instanceFamily;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("seed", seed)
                .append("instanceSpec", instanceSpec)
                .append("instanceFamily", instanceFamily)
                .append("instanceId", instanceId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("hpcClusterId", hpcClusterId)
                .append("securityEnhancementStrategy", securityEnhancementStrategy)
                .append("keyPairName", keyPairName)
                .append("spotPriceLimit", spotPriceLimit)
                .append("deletionProtection", deletionProtection)
                .append("resourceGroupId", resourceGroupId)
                .append("hostName", hostName)
                .append("password", password)
                .append("storageSetPartitionNumber", storageSetPartitionNumber)
                .append("tags", tags)
                .append("resourceTag", resourceTag)
                .append("autoRenewPeriod", autoRenewPeriod)
                .append("nodeControllerId", nodeControllerId)
                .append("period", period)
                .append("dryRun", dryRun)
                .append("ownerId", ownerId)
                .append("vSwitchId", vSwitchId)
                .append("privateIpAddress", privateIpAddress)
                .append("spotStrategy", spotStrategy)
                .append("periodUnit", periodUnit)
                .append("instanceName", instanceName)
                .append("autoRenew", autoRenew)
                .append("internetChargeType", internetChargeType)
                .append("zoneId", zoneId)
                .append("internetMaxBandwidthIn", internetMaxBandwidthIn)
                .append("useAdditionalService", useAdditionalService)
                .append("affinity", affinity)
                .append("imageId", imageId)
                .append("clientToken", clientToken)
                .append("vlanId", vlanId)
                .append("spotInterruptionBehavior", spotInterruptionBehavior)
                .append("ioOptimized", ioOptimized)
                .append("securityGroupId", securityGroupId)
                .append("internetMaxBandwidthOut", internetMaxBandwidthOut)
                .append("description", description)
                .append("systemDiskCategory", systemDiskCategory)
                .append("systemDiskPerformanceLevel", systemDiskPerformanceLevel)
                .append("userData", userData)
                .append("passwordInherit", passwordInherit)
                .append("instanceType", instanceType)
                .append("arns", arns)
                .append("instanceChargeType", instanceChargeType)
                .append("deploymentSetId", deploymentSetId)
                .append("innerIpAddress", innerIpAddress)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("tenancy", tenancy)
                .append("systemDiskDiskName", systemDiskDiskName)
                .append("ramRoleName", ramRoleName)
                .append("dedicatedHostId", dedicatedHostId)
                .append("clusterId", clusterId)
                .append("creditSpecification", creditSpecification)
                .append("spotDuration", spotDuration)
                .append("dataDisks", dataDisks)
                .append("storageSetId", storageSetId)
                .append("systemDiskSize", systemDiskSize)
                .append("systemDiskDescription", systemDiskDescription)
                .toString();
    }

    @Override
    public void adaptToAliCloud() {
        if (StringUtils.isNotEmpty(this.getResourceTag())) {
            final List<Pair<String, String>> pairs = PluginStringUtils.splitResourceTag(this.getResourceTag());
            List<CreateInstanceRequest.Tag> tags = new ArrayList<>();
            for (Pair<String, String> pair : pairs) {
                final CreateInstanceRequest.Tag tag = new CreateInstanceRequest.Tag();
                tag.setKey(pair.getKey());
                tag.setValue(pair.getValue());
                tags.add(tag);
            }
            this.setTags(tags);
        }

        if (StringUtils.isNotEmpty(instanceChargeType)) {
            final InstanceChargeType type = EnumUtils.getEnumIgnoreCase(InstanceChargeType.class, instanceChargeType);
            if (null == type) {
                throw new PluginException("Invalid instance charge type");
            }
            instanceChargeType = type.toString();

            if (instanceChargeType.equalsIgnoreCase(InstanceChargeType.PostPaid.toString())) {
                period = null;
                autoRenew = null;
            }
        }

        if (StringUtils.isNotEmpty(periodUnit)) {
            periodUnit = StringUtils.capitalize(periodUnit.toLowerCase());
        }
    }

}
