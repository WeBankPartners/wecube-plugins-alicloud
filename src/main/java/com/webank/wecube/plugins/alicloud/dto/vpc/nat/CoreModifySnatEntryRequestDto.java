package com.webank.wecube.plugins.alicloud.dto.vpc.nat;

import com.aliyuncs.vpc.model.v20160428.ModifySnatEntryRequest;
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
public class CoreModifySnatEntryRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<ModifySnatEntryRequest> {

    @NotEmpty(message = "natId field is mandatory")
    private String natId;

    private String resourceOwnerId;

    @NotEmpty(message = "snatIp field is mandatory")
    private String snatIp;

    @NotEmpty(message = "snatEntryId field is mandatory")
    private String snatEntryId;

    private String resourceOwnerAccount;

    private String ownerAccount;

    @NotEmpty(message = "snatTable id field is mandatory")
    private String snatTableId;

    private String ownerId;

    private String snatEntryName;

    public CoreModifySnatEntryRequestDto() {
    }


    public String getNatId() {
        return natId;
    }

    public void setNatId(String natId) {
        this.natId = natId;
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getSnatIp() {
        return snatIp;
    }

    public void setSnatIp(String snatIp) {
        this.snatIp = snatIp;
    }

    public String getSnatEntryId() {
        return snatEntryId;
    }

    public void setSnatEntryId(String snatEntryId) {
        this.snatEntryId = snatEntryId;
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

    public String getSnatTableId() {
        return snatTableId;
    }

    public void setSnatTableId(String snatTableId) {
        this.snatTableId = snatTableId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getSnatEntryName() {
        return snatEntryName;
    }

    public void setSnatEntryName(String snatEntryName) {
        this.snatEntryName = snatEntryName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("natId", natId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("snatIp", snatIp)
                .append("snatEntryId", snatEntryId)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("snatTableId", snatTableId)
                .append("ownerId", ownerId)
                .append("snatEntryName", snatEntryName)
                .toString();
    }

    @Override
    public void adaptToAliCloud() throws PluginException {
        if (StringUtils.isNotEmpty(snatIp)) {
            snatIp = PluginStringUtils.removeSquareBracket(snatIp);
        }
    }

}
