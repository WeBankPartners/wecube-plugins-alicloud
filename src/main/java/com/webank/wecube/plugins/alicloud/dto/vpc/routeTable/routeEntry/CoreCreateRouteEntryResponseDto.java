package com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry;

import com.aliyuncs.vpc.model.v20160428.CreateRouteEntryResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .toString();
    }
}
