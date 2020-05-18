package com.webank.wecube.plugins.alicloud.dto.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.DeleteDiskRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreDetachDeleteDiskRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<DeleteDiskRequest> {
    private String resourceOwnerId;
    @NotEmpty(message = "diskId is mandatory.")
    private String diskId;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;

    // detach disk fields
    @NotEmpty(message = "Seed is mandatory.")
    private String seed;
    @NotEmpty(message = "UnmountDir is mandatory.")
    private String unmountDir;
    @NotEmpty(message = "Password is mandatory.")
    @JsonProperty("password")
    private String hostPassword;
    @NotEmpty(message = "VolumeName is mandatory")
    private String volumeName;

    private String deleteWithInstance;
    @NotEmpty(message = "instanceId is mandatory.")
    private String instanceId;

    @NotEmpty(message = "instanceGuid field is mandatory")
    private String instanceGuid;

    public CoreDetachDeleteDiskRequestDto() {
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
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

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getUnmountDir() {
        return unmountDir;
    }

    public void setUnmountDir(String unmountDir) {
        this.unmountDir = unmountDir;
    }

    public String getHostPassword() {
        return hostPassword;
    }

    public void setHostPassword(String hostPassword) {
        this.hostPassword = hostPassword;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public String getDeleteWithInstance() {
        return deleteWithInstance;
    }

    public void setDeleteWithInstance(String deleteWithInstance) {
        this.deleteWithInstance = deleteWithInstance;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceGuid() {
        return instanceGuid;
    }

    public void setInstanceGuid(String instanceGuid) {
        this.instanceGuid = instanceGuid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("resourceOwnerId", resourceOwnerId)
                .append("diskId", diskId)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("seed", seed)
                .append("unmountDir", unmountDir)
                .append("hostPassword", hostPassword)
                .append("volumeName", volumeName)
                .append("deleteWithInstance", deleteWithInstance)
                .append("instanceId", instanceId)
                .append("instanceGuid", instanceGuid)
                .toString();
    }
}
