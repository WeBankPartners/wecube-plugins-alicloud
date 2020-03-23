package com.webank.wecube.plugins.alicloud.dto.vpc.eip;

import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;

/**
 * @author howechen
 */
public class CoreAssociateEipRequestDto extends CoreRequestInputDto<CoreAssociateEipRequestDto> {
    private String resourceOwnerId;
    private String allocationId;
    private String mode;
    private String instanceRegionId;
    private String instanceType;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    private String privateIpAddress;
    private String instanceId;

    public CoreAssociateEipRequestDto() {
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String allocationId) {
        this.allocationId = allocationId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getInstanceRegionId() {
        return instanceRegionId;
    }

    public void setInstanceRegionId(String instanceRegionId) {
        this.instanceRegionId = instanceRegionId;
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

    public String getPrivateIpAddress() {
        return privateIpAddress;
    }

    public void setPrivateIpAddress(String privateIpAddress) {
        this.privateIpAddress = privateIpAddress;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
