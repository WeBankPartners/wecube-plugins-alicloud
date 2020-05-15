package com.webank.wecube.plugins.alicloud.dto.redis;

import com.aliyuncs.r_kvstore.model.v20150101.ModifySecurityGroupConfigurationRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreModifySecurityGroupRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<ModifySecurityGroupConfigurationRequest> {

    private String resourceOwnerId;
    @NotEmpty(message = "securityGroupId field is mandatory")
    private String securityGroupId;
    private String securityToken;
    @NotEmpty(message = "instanceId field is mandatory")
    @JsonProperty(value = "instanceId")
    private String dBInstanceId;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;

    public CoreModifySecurityGroupRequestDto() {
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getdBInstanceId() {
        return dBInstanceId;
    }

    public void setdBInstanceId(String dBInstanceId) {
        this.dBInstanceId = dBInstanceId;
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

    @Override
    public void adaptToAliCloud() throws PluginException {
        if (StringUtils.isNotEmpty(securityGroupId)) {
            securityGroupId = PluginStringUtils.removeSquareBracket(securityGroupId);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("resourceOwnerId", resourceOwnerId)
                .append("securityGroupId", securityGroupId)
                .append("securityToken", securityToken)
                .append("dBInstanceId", dBInstanceId)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .toString();
    }
}
