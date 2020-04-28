package com.webank.wecube.plugins.alicloud.dto.vpc;

import com.aliyuncs.vpc.model.v20160428.CreateVpcResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author howechen
 */
public class CoreCreateVpcResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateVpcResponseDto, CreateVpcResponse> {
    @JsonIgnore
    private String vRouterId;
    @JsonIgnore
    private String cidrBlock;
    @JsonIgnore
    private String vpcName;
    @JsonIgnore
    private String requestId;
    private String vpcId;
    private String routeTableId;
    @JsonIgnore
    private String resourceGroupId;

    public CoreCreateVpcResponseDto() {
    }

    public String getvRouterId() {
        return vRouterId;
    }

    public void setvRouterId(String vRouterId) {
        this.vRouterId = vRouterId;
    }

    public String getCidrBlock() {
        return cidrBlock;
    }

    public void setCidrBlock(String cidrBlock) {
        this.cidrBlock = cidrBlock;
    }

    public String getVpcName() {
        return vpcName;
    }

    public void setVpcName(String vpcName) {
        this.vpcName = vpcName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getRouteTableId() {
        return routeTableId;
    }

    public void setRouteTableId(String routeTableId) {
        this.routeTableId = routeTableId;
    }

    public String getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("vRouterId", vRouterId)
                .append("cidrBlock", cidrBlock)
                .append("vpcName", vpcName)
                .append("requestId", requestId)
                .append("vpcId", vpcId)
                .append("routeTableId", routeTableId)
                .append("resourceGroupId", resourceGroupId)
                .toString();
    }
}
