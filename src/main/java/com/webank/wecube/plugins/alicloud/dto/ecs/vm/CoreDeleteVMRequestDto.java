package com.webank.wecube.plugins.alicloud.dto.ecs.vm;

import com.aliyuncs.ecs.model.v20140526.DeleteInstanceRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreDeleteVMRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<DeleteInstanceRequest> {
    private String resourceOwnerId;
    private String terminateSubscription;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    @NotEmpty(message = "instanceId field is mandatory.")
    private String instanceId;
    private String force;

    public CoreDeleteVMRequestDto() {
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getTerminateSubscription() {
        return terminateSubscription;
    }

    public void setTerminateSubscription(String terminateSubscription) {
        this.terminateSubscription = terminateSubscription;
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

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("resourceOwnerId", resourceOwnerId)
                .append("terminateSubscription", terminateSubscription)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("instanceId", instanceId)
                .append("force", force)
                .toString();
    }
}
