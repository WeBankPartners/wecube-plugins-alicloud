package com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer;

import com.aliyuncs.slb.model.v20140515.RemoveVServerGroupBackendServersRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreRemoveBackendServerRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<RemoveVServerGroupBackendServersRequest> {
    // if delete listener
    private String deleteListener;

    // fields from core
    @NotEmpty(message = "hostIds field is mandatory.")
    private String hostIds;
    @NotEmpty(message = "hostPorts field is mandatory.")
    private String hostPorts;

    // alicloud fields
    @NotEmpty(message = "listenerPort field is mandatory")
    private String listenerPort;
    private String loadBalancerId;
    private String listenerProtocol;

    private String resourceOwnerId;
    @JsonIgnore
    private String backendServers;
    @NotEmpty(message = "vServerGroupId field is mandatory.")
    @JsonProperty(value = "vServerGroupId")
    private String vServerGroupId;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;

    public CoreRemoveBackendServerRequestDto() {
    }

    public String getListenerPort() {
        return listenerPort;
    }

    public void setListenerPort(String listenerPort) {
        this.listenerPort = listenerPort;
    }

    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    public void setLoadBalancerId(String loadBalancerId) {
        this.loadBalancerId = loadBalancerId;
    }

    public String getListenerProtocol() {
        return listenerProtocol;
    }

    public void setListenerProtocol(String listenerProtocol) {
        this.listenerProtocol = listenerProtocol;
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getBackendServers() {
        return backendServers;
    }

    public void setBackendServers(String backendServers) {
        this.backendServers = backendServers;
    }

    public String getvServerGroupId() {
        return vServerGroupId;
    }

    public void setvServerGroupId(String vServerGroupId) {
        this.vServerGroupId = vServerGroupId;
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

    public String getHostIds() {
        return hostIds;
    }

    public void setHostIds(String hostIds) {
        this.hostIds = hostIds;
    }

    public String getHostPorts() {
        return hostPorts;
    }

    public void setHostPorts(String hostPorts) {
        this.hostPorts = hostPorts;
    }

    public String getDeleteListener() {
        return deleteListener;
    }

    public void setDeleteListener(String deleteListener) {
        this.deleteListener = deleteListener;
    }

    public boolean ifDeleteListener() {
        return StringUtils.equalsIgnoreCase("Y", this.getDeleteListener());
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("deleteListener", deleteListener)
                .append("hostIds", hostIds)
                .append("hostPorts", hostPorts)
                .append("listenerPort", listenerPort)
                .append("loadBalancerId", loadBalancerId)
                .append("listenerProtocol", listenerProtocol)
                .append("resourceOwnerId", resourceOwnerId)
                .append("backendServers", backendServers)
                .append("vServerGroupId", vServerGroupId)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .toString();
    }


}
