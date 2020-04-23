package com.webank.wecube.plugins.alicloud.dto.vpc.eip;

import com.aliyuncs.vpc.model.v20160428.AllocateEipAddressRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author howechen
 */
public class CoreAllocateEipRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<AllocateEipAddressRequest> {
    private String allocationId;

    private String resourceOwnerId;
    private String clientToken;
    private String iSP;
    private String resourceGroupId;
    private String netmode;
    private String instanceChargeType;
    private String period;
    private String autoPay = "true";
    private String resourceOwnerAccount;
    private String bandwidth;
    private String ownerAccount;
    private String ownerId;
    private String activityId;
    private String internetChargeType;
    private String pricingCycle;

    public CoreAllocateEipRequestDto() {
    }

    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String allocationId) {
        this.allocationId = allocationId;
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

    public String getISP() {
        return iSP;
    }

    public void setISP(String iSP) {
        this.iSP = iSP;
    }

    public String getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    public String getNetmode() {
        return netmode;
    }

    public void setNetmode(String netmode) {
        this.netmode = netmode;
    }

    public String getInstanceChargeType() {
        return instanceChargeType;
    }

    public void setInstanceChargeType(String instanceChargeType) {
        this.instanceChargeType = instanceChargeType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getAutoPay() {
        return autoPay;
    }

    public void setAutoPay(String autoPay) {
        this.autoPay = autoPay;
    }

    public String getResourceOwnerAccount() {
        return resourceOwnerAccount;
    }

    public void setResourceOwnerAccount(String resourceOwnerAccount) {
        this.resourceOwnerAccount = resourceOwnerAccount;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getInternetChargeType() {
        return internetChargeType;
    }

    public void setInternetChargeType(String internetChargeType) {
        this.internetChargeType = internetChargeType;
    }

    public String getPricingCycle() {
        return pricingCycle;
    }

    public void setPricingCycle(String pricingCycle) {
        this.pricingCycle = pricingCycle;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("allocationId", allocationId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("clientToken", clientToken)
                .append("iSP", iSP)
                .append("resourceGroupId", resourceGroupId)
                .append("netmode", netmode)
                .append("instanceChargeType", instanceChargeType)
                .append("period", period)
                .append("autoPay", autoPay)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("bandwidth", bandwidth)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("activityId", activityId)
                .append("internetChargeType", internetChargeType)
                .append("pricingCycle", pricingCycle)
                .toString();
    }
}
