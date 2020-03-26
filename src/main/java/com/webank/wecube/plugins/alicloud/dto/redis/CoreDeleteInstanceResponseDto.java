package com.webank.wecube.plugins.alicloud.dto.redis;

import com.aliyuncs.r_kvstore.model.v20150101.DeleteInstanceResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreDeleteInstanceResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreDeleteInstanceResponseDto, DeleteInstanceResponse> {
    private String requestId;

    public CoreDeleteInstanceResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
