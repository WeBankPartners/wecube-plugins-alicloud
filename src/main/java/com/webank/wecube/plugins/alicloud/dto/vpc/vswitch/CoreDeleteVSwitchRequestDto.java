package com.webank.wecube.plugins.alicloud.dto.vpc.vswitch;

import com.aliyuncs.vpc.model.v20160428.CreateVSwitchRequest;
import com.aliyuncs.vpc.model.v20160428.DeleteVSwitchRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreDeleteVSwitchRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<DeleteVSwitchRequest> {

    private Long resourceOwnerId;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private Long ownerId;
    @NotEmpty(message = "vSwitchId field is mandatory.")
    @JsonProperty(value = "vSwitchId")
    private String vSwitchId;

    public CoreDeleteVSwitchRequestDto() {
    }

    public Long getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(Long resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
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

    public String getVSwitchId() {
        return vSwitchId;
    }

    public void setVSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("resourceOwnerId", resourceOwnerId)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("vSwitchId", vSwitchId)
                .toString();
    }

    @Override
    public DeleteVSwitchRequest toSdk() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return mapper.convertValue(this, DeleteVSwitchRequest.class);
    }
}
