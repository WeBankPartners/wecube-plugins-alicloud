package com.webank.wecube.plugins.alicloud.dto.rds.securityGroup;

import com.aliyuncs.rds.model.v20140815.ModifySecurityGroupConfigurationRequest;
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
public class CoreModifyDBSecurityGroupRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<ModifySecurityGroupConfigurationRequest> {

    private String resourceOwnerId;

    private String resourceOwnerAccount;

    @NotEmpty(message = "securityGroupId field is mandatory")
    private String securityGroupId;

    private String ownerId;

    @NotEmpty(message = "dBInstanceId field is mandatory")
    @JsonProperty(value = "dBInstanceId")
    private String dBInstanceId;

    public CoreModifyDBSecurityGroupRequestDto() {
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getResourceOwnerAccount() {
        return resourceOwnerAccount;
    }

    public void setResourceOwnerAccount(String resourceOwnerAccount) {
        this.resourceOwnerAccount = resourceOwnerAccount;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getdBInstanceId() {
        return dBInstanceId;
    }

    public void setdBInstanceId(String dBInstanceId) {
        this.dBInstanceId = dBInstanceId;
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
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("securityGroupId", securityGroupId)
                .append("ownerId", ownerId)
                .append("dBInstanceId", dBInstanceId)
                .toString();
    }
}
