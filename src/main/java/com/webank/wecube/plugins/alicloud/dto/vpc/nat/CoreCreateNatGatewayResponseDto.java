package com.webank.wecube.plugins.alicloud.dto.vpc.nat;

import com.aliyuncs.vpc.model.v20160428.CreateNatGatewayResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

import java.util.List;

/**
 * @author howechen
 */
public class CoreCreateNatGatewayResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateNatGatewayResponseDto, CreateNatGatewayResponse> {
    private String requestId;
    private String natGatewayId;
    private List<String> forwardTableIds;
    private List<String> snatTableIds;
    private List<String> bandwidthPackageIds;

    public CoreCreateNatGatewayResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getNatGatewayId() {
        return natGatewayId;
    }

    public void setNatGatewayId(String natGatewayId) {
        this.natGatewayId = natGatewayId;
    }

    public List<String> getForwardTableIds() {
        return forwardTableIds;
    }

    public void setForwardTableIds(List<String> forwardTableIds) {
        this.forwardTableIds = forwardTableIds;
    }

    public List<String> getSnatTableIds() {
        return snatTableIds;
    }

    public void setSnatTableIds(List<String> snatTableIds) {
        this.snatTableIds = snatTableIds;
    }

    public List<String> getBandwidthPackageIds() {
        return bandwidthPackageIds;
    }

    public void setBandwidthPackageIds(List<String> bandwidthPackageIds) {
        this.bandwidthPackageIds = bandwidthPackageIds;
    }
}
