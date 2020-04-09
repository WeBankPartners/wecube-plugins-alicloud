package com.webank.wecube.plugins.alicloud.dto.ecs.vm;

import com.aliyuncs.ecs.model.v20140526.StartInstanceRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;

/**
 * @author howechen
 */
public class CoreStartVMRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<StartInstanceRequest> {
    private String resourceOwnerId;
    private String sourceRegionId;
    private String initLocalDisk;
    private String dryRun;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    private String instanceId;


    public CoreStartVMRequestDto() {
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getSourceRegionId() {
        return sourceRegionId;
    }

    public void setSourceRegionId(String sourceRegionId) {
        this.sourceRegionId = sourceRegionId;
    }

    public String getInitLocalDisk() {
        return initLocalDisk;
    }

    public void setInitLocalDisk(String initLocalDisk) {
        this.initLocalDisk = initLocalDisk;
    }

    public String getDryRun() {
        return dryRun;
    }

    public void setDryRun(String dryRun) {
        this.dryRun = dryRun;
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
}
