package com.webank.wecube.plugins.alicloud.dto.vpc.vswitch;

import com.aliyuncs.vpc.model.v20160428.CreateVSwitchResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

import java.lang.reflect.ParameterizedType;

/**
 * @author howechen
 */
public class CoreCreateVSwitchResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateVSwitchResponseDto, CreateVSwitchResponse> {
    private String routeTableId;

    private String requestId;
    @JsonProperty(value = "vSwitchId")
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

    @JsonProperty(value = "vSwitchId")
    public String getVSwitchId() {
        return vSwitchId;
    }

    @JsonProperty(value = "vSwitchId")
    public void setVSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    @Override
    public CoreCreateVSwitchResponseDto fromSdk(CreateVSwitchResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final CoreCreateVSwitchResponseDto result = mapper.convertValue(response, this.getClass());
        result.setVSwitchId(response.getVSwitchId());
        return result;
    }
}
