package com.webank.wecube.plugins.alicloud.dto.ecs.vm;

import com.aliyuncs.ecs.model.v20140526.ModifyInstanceAttributeRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author howechen
 */
public class CoreModifyInstanceAttributeRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<ModifyInstanceAttributeRequest> {
    @NotEmpty(message = "securityGroupId field is mandatory.")
    private String securityGroupId;

    private String resourceOwnerId;
    private String recyclable;
    private String description;
    private String deletionProtection;
    private String userData;
    private String password;
    private String hostName;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String creditSpecification;
    private String ownerId;
    private List<String> securityGroupIdss;
    @NotEmpty(message = "instanceId field is mandatory.")
    private String instanceId;
    private String instanceName;

    public CoreModifyInstanceAttributeRequestDto() {
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

    public String getRecyclable() {
        return recyclable;
    }

    public void setRecyclable(String recyclable) {
        this.recyclable = recyclable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeletionProtection() {
        return deletionProtection;
    }

    public void setDeletionProtection(String deletionProtection) {
        this.deletionProtection = deletionProtection;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
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

    public String getCreditSpecification() {
        return creditSpecification;
    }

    public void setCreditSpecification(String creditSpecification) {
        this.creditSpecification = creditSpecification;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getSecurityGroupIdss() {
        return securityGroupIdss;
    }

    public void setSecurityGroupIdss(List<String> securityGroupIdss) {
        this.securityGroupIdss = securityGroupIdss;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("securityGroupId", securityGroupId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("recyclable", recyclable)
                .append("description", description)
                .append("deletionProtection", deletionProtection)
                .append("userData", userData)
                .append("password", password)
                .append("hostName", hostName)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("creditSpecification", creditSpecification)
                .append("ownerId", ownerId)
                .append("securityGroupIdss", securityGroupIdss)
                .append("instanceId", instanceId)
                .append("instanceName", instanceName)
                .toString();
    }
}
