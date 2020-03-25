package com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup;

import com.aliyuncs.ecs.model.v20140526.DeleteSecurityGroupResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreDeleteSecurityGroupResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreDeleteSecurityGroupResponseDto, DeleteSecurityGroupResponse> {

    private String requestId;

    public CoreDeleteSecurityGroupResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
