package com.webank.wecube.plugins.alicloud.dto.rds.securityIP;

import com.aliyuncs.rds.model.v20140815.ModifySecurityIpsResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreModifySecurityIPsResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreModifySecurityIPsResponseDto, ModifySecurityIpsResponse> {
    private String requestId;
    private String taskId;

    public CoreModifySecurityIPsResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
