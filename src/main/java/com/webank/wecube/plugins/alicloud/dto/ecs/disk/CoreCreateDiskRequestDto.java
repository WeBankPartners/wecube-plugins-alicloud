package com.webank.wecube.plugins.alicloud.dto.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.CreateDiskRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;

import java.util.List;

/**
 * @author howechen
 */
public class CoreCreateDiskRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateDiskRequest> {
    private String diskId;

    private String resourceOwnerId;
    private String snapshotId;
    private String clientToken;
    private String description;
    private String diskName;
    private String resourceGroupId;
    private String diskCategory;
    private String storageSetPartitionNumber;
    private List<CreateDiskRequest.Tag> tags;
    private List<CreateDiskRequest.Arn> arns;
    private String advancedFeatures;
    private String resourceOwnerAccount;
    private String performanceLevel;
    private String ownerAccount;
    private String ownerId;
    private String instanceId;
    private String storageSetId;
    private String size;
    private String encrypted;
    private String zoneId;
    private String kMSKeyId;

    public CoreCreateDiskRequestDto() {
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    public String getDiskCategory() {
        return diskCategory;
    }

    public void setDiskCategory(String diskCategory) {
        this.diskCategory = diskCategory;
    }

    public String getStorageSetPartitionNumber() {
        return storageSetPartitionNumber;
    }

    public void setStorageSetPartitionNumber(String storageSetPartitionNumber) {
        this.storageSetPartitionNumber = storageSetPartitionNumber;
    }

    public List<CreateDiskRequest.Tag> getTags() {
        return tags;
    }

    public void setTags(List<CreateDiskRequest.Tag> tags) {
        this.tags = tags;
    }

    public List<CreateDiskRequest.Arn> getArns() {
        return arns;
    }

    public void setArns(List<CreateDiskRequest.Arn> arns) {
        this.arns = arns;
    }

    public String getAdvancedFeatures() {
        return advancedFeatures;
    }

    public void setAdvancedFeatures(String advancedFeatures) {
        this.advancedFeatures = advancedFeatures;
    }

    public String getResourceOwnerAccount() {
        return resourceOwnerAccount;
    }

    public void setResourceOwnerAccount(String resourceOwnerAccount) {
        this.resourceOwnerAccount = resourceOwnerAccount;
    }

    public String getPerformanceLevel() {
        return performanceLevel;
    }

    public void setPerformanceLevel(String performanceLevel) {
        this.performanceLevel = performanceLevel;
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

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getStorageSetId() {
        return storageSetId;
    }

    public void setStorageSetId(String storageSetId) {
        this.storageSetId = storageSetId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getkMSKeyId() {
        return kMSKeyId;
    }

    public void setkMSKeyId(String kMSKeyId) {
        this.kMSKeyId = kMSKeyId;
    }
}
