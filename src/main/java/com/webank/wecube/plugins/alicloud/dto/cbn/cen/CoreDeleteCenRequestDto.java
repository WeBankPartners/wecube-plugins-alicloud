package com.webank.wecube.plugins.alicloud.dto.cbn.cen;

import com.aliyuncs.cbn.model.v20170912.DeleteCenRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreDeleteCenRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<DeleteCenRequest> {
    private String resourceOwnerId;
    private String resourceOwnerAccount;
    @NotEmpty(message = "cenId field is mandatory")
    private String cenId;
    private String ownerAccount;
    private String ownerId;

    public CoreDeleteCenRequestDto() {
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getResourceOwnerAccount() {
        return resourceOwnerAccount;
    }

    public void setResourceOwnerAccount(String resourceOwnerAccount) {
        this.resourceOwnerAccount = resourceOwnerAccount;
    }

    public String getCenId() {
        return cenId;
    }

    public void setCenId(String cenId) {
        this.cenId = cenId;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("resourceOwnerId", resourceOwnerId)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("cenId", cenId)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .toString();
    }
}
