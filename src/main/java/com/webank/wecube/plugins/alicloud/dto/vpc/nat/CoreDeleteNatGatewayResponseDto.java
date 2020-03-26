package com.webank.wecube.plugins.alicloud.dto.vpc.nat;

import com.aliyuncs.vpc.model.v20160428.DeleteNatGatewayResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreDeleteNatGatewayResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreDeleteNatGatewayResponseDto, DeleteNatGatewayResponse> {
    private String requestId;

    public CoreDeleteNatGatewayResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
