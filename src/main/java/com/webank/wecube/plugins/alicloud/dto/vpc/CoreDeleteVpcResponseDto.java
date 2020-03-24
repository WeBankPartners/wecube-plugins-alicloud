package com.webank.wecube.plugins.alicloud.dto.vpc;

import com.aliyuncs.vpc.model.v20160428.DeleteVpcResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreDeleteVpcResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreDeleteVpcResponseDto, DeleteVpcResponse> {
    private String requestId;

    public CoreDeleteVpcResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
