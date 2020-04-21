package com.webank.wecube.plugins.alicloud.dto.loadBalancer;

import com.aliyuncs.slb.model.v20140515.DeleteLoadBalancerRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreDeleteLoadBalancerRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<DeleteLoadBalancerRequest> {

    private String resourceOwnerId;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    @NotEmpty(message = "loadBalancerId field is mandatory.")
    private String loadBalancerId;

    public CoreDeleteLoadBalancerRequestDto() {
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

    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    public void setLoadBalancerId(String loadBalancerId) {
        this.loadBalancerId = loadBalancerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("resourceOwnerId", resourceOwnerId)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("loadBalancerId", loadBalancerId)
                .toString();
    }
}
