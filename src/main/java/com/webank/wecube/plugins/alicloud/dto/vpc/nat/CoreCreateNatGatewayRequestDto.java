package com.webank.wecube.plugins.alicloud.dto.vpc.nat;

import com.aliyuncs.vpc.model.v20160428.CreateNatGatewayRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author howechen
 */
public class CoreCreateNatGatewayRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateNatGatewayRequest> {
    private String natGatewayId;

    private String resourceOwnerId;
    private String clientToken;
    private String description;
    private String spec;
    private String duration;
    private String natType;
    private List<CreateNatGatewayRequest.BandwidthPackage> bandwidthPackages;
    private String instanceChargeType;
    private String autoPay = "true";
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    private String vSwitchId;
    private String internetChargeType = "PayByTraffic";
    @NotEmpty(message = "vpcId field is mandatory")
    private String vpcId;
    private String name;
    private String pricingCycle;

    public CoreCreateNatGatewayRequestDto() {
    }

    public String getNatGatewayId() {
        return natGatewayId;
    }

    public void setNatGatewayId(String natGatewayId) {
        this.natGatewayId = natGatewayId;
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

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNatType() {
        return natType;
    }

    public void setNatType(String natType) {
        this.natType = natType;
    }

    public List<CreateNatGatewayRequest.BandwidthPackage> getBandwidthPackages() {
        return bandwidthPackages;
    }

    public void setBandwidthPackages(List<CreateNatGatewayRequest.BandwidthPackage> bandwidthPackages) {
        this.bandwidthPackages = bandwidthPackages;
    }

    public String getInstanceChargeType() {
        return instanceChargeType;
    }

    public void setInstanceChargeType(String instanceChargeType) {
        this.instanceChargeType = instanceChargeType;
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

    public String getvSwitchId() {
        return vSwitchId;
    }

    public void setvSwitchId(String vSwitchId) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                .append("natGatewayId", natGatewayId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("clientToken", clientToken)
                .append("description", description)
                .append("spec", spec)
                .append("duration", duration)
                .append("natType", natType)
                .append("bandwidthPackages", bandwidthPackages)
                .append("instanceChargeType", instanceChargeType)
                .append("autoPay", autoPay)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("vSwitchId", vSwitchId)
                .append("internetChargeType", internetChargeType)
                .append("vpcId", vpcId)
                .append("name", name)
                .append("pricingCycle", pricingCycle)
                .toString();
    }
}
