package com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer;

import com.aliyuncs.slb.model.v20140515.RemoveVServerGroupBackendServersResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public CoreRemoveBackendServerResponseDto fromSdk(RemoveVServerGroupBackendServersResponse response) {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return mapper.convertValue(response, this.getClass());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .append("vServerGroupId", vServerGroupId)
                .append("backendServers", backendServers)
                .toString();
    }
}
