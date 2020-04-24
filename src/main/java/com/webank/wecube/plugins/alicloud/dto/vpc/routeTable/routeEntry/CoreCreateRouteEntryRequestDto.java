package com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry;

import com.aliyuncs.vpc.model.v20160428.CreateRouteEntryRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author howechen
 */
public class CoreCreateRouteEntryRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateRouteEntryRequest> {
    private String resourceOwnerId;
    private String routeEntryName;
    private String clientToken;
    @NotEmpty(message = "nextHopId field is mandatory")
    private String nextHopId;
    private String nextHopType;
    @NotEmpty(message = "routeTableId field is mandatory")
    private String routeTableId;
    private String resourceOwnerAccount;
    @NotEmpty(message = "destinationCidrBlock field is mandatory")
    private String destinationCidrBlock;
    private String ownerAccount;
    private String ownerId;
    private List<CreateRouteEntryRequest.NextHopList> nextHopLists;

    public CoreCreateRouteEntryRequestDto() {
    }


    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getRouteEntryName() {
        return routeEntryName;
    }

    public void setRouteEntryName(String routeEntryName) {
        this.routeEntryName = routeEntryName;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getNextHopId() {
        return nextHopId;
    }

    public void setNextHopId(String nextHopId) {
        this.nextHopId = nextHopId;
    }

    public String getNextHopType() {
        return nextHopType;
    }

    public void setNextHopType(String nextHopType) {
        this.nextHopType = nextHopType;
    }

    public String getRouteTableId() {
        return routeTableId;
    }

    public void setRouteTableId(String routeTableId) {
        this.routeTableId = routeTableId;
    }

    public String getResourceOwnerAccount() {
        return resourceOwnerAccount;
    }

    public void setResourceOwnerAccount(String resourceOwnerAccount) {
        this.resourceOwnerAccount = resourceOwnerAccount;
    }

    public String getDestinationCidrBlock() {
        return destinationCidrBlock;
    }

    public void setDestinationCidrBlock(String destinationCidrBlock) {
        this.destinationCidrBlock = destinationCidrBlock;
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

    public List<CreateRouteEntryRequest.NextHopList> getNextHopLists() {
        return nextHopLists;
    }

    public void setNextHopLists(List<CreateRouteEntryRequest.NextHopList> nextHopLists) {
        this.nextHopLists = nextHopLists;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("resourceOwnerId", resourceOwnerId)
                .append("routeEntryName", routeEntryName)
                .append("clientToken", clientToken)
                .append("nextHopId", nextHopId)
                .append("nextHopType", nextHopType)
                .append("routeTableId", routeTableId)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("destinationCidrBlock", destinationCidrBlock)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("nextHopLists", nextHopLists)
                .toString();
    }
}
