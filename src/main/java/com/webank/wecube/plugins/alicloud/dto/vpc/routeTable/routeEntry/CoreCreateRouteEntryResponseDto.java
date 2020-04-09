package com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry;

import com.aliyuncs.vpc.model.v20160428.CreateRouteEntryResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreCreateRouteEntryResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateRouteEntryResponseDto, CreateRouteEntryResponse> {

    private String requestId;

    public CoreCreateRouteEntryResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
