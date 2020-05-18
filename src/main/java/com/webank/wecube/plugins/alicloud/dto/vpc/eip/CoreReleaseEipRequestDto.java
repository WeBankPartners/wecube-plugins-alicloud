package com.webank.wecube.plugins.alicloud.dto.vpc.eip;

import com.aliyuncs.vpc.model.v20160428.ReleaseEipAddressRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreReleaseEipRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<ReleaseEipAddressRequest> {
    @JsonProperty(value = "cbpName")
    private String name;
    private String resourceOwnerId;
    @NotEmpty(message = "allocationId field is mandatory")
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("cbpName", name)
                .append("resourceOwnerId", resourceOwnerId)
                .append("allocationId", allocationId)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .toString();
    }


}
