package com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup;

import com.aliyuncs.ecs.model.v20140526.CreateSecurityGroupRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author howechen
 */
public class CoreCreateSecurityGroupRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateSecurityGroupRequest> {
    private String securityGroupId;

    private String resourceOwnerId;
    private String clientToken;
    private String description;
    @NotEmpty(message = "securityGroupName field is mandatory")
    private String securityGroupName;
    private String resourceGroupId;
    private List<CreateSecurityGroupRequest.Tag> tags;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    private String securityGroupType;
    @NotEmpty(message = "vpcId field is mandatory")
    private String vpcId;

    public CoreCreateSecurityGroupRequestDto() {
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
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

    public String getSecurityGroupName() {
        return securityGroupName;
    }

    public void setSecurityGroupName(String securityGroupName) {
        this.securityGroupName = securityGroupName;
    }

    public String getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    public List<CreateSecurityGroupRequest.Tag> getTags() {
        return tags;
    }

    public void setTags(List<CreateSecurityGroupRequest.Tag> tags) {
        this.tags = tags;
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

    public String getSecurityGroupType() {
        return securityGroupType;
    }

    public void setSecurityGroupType(String securityGroupType) {
        this.securityGroupType = securityGroupType;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("securityGroupId", securityGroupId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("clientToken", clientToken)
                .append("description", description)
                .append("securityGroupName", securityGroupName)
                .append("resourceGroupId", resourceGroupId)
                .append("tags", tags)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("securityGroupType", securityGroupType)
                .append("vpcId", vpcId)
                .toString();
    }
}
