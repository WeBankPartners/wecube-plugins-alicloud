package com.webank.wecube.plugins.alicloud.dto.vpc.vswitch;

import com.aliyuncs.vpc.model.v20160428.CreateVSwitchResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.reflect.ParameterizedType;

/**
 * @author howechen
 */
public class CoreCreateVSwitchResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateVSwitchResponseDto, CreateVSwitchResponse> {
    private String routeTableId;
    private String requestId;
    private String vSwitchId;

    public CoreCreateVSwitchResponseDto() {
    }

    public String getRouteTableId() {
        return routeTableId;
    }

    public void setRouteTableId(String routeTableId) {
        this.routeTableId = routeTableId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getvSwitchId() {
        return vSwitchId;
    }

    public void setvSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("routeTableId", routeTableId)
                .append("requestId", requestId)
                .append("vSwitchId", vSwitchId)
                .toString();
    }

}
