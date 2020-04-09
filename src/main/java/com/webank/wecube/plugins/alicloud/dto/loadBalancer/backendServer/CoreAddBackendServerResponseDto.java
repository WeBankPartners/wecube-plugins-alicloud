package com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer;

import com.aliyuncs.slb.model.v20140515.CreateVServerGroupResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

import java.util.List;

/**
 * @author howechen
 */
public class CoreAddBackendServerResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreAddBackendServerResponseDto, CreateVServerGroupResponse> {

    private String requestId;
    private String vServerGroupId;
    private List<CreateVServerGroupResponse.BackendServer> backendServers;

    public CoreAddBackendServerResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getVServerGroupId() {
        return vServerGroupId;
    }

    public void setVServerGroupId(String vServerGroupId) {
        this.vServerGroupId = vServerGroupId;
    }

    public List<CreateVServerGroupResponse.BackendServer> getBackendServers() {
        return backendServers;
    }

    public void setBackendServers(List<CreateVServerGroupResponse.BackendServer> backendServers) {
        this.backendServers = backendServers;
    }
}
