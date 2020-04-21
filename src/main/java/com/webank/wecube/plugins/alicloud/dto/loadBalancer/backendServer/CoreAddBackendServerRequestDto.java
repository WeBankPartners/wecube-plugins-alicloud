package com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer;

import com.aliyuncs.slb.model.v20140515.CreateVServerGroupRequest;
import com.aliyuncs.vpc.model.v20160428.CreateVSwitchRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class CoreAddBackendServerRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateVServerGroupRequest> {

    // fields from core
    @NotEmpty(message = "hostIds field is mandatory.")
    private String hostIds;
    @NotEmpty(message = "hostPorts field is mandatory.")
    private String hostPorts;

    // alicloud fields
    private String listenerPort;
    private String listenerProtocol;
    private String bandwidth;
    private String serverId;

    private String resourceOwnerId;
    @JsonIgnore
    private String backendServers;
    private String resourceOwnerAccount;
    private String ownerAccount;
    private String ownerId;
    private String vServerGroupName;
    private String loadBalancerId;

    public CoreAddBackendServerRequestDto() {
    }

    public String getListenerPort() {
        return listenerPort;
    }

    public void setListenerPort(String listenerPort) {
        this.listenerPort = listenerPort;
    }

    public String getListenerProtocol() {
        return listenerProtocol;
    }

    public void setListenerProtocol(String listenerProtocol) {
        this.listenerProtocol = listenerProtocol;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
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

    public String getvServerGroupName() {
        return vServerGroupName;
    }

    public void setvServerGroupName(String vServerGroupName) {
        this.vServerGroupName = vServerGroupName;
    }

    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    public void setLoadBalancerId(String loadBalancerId) {
        this.loadBalancerId = loadBalancerId;
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

    @Override
    public CreateVServerGroupRequest toSdk() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return mapper.convertValue(this, CreateVServerGroupRequest.class);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("hostIds", hostIds)
                .append("hostPorts", hostPorts)
                .append("listenerPort", listenerPort)
                .append("listenerProtocol", listenerProtocol)
                .append("bandwidth", bandwidth)
                .append("serverId", serverId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("backendServers", backendServers)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ownerAccount", ownerAccount)
                .append("ownerId", ownerId)
                .append("vServerGroupName", vServerGroupName)
                .append("loadBalancerId", loadBalancerId)
                .toString();
    }
}
