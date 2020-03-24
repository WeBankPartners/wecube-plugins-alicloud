package com.webank.wecube.plugins.alicloud.dto.vpc.vswitch;

import com.aliyuncs.vpc.model.v20160428.CreateVSwitchResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

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

    public String getVSwitchId() {
        return vSwitchId;
    }

    public void setVSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }
}
