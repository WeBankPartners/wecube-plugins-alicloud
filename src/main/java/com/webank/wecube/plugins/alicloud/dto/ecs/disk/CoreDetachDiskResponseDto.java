package com.webank.wecube.plugins.alicloud.dto.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.DetachDiskResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreDetachDiskResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreDetachDiskResponseDto, DetachDiskResponse> {

    private String requestId;

    public CoreDetachDiskResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
