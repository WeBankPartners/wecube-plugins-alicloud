package com.webank.wecube.plugins.alicloud.dto.cbn.cen;

import com.aliyuncs.cbn.model.v20170912.DetachCenChildInstanceResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreDetachCenChildResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreDetachCenChildResponseDto, DetachCenChildInstanceResponse> {
    private String requestId;

    public CoreDetachCenChildResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
