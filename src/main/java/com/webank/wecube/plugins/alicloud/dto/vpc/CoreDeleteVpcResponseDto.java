package com.webank.wecube.plugins.alicloud.dto.vpc;

import com.aliyuncs.vpc.model.v20160428.DeleteVpcResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author howechen
 */
public class CoreDeleteVpcResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreDeleteVpcResponseDto, DeleteVpcResponse> {
    @JsonIgnore
    private String requestId;

    public CoreDeleteVpcResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .toString();
    }
}
