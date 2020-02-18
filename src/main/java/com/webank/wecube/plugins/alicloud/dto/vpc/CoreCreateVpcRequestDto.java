package com.webank.wecube.plugins.alicloud.dto.vpc;

import com.aliyuncs.vpc.model.v20160428.CreateVpcRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author howechen
 */
public class CoreCreateVpcRequestDto {
    String vpcId;
    String regionId;
    private Long resourceOwnerId;
    private String clientToken;
    private Boolean enableIpv6;
    private String description;
    private String vpcName;
    private String resourceGroupId;
    private String userCidr;
    private Boolean dryRun;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private Long ownerId;
    private String ipv6CidrBlock;
    private String cidrBlock;

    public static CreateVpcRequest toSdk(CoreCreateVpcRequestDto coreCreateVpcRequestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(coreCreateVpcRequestDto, CreateVpcRequest.class);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("vpcId", vpcId)
                .append("regionId", regionId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("clientToken", clientToken)
                .append("enableIpv6", enableIpv6)
                .append("description", description)
                .append("vpcName", vpcName)
                .append("resourceGroupId", resourceGroupId)
                .append("userCidr", userCidr)
                .append("dryRun", dryRun)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("ipv6CidrBlock", ipv6CidrBlock)
                .append("cidrBlock", cidrBlock)
                .toString();
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Long getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(Long resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public Boolean getEnableIpv6() {
        return enableIpv6;
    }

    public void setEnableIpv6(Boolean enableIpv6) {
        this.enableIpv6 = enableIpv6;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVpcName() {
        return vpcName;
    }

    public void setVpcName(String vpcName) {
        this.vpcName = vpcName;
    }

    public String getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    public String getUserCidr() {
        return userCidr;
    }

    public void setUserCidr(String userCidr) {
        this.userCidr = userCidr;
    }

    public Boolean getDryRun() {
        return dryRun;
    }

    public void setDryRun(Boolean dryRun) {
        this.dryRun = dryRun;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getIpv6CidrBlock() {
        return ipv6CidrBlock;
    }

    public void setIpv6CidrBlock(String ipv6CidrBlock) {
        this.ipv6CidrBlock = ipv6CidrBlock;
    }

    public String getCidrBlock() {
        return cidrBlock;
    }

    public void setCidrBlock(String cidrBlock) {
        this.cidrBlock = cidrBlock;
    }
}
