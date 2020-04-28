package com.webank.wecube.plugins.alicloud.dto.vpc.nat;

import com.aliyuncs.vpc.model.v20160428.DeleteSnatEntryRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreDeleteSnatEntryRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<DeleteSnatEntryRequest> {
    private String resourceOwnerId;

    private String clientToken;

    @NotEmpty(message = "snatEntryId field is mandatory")
    private String snatEntryId;

    private String resourceOwnerAccount;

    private String ownerAccount;

    @NotEmpty(message = "snatTableId field is mandatory")
    private String snatTableId;

    private String ownerId;

    public CoreDeleteSnatEntryRequestDto() {
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getSnatEntryId() {
        return snatEntryId;
    }

    public void setSnatEntryId(String snatEntryId) {
        this.snatEntryId = snatEntryId;
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

    public String getSnatTableId() {
        return snatTableId;
    }

    public void setSnatTableId(String snatTableId) {
        this.snatTableId = snatTableId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("resourceOwnerId", resourceOwnerId)
                .append("clientToken", clientToken)
                .append("snatEntryId", snatEntryId)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("snatTableId", snatTableId)
                .append("ownerId", ownerId)
                .toString();
    }
}
