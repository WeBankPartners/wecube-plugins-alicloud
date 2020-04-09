package com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry;

import com.aliyuncs.vpc.model.v20160428.DeleteRouteEntryRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;

import java.util.List;

/**
 * @author howechen
 */
public class CoreDeleteRouteEntryRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<DeleteRouteEntryRequest> {

    private String resourceOwnerId;
    private String nextHopId;
    private String routeTableId;
    private String resourceOwnerAccount;
    private String destinationCidrBlock;
    private String ownerAccount;
    private String ownerId;
    private String routeEntryId;
    private List<DeleteRouteEntryRequest.NextHopList> nextHopLists;


    public CoreDeleteRouteEntryRequestDto() {
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getNextHopId() {
        return nextHopId;
    }

    public void setNextHopId(String nextHopId) {
        this.nextHopId = nextHopId;
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

    public String getRouteEntryId() {
        return routeEntryId;
    }

    public void setRouteEntryId(String routeEntryId) {
        this.routeEntryId = routeEntryId;
    }

    public List<DeleteRouteEntryRequest.NextHopList> getNextHopLists() {
        return nextHopLists;
    }

    public void setNextHopLists(List<DeleteRouteEntryRequest.NextHopList> nextHopLists) {
        this.nextHopLists = nextHopLists;
    }
}
