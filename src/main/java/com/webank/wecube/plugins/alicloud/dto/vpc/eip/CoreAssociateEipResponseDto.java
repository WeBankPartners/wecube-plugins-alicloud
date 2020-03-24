package com.webank.wecube.plugins.alicloud.dto.vpc.eip;

import com.aliyuncs.vpc.model.v20160428.AssociateEipAddressResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreAssociateEipResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreAssociateEipResponseDto, AssociateEipAddressResponse> {
    private String requestId;

    public CoreAssociateEipResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
