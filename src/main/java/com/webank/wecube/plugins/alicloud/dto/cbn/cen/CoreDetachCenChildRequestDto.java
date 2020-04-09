package com.webank.wecube.plugins.alicloud.dto.cbn.cen;

import com.aliyuncs.cbn.model.v20170912.DetachCenChildInstanceRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;

/**
 * @author howechen
 */
public class CoreDetachCenChildRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<DetachCenChildInstanceRequest> {
    private String resourceOwnerId;
    private String cenId;
    private String cenOwnerId;
    private String childInstanceRegionId;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    private String childInstanceType;
    private String childInstanceOwnerId;
    private String childInstanceId;

    public CoreDetachCenChildRequestDto() {
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

    public String getCenOwnerId() {
        return cenOwnerId;
    }

    public void setCenOwnerId(String cenOwnerId) {
        this.cenOwnerId = cenOwnerId;
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
}
