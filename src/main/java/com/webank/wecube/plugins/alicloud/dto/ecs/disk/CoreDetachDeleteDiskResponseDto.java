package com.webank.wecube.plugins.alicloud.dto.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.DeleteDiskResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreDetachDeleteDiskResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreDetachDeleteDiskResponseDto, DeleteDiskResponse> {
    private String requestId;

    public CoreDetachDeleteDiskResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}