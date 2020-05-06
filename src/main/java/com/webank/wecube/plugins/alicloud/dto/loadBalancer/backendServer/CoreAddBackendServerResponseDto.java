package com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer;

import com.aliyuncs.slb.model.v20140515.CreateVServerGroupResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * @author howechen
 */
public class CoreAddBackendServerResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreAddBackendServerResponseDto, CreateVServerGroupResponse> {

    private String requestId;
    @JsonProperty(value = "vServerGroupId")
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

    public String getvServerGroupId() {
        return vServerGroupId;
    }

    public void setvServerGroupId(String vServerGroupId) {
        this.vServerGroupId = vServerGroupId;
    }

    public List<CreateVServerGroupResponse.BackendServer> getBackendServers() {
        return backendServers;
    }

    public void setBackendServers(List<CreateVServerGroupResponse.BackendServer> backendServers) {
        this.backendServers = backendServers;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .append("vServerGroupId", vServerGroupId)
                .append("backendServers", backendServers)
                .toString();
    }
}
