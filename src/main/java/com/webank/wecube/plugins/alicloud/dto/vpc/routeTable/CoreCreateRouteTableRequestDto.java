package com.webank.wecube.plugins.alicloud.dto.vpc.routeTable;

import com.aliyuncs.vpc.model.v20160428.CreateRouteTableRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreCreateRouteTableRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateRouteTableRequest> {
    private String routeTableId;

    private String resourceOwnerId;
    private String clientToken;
    private String description;
    private String routeTableName;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    @NotEmpty(message = "vpcId field is mandatory.")
    private String vpcId;

    public CoreCreateRouteTableRequestDto() {
    }

    public String getRouteTableId() {
        return routeTableId;
    }

    public void setRouteTableId(String routeTableId) {
        this.routeTableId = routeTableId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRouteTableName() {
        return routeTableName;
    }

    public void setRouteTableName(String routeTableName) {
        this.routeTableName = routeTableName;
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

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("routeTableId", routeTableId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("clientToken", clientToken)
                .append("description", description)
                .append("routeTableName", routeTableName)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("vpcId", vpcId)
                .toString();
    }
}
