package com.webank.wecube.plugins.alicloud.dto.vpc.routeTable;

import com.aliyuncs.vpc.model.v20160428.AssociateRouteTableResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreAssociateRouteTableResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreAssociateRouteTableResponseDto, AssociateRouteTableResponse> {

    private String requestId;

    public CoreAssociateRouteTableResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
