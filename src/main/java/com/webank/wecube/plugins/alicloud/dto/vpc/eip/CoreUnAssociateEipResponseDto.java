package com.webank.wecube.plugins.alicloud.dto.vpc.eip;

import com.aliyuncs.vpc.model.v20160428.UnassociateEipAddressResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreUnAssociateEipResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreUnAssociateEipResponseDto, UnassociateEipAddressResponse> {
    private String requestId;

    public CoreUnAssociateEipResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
