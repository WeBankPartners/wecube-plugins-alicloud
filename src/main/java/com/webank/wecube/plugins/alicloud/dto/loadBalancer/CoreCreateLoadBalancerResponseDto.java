package com.webank.wecube.plugins.alicloud.dto.loadBalancer;

import com.aliyuncs.slb.model.v20140515.CreateLoadBalancerResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author howechen
 */
public class CoreCreateLoadBalancerResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateLoadBalancerResponseDto, CreateLoadBalancerResponse> {

    private String requestId;
    private String loadBalancerId;
    private String resourceGroupId;
    private String address;
    private String loadBalancerName;
    private String vpcId;
    private String vSwitchId;
    private String networkType;
    private String orderId;
    private String addressIPVersion;

    public CoreCreateLoadBalancerResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    public void setLoadBalancerId(String loadBalancerId) {
        this.loadBalancerId = loadBalancerId;
    }

    public String getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLoadBalancerName() {
        return loadBalancerName;
    }

    public void setLoadBalancerName(String loadBalancerName) {
        this.loadBalancerName = loadBalancerName;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getVSwitchId() {
        return vSwitchId;
    }

    public void setVSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAddressIPVersion() {
        return addressIPVersion;
    }

    public void setAddressIPVersion(String addressIPVersion) {
        this.addressIPVersion = addressIPVersion;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .append("loadBalancerId", loadBalancerId)
                .append("resourceGroupId", resourceGroupId)
                .append("address", address)
                .append("loadBalancerName", loadBalancerName)
                .append("vpcId", vpcId)
                .append("vSwitchId", vSwitchId)
                .append("networkType", networkType)
                .append("orderId", orderId)
                .append("addressIPVersion", addressIPVersion)
                .toString();
    }
}
