package com.webank.wecube.plugins.alicloud.dto.vpc.routeTable;

import com.aliyuncs.vpc.model.v20160428.CreateRouteTableResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author howechen
 */
public class CoreCreateRouteTableResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateRouteTableResponseDto, CreateRouteTableResponse> {

    private String requestId;
    private String routeTableId;

    public CoreCreateRouteTableResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRouteTableId() {
        return routeTableId;
    }

    public void setRouteTableId(String routeTableId) {
        this.routeTableId = routeTableId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .append("routeTableId", routeTableId)
                .toString();
    }
}
