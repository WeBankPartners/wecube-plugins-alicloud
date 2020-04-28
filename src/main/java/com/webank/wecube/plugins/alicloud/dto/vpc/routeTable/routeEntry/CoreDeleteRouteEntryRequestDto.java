package com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry;

import com.aliyuncs.vpc.model.v20160428.DeleteRouteEntryRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author howechen
 */
public class CoreDeleteRouteEntryRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<DeleteRouteEntryRequest> {

    private String resourceOwnerId;
    @NotEmpty(message = "nextHopId field is mandatory")
    private String nextHopId;
    @NotEmpty(message = "routeTableId field is mandatory")
    private String routeTableId;
    private String resourceOwnerAccount;
    @NotEmpty(message = "destinationCidrBlock field is mandatory")
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("resourceOwnerId", resourceOwnerId)
                .append("nextHopId", nextHopId)
                .append("routeTableId", routeTableId)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("destinationCidrBlock", destinationCidrBlock)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("routeEntryId", routeEntryId)
                .append("nextHopLists", nextHopLists)
                .toString();
    }
}
