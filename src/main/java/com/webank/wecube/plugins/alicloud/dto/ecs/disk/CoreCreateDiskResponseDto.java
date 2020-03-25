package com.webank.wecube.plugins.alicloud.dto.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.CreateDiskResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreCreateDiskResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateDiskResponseDto, CreateDiskResponse> {
    private String requestId;
    private String diskId;

    public CoreCreateDiskResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }
}
