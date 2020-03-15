package com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer;

import com.aliyuncs.slb.model.v20140515.CreateVServerGroupRequest;

/**
 * @author howechen
 */
public class CoreAddBackendServerRequestDto extends CreateVServerGroupRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;


    private Integer listenerPort;
    private String listenerProtocol;
    private Integer bandwidth;
    private String serverId;


    public CoreAddBackendServerRequestDto() {
    }

    public String getIdentityParams() {
        return identityParams;
    }

    public void setIdentityParams(String identityParams) {
        this.identityParams = identityParams;
    }

    public String getCloudParams() {
        return cloudParams;
    }

    public void setCloudParams(String cloudParams) {
        this.cloudParams = cloudParams;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCallbackParameter() {
        return callbackParameter;
    }

    public void setCallbackParameter(String callbackParameter) {
        this.callbackParameter = callbackParameter;
    }

    public Integer getListenerPort() {
        return listenerPort;
    }

    public void setListenerPort(Integer listenerPort) {
        this.listenerPort = listenerPort;
    }

    public String getListenerProtocol() {
        return listenerProtocol;
    }

    public void setListenerProtocol(String listenerProtocol) {
        this.listenerProtocol = listenerProtocol;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Integer getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Integer bandwidth) {
        this.bandwidth = bandwidth;
    }
}
