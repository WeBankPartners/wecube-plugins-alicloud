package com.webank.wecube.plugins.alicloud.dto.vpc.eip;

import com.aliyuncs.vpc.model.v20160428.AllocateEipAddressResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author howechen
 */
public class CoreAllocateEipResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreAllocateEipResponseDto, AllocateEipAddressResponse> {

    private String requestId;
    private String allocationId;
    private String cbpId;
    private String cbpName;
    private String eipAddress;
    private String orderId;
    private String resourceGroupId;

    public CoreAllocateEipResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String allocationId) {
        this.allocationId = allocationId;
    }

    public String getEipAddress() {
        return eipAddress;
    }

    public void setEipAddress(String eipAddress) {
        this.eipAddress = eipAddress;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getResourceGroupId() {
        return resourceGroupId;
    }

    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }


    public String getCbpId() {
        return cbpId;
    }

    public void setCbpId(String cbpId) {
        this.cbpId = cbpId;
    }

    public String getCbpName() {
        return cbpName;
    }

    public void setCbpName(String cbpName) {
        this.cbpName = cbpName;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .append("allocationId", allocationId)
                .append("cbpId", cbpId)
                .append("cbpName", cbpName)
                .append("eipAddress", eipAddress)
                .append("orderId", orderId)
                .append("resourceGroupId", resourceGroupId)
                .toString();
    }


}
