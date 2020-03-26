package com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer;

import com.aliyuncs.slb.model.v20140515.RemoveVServerGroupBackendServersResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

import java.util.List;

/**
 * @author howechen
 */
public class CoreRemoveBackendServerResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreRemoveBackendServerResponseDto, RemoveVServerGroupBackendServersResponse> {
    private String requestId;
    private String vServerGroupId;
    private List<RemoveVServerGroupBackendServersResponse.BackendServer> backendServers;

    public CoreRemoveBackendServerResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getvServerGroupId() {
        return vServerGroupId;
    }

    public void setvServerGroupId(String vServerGroupId) {
        this.vServerGroupId = vServerGroupId;
    }

    public List<RemoveVServerGroupBackendServersResponse.BackendServer> getBackendServers() {
        return backendServers;
    }

    public void setBackendServers(List<RemoveVServerGroupBackendServersResponse.BackendServer> backendServers) {
        this.backendServers = backendServers;
    }
}
