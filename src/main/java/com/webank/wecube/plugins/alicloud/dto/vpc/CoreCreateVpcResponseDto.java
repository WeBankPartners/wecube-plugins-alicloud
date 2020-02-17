package com.webank.wecube.plugins.alicloud.dto.vpc;

import com.aliyuncs.vpc.model.v20160428.CreateVpcResponse;
import com.aliyuncs.vpc.model.v20160428.DescribeVpcsResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateVpcResponseDto {
    private String requestId;
    private String vpcId;
    private String vRouterId;
    private String routeTableId;
    private String resourceGroupId;
    private String cidrBlock;
    private String vpcName;

    public static CoreCreateVpcResponseDto fromSdk(CreateVpcResponse createVpcResponse) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(createVpcResponse, CoreCreateVpcResponseDto.class);
    }

    public static CoreCreateVpcResponseDto fromSdk(DescribeVpcsResponse.Vpc createVpcResponse) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(createVpcResponse, CoreCreateVpcResponseDto.class);
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

    public String getvRouterId() {
        return vRouterId;
    }

    public void setvRouterId(String vRouterId) {
        this.vRouterId = vRouterId;
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
}
