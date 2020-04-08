package com.webank.wecube.plugins.alicloud.dto.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.DetachDiskRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreDetachDiskRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<DetachDiskRequest> {


    @NotEmpty(message = "Seed is mandatory.")
    private String seed;
    @NotEmpty(message = "Password is mandatory.")
    @JsonProperty("password")
    private String unmountDir;
    private String hostPassword;
    private String volumeName;

    private String resourceOwnerId;
    private String diskId;
    private String deleteWithInstance;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    private String instanceId;


    public CoreDetachDiskRequestDto() {
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

    public String getDeleteWithInstance() {
        return deleteWithInstance;
    }

    public void setDeleteWithInstance(String deleteWithInstance) {
        this.deleteWithInstance = deleteWithInstance;
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

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getHostPassword() {
        return hostPassword;
    }

    public void setHostPassword(String hostPassword) {
        this.hostPassword = hostPassword;
    }

    public String getUnmountDir() {
        return unmountDir;
    }

    public void setUnmountDir(String unmountDir) {
        this.unmountDir = unmountDir;
    }
}
