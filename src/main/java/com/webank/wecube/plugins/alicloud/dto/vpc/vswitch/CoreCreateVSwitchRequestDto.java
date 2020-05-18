package com.webank.wecube.plugins.alicloud.dto.vpc.vswitch;

import com.aliyuncs.vpc.model.v20160428.CreateVSwitchRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreCreateVSwitchRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateVSwitchRequest> {
    @JsonProperty(value = "vSwitchId")
    private String vSwitchId;

    private String resourceOwnerId;
    private String clientToken;
    private String description;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    private String ipv6CidrBlock;
    @NotEmpty(message = "vpcId field is mandatory.")
    private String vpcId;
    @NotEmpty(message = "vSwitchName field is mandatory.")
    @JsonProperty(value = "vSwitchName")
    private String vSwitchName;
    @NotEmpty(message = "cidrBlock field is mandatory.")
    private String cidrBlock;
    @NotEmpty(message = "zoneId field is mandatory.")
    private String zoneId;

    public CoreCreateVSwitchRequestDto() {
    }

    @JsonProperty(value = "vSwitchId")
    public String getvSwitchId() {
        return vSwitchId;
    }

    @JsonProperty(value = "vSwitchId")
    public void setvSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
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

    public String getIpv6CidrBlock() {
        return ipv6CidrBlock;
    }

    public void setIpv6CidrBlock(String ipv6CidrBlock) {
        this.ipv6CidrBlock = ipv6CidrBlock;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getvSwitchName() {
        return vSwitchName;
    }

    public void setvSwitchName(String vSwitchName) {
        this.vSwitchName = vSwitchName;
    }

    public String getCidrBlock() {
        return cidrBlock;
    }

    public void setCidrBlock(String cidrBlock) {
        this.cidrBlock = cidrBlock;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("vSwitchId", vSwitchId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("clientToken", clientToken)
                .append("description", description)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("ipv6CidrBlock", ipv6CidrBlock)
                .append("vpcId", vpcId)
                .append("vSwitchName", vSwitchName)
                .append("cidrBlock", cidrBlock)
                .append("zoneId", zoneId)
                .toString();
    }
}
