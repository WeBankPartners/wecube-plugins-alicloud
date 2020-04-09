package com.webank.wecube.plugins.alicloud.dto.ecs.vm;

import com.aliyuncs.ecs.model.v20140526.ModifyInstanceAttributeRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;

import java.util.List;

/**
 * @author howechen
 */
public class CoreBindSecurityGroupRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<ModifyInstanceAttributeRequest> {
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
    private String instanceId;
    private String instanceName;

    public CoreBindSecurityGroupRequestDto() {
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
}
