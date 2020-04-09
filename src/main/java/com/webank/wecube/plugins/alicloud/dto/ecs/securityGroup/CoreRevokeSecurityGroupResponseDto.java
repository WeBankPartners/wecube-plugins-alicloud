package com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup;

import com.aliyuncs.ecs.model.v20140526.RevokeSecurityGroupResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreRevokeSecurityGroupResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreRevokeSecurityGroupResponseDto, RevokeSecurityGroupResponse> {
    private String requestId;

    public CoreRevokeSecurityGroupResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
