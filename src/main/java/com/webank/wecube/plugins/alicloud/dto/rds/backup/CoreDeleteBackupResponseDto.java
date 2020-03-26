package com.webank.wecube.plugins.alicloud.dto.rds.backup;

import com.aliyuncs.rds.model.v20140815.DeleteBackupResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreDeleteBackupResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreDeleteBackupResponseDto, DeleteBackupResponse> {
    private String requestId;

    public CoreDeleteBackupResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
