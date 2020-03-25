package com.webank.wecube.plugins.alicloud.dto.ecs.vm;

import com.aliyuncs.ecs.model.v20140526.StopInstanceRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;

/**
 * @author howechen
 */
public class CoreStopVMRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<StopInstanceRequest> {
    private String resourceOwnerId;
    private String stoppedMode;
    private String forceStop;
    private String confirmStop;
    private String dryRun;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    private String instanceId;

    public CoreStopVMRequestDto() {
    }


    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getStoppedMode() {
        return stoppedMode;
    }

    public void setStoppedMode(String stoppedMode) {
        this.stoppedMode = stoppedMode;
    }

    public String getForceStop() {
        return forceStop;
    }

    public void setForceStop(String forceStop) {
        this.forceStop = forceStop;
    }

    public String getConfirmStop() {
        return confirmStop;
    }

    public void setConfirmStop(String confirmStop) {
        this.confirmStop = confirmStop;
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
