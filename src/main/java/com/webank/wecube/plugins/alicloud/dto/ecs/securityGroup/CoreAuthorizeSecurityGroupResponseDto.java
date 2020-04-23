package com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup;

import com.aliyuncs.ecs.model.v20140526.AuthorizeSecurityGroupResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author howechen
 */
public class CoreAuthorizeSecurityGroupResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreAuthorizeSecurityGroupResponseDto, AuthorizeSecurityGroupResponse> {
    private String requestId;

    public CoreAuthorizeSecurityGroupResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .toString();
    }
}
