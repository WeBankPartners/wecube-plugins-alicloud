package com.webank.wecube.plugins.alicloud.dto.vpc.nat;

import com.aliyuncs.vpc.model.v20160428.CreateNatGatewayResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * @author howechen
 */
public class CoreCreateNatGatewayResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateNatGatewayResponseDto, CreateNatGatewayResponse> {
    private String requestId;
    private String natGatewayId;
    private String forwardTableId;
    @JsonIgnore
    private List<String> forwardTableIds;
    private String snatTableId;
    @JsonIgnore
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .append("natGatewayId", natGatewayId)
                .append("forwardTableIds", forwardTableIds)
                .append("snatTableIds", snatTableIds)
                .append("bandwidthPackageIds", bandwidthPackageIds)
                .toString();
    }

    @Override
    public CoreCreateNatGatewayResponseDto fromSdk(CreateNatGatewayResponse response) {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        final CoreCreateNatGatewayResponseDto result = mapper.convertValue(response, CoreCreateNatGatewayResponseDto.class);

        if (!response.getForwardTableIds().isEmpty()) {
            result.setForwardTableId(response.getForwardTableIds().get(0));
        }

        if (!response.getSnatTableIds().isEmpty()) {
            result.setSnatTableId(response.getSnatTableIds().get(0));
        }
        return result;
    }

    public String getSnatTableId() {
        return snatTableId;
    }

    public void setSnatTableId(String snatTableId) {
        this.snatTableId = snatTableId;
    }

    public String getForwardTableId() {
        return forwardTableId;
    }

    public void setForwardTableId(String forwardTableId) {
        this.forwardTableId = forwardTableId;
    }
}
