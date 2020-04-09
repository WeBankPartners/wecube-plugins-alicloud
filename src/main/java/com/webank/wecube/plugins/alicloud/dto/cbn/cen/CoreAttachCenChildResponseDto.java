package com.webank.wecube.plugins.alicloud.dto.cbn.cen;

import com.aliyuncs.cbn.model.v20170912.AttachCenChildInstanceResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreAttachCenChildResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreAttachCenChildResponseDto, AttachCenChildInstanceResponse> {

    private String requestId;

    public CoreAttachCenChildResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
