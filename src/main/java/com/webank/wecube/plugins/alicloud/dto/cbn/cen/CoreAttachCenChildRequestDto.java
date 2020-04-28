package com.webank.wecube.plugins.alicloud.dto.cbn.cen;

import com.aliyuncs.cbn.model.v20170912.AttachCenChildInstanceRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreAttachCenChildRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<AttachCenChildInstanceRequest> {
    private String resourceOwnerId;
    @NotEmpty(message = "cenId field is mandatory")
    private String cenId;
    @NotEmpty(message = "childInstanceRegionId field is mandatory")
    private String childInstanceRegionId;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    @NotEmpty(message = "childInstanceType field is mandatory")
    private String childInstanceType;
    private String childInstanceOwnerId;
    @NotEmpty(message = "childInstanceId field is mandatory")
    private String childInstanceId;

    public CoreAttachCenChildRequestDto() {
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getCenId() {
        return cenId;
    }

    public void setCenId(String cenId) {
        this.cenId = cenId;
    }

    public String getChildInstanceRegionId() {
        return childInstanceRegionId;
    }

    public void setChildInstanceRegionId(String childInstanceRegionId) {
        this.childInstanceRegionId = childInstanceRegionId;
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

    public String getChildInstanceType() {
        return childInstanceType;
    }

    public void setChildInstanceType(String childInstanceType) {
        this.childInstanceType = childInstanceType;
    }

    public String getChildInstanceOwnerId() {
        return childInstanceOwnerId;
    }

    public void setChildInstanceOwnerId(String childInstanceOwnerId) {
        this.childInstanceOwnerId = childInstanceOwnerId;
    }

    public String getChildInstanceId() {
        return childInstanceId;
    }

    public void setChildInstanceId(String childInstanceId) {
        this.childInstanceId = childInstanceId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("resourceOwnerId", resourceOwnerId)
                .append("cenId", cenId)
                .append("childInstanceRegionId", childInstanceRegionId)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("childInstanceType", childInstanceType)
                .append("childInstanceOwnerId", childInstanceOwnerId)
                .append("childInstanceId", childInstanceId)
                .toString();
    }
}
