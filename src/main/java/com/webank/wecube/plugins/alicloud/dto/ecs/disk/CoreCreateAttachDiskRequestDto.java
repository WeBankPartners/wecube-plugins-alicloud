package com.webank.wecube.plugins.alicloud.dto.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.CreateDiskRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author howechen
 */
public class CoreCreateAttachDiskRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateDiskRequest> {
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
    @NotEmpty(message = "instanceId field is mandatory")
    private String instanceId;
    private String storageSetId;
    @NotEmpty(message = "size field is mandatory")
    private String size;
    private String encrypted;
    @NotEmpty(message = "zoneId field is mandatory")
    private String zoneId;
    private String kMSKeyId;

    // attach disk fields
    @NotEmpty(message = "Password field is mandatory.")
    @JsonProperty(value = "password")
    private String hostPassword;
    @NotEmpty(message = "fileSystemType field is mandatory")
    private String fileSystemType;
    @NotEmpty(message = "mountDir field is mandatory")
    private String mountDir;
    @NotEmpty(message = "Seed field is mandatory.")
    private String seed;

    private String keyPairName;
    private String bootable;
    private String deleteWithInstance;
    private String device;

    public CoreCreateAttachDiskRequestDto() {
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

    public String getHostPassword() {
        return hostPassword;
    }

    public void setHostPassword(String hostPassword) {
        this.hostPassword = hostPassword;
    }

    public String getFileSystemType() {
        return fileSystemType;
    }

    public void setFileSystemType(String fileSystemType) {
        this.fileSystemType = fileSystemType;
    }

    public String getMountDir() {
        return mountDir;
    }

    public void setMountDir(String mountDir) {
        this.mountDir = mountDir;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getKeyPairName() {
        return keyPairName;
    }

    public void setKeyPairName(String keyPairName) {
        this.keyPairName = keyPairName;
    }

    public String getBootable() {
        return bootable;
    }

    public void setBootable(String bootable) {
        this.bootable = bootable;
    }

    public String getDeleteWithInstance() {
        return deleteWithInstance;
    }

    public void setDeleteWithInstance(String deleteWithInstance) {
        this.deleteWithInstance = deleteWithInstance;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public CreateDiskRequest toSdk() {
        if (null != this.getZoneId()) {
            this.setInstanceId(null);
        }
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(this, CreateDiskRequest.class);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("diskId", diskId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("snapshotId", snapshotId)
                .append("clientToken", clientToken)
                .append("description", description)
                .append("diskName", diskName)
                .append("resourceGroupId", resourceGroupId)
                .append("diskCategory", diskCategory)
                .append("storageSetPartitionNumber", storageSetPartitionNumber)
                .append("tags", tags)
                .append("arns", arns)
                .append("advancedFeatures", advancedFeatures)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("performanceLevel", performanceLevel)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("instanceId", instanceId)
                .append("storageSetId", storageSetId)
                .append("size", size)
                .append("encrypted", encrypted)
                .append("zoneId", zoneId)
                .append("kMSKeyId", kMSKeyId)
                .append("hostPassword", hostPassword)
                .append("fileSystemType", fileSystemType)
                .append("mountDir", mountDir)
                .append("seed", seed)
                .append("keyPairName", keyPairName)
                .append("bootable", bootable)
                .append("deleteWithInstance", deleteWithInstance)
                .append("device", device)
                .toString();
    }
}
