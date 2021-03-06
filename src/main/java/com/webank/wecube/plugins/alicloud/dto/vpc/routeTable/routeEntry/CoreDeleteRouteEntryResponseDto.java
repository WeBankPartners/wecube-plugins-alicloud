package com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry;

import com.aliyuncs.vpc.model.v20160428.DeleteRouteEntryResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author howechen
 */
public class CoreDeleteRouteEntryResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreDeleteRouteEntryResponseDto, DeleteRouteEntryResponse> {
    private String requestId;

    public CoreDeleteRouteEntryResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .toString();
    }
}
