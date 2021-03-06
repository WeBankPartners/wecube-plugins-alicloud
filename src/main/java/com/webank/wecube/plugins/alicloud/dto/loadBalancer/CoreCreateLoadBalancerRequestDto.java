package com.webank.wecube.plugins.alicloud.dto.loadBalancer;

import com.aliyuncs.slb.model.v20140515.CreateLoadBalancerRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import com.webank.wecube.plugins.alicloud.service.loadBalancer.LoadBalancerPayType;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreCreateLoadBalancerRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateLoadBalancerRequest> {

    private String loadBalancerId;
    private String loadBalancerProtocol;

    private String resourceOwnerId;
    private String clientToken;
    private String addressIPVersion = "ipv4";
    private String masterZoneId;
    private String duration;
    private String resourceGroupId;
    private String loadBalancerName;
    @NotEmpty(message = "addressType field is mandatory.")
    private String addressType;
    private String slaveZoneId;
    private String deleteProtection = "off";
    private String loadBalancerSpec;
    private String autoPay = "true";
    private String address;
    private String resourceOwnerAccount;
    private String bandwidth;
    private String ownerAccount;
    private String ownerId;
    @JsonProperty("vSwitchId")
    private String vSwitchId;
    private String internetChargeType;
    private String vpcId;
    private String payType;
    private String pricingCycle;

    public CoreCreateLoadBalancerRequestDto() {
    }

    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    public void setLoadBalancerId(String loadBalancerId) {
        this.loadBalancerId = loadBalancerId;
    }

    public String getLoadBalancerProtocol() {
        return loadBalancerProtocol;
    }

    public void setLoadBalancerProtocol(String loadBalancerProtocol) {
        this.loadBalancerProtocol = loadBalancerProtocol;
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

    public String getAddressIPVersion() {
        return addressIPVersion;
    }

    public void setAddressIPVersion(String addressIPVersion) {
        this.addressIPVersion = addressIPVersion;
    }

    public String getMasterZoneId() {
        return masterZoneId;
    }

    public void setMasterZoneId(String masterZoneId) {
        this.masterZoneId = masterZoneId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    public String getLoadBalancerName() {
        return loadBalancerName;
    }

    public void setLoadBalancerName(String loadBalancerName) {
        this.loadBalancerName = loadBalancerName;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getSlaveZoneId() {
        return slaveZoneId;
    }

    public void setSlaveZoneId(String slaveZoneId) {
        this.slaveZoneId = slaveZoneId;
    }

    public String getDeleteProtection() {
        return deleteProtection;
    }

    public void setDeleteProtection(String deleteProtection) {
        this.deleteProtection = deleteProtection;
    }

    public String getLoadBalancerSpec() {
        return loadBalancerSpec;
    }

    public void setLoadBalancerSpec(String loadBalancerSpec) {
        this.loadBalancerSpec = loadBalancerSpec;
    }

    public String getAutoPay() {
        return autoPay;
    }

    public void setAutoPay(String autoPay) {
        this.autoPay = autoPay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getVSwitchId() {
        return vSwitchId;
    }

    public void setVSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    public String getInternetChargeType() {
        return internetChargeType;
    }

    public void setInternetChargeType(String internetChargeType) {
        this.internetChargeType = internetChargeType;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPricingCycle() {
        return pricingCycle;
    }

    public void setPricingCycle(String pricingCycle) {
        this.pricingCycle = pricingCycle;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("loadBalancerId", loadBalancerId)
                .append("loadBalancerProtocol", loadBalancerProtocol)
                .append("resourceOwnerId", resourceOwnerId)
                .append("clientToken", clientToken)
                .append("addressIPVersion", addressIPVersion)
                .append("masterZoneId", masterZoneId)
                .append("duration", duration)
                .append("resourceGroupId", resourceGroupId)
                .append("loadBalancerName", loadBalancerName)
                .append("addressType", addressType)
                .append("slaveZoneId", slaveZoneId)
                .append("deleteProtection", deleteProtection)
                .append("loadBalancerSpec", loadBalancerSpec)
                .append("autoPay", autoPay)
                .append("address", address)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("bandwidth", bandwidth)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("vSwitchId", vSwitchId)
                .append("internetChargeType", internetChargeType)
                .append("vpcId", vpcId)
                .append("payType", payType)
                .append("pricingCycle", pricingCycle)
                .toString();
    }

    @Override
    public void adaptToAliCloud() throws PluginException {
        if (!StringUtils.isEmpty(this.getAddressType()) && StringUtils.equalsIgnoreCase(AddressType.internet.toString(), this.getAddressType())) {
            this.setAddress(null);
            this.setVSwitchId(null);
        }

        if (StringUtils.isNotEmpty(payType)) {
            if (EnumUtils.isValidEnumIgnoreCase(LoadBalancerPayType.class, payType)) {
                payType = EnumUtils.getEnumIgnoreCase(LoadBalancerPayType.class, payType).toString();
            } else {
                if (StringUtils.equalsIgnoreCase("postpaid", payType)) {
                    payType = LoadBalancerPayType.PayOnDemand.toString();
                } else if (StringUtils.equalsIgnoreCase("prepaid", payType)) {
                    payType = LoadBalancerPayType.PrePay.toString();
                } else {
                    throw new PluginException(String.format("Invalid given payType: [%s]", payType));
                }
            }
        }
    }

    private enum AddressType {
        // internet
        internet,
        // intranet
        intranet
    }
}
