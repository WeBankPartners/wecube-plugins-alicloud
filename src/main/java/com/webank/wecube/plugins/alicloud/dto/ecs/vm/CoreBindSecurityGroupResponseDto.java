package com.webank.wecube.plugins.alicloud.dto.ecs.vm;

import com.aliyuncs.ecs.model.v20140526.ModifyInstanceAttributeResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreBindSecurityGroupResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreBindSecurityGroupResponseDto, ModifyInstanceAttributeResponse> {
    private String requestId;

    public CoreBindSecurityGroupResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
