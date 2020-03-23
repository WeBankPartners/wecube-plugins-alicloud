package com.webank.wecube.plugins.alicloud.dto.vpc.eip;

import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;

/**
 * @author howechen
 */
public class CoreReleaseEipRequestDto extends CoreRequestInputDto<CoreReleaseEipRequestDto> {
    private String resourceOwnerId;
    private String allocationId;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;

    public CoreReleaseEipRequestDto() {
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
}
