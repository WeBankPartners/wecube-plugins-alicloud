package com.webank.wecube.plugins.alicloud.dto.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.AttachDiskResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreAttachDiskResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreAttachDiskResponseDto, AttachDiskResponse> {
    private String requestId;

    public CoreAttachDiskResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
