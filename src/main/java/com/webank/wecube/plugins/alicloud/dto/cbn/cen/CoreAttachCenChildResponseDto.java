package com.webank.wecube.plugins.alicloud.dto.cbn.cen;

import com.aliyuncs.cbn.model.v20170912.AttachCenChildInstanceResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author howechen
 */
public class CoreAttachCenChildResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreAttachCenChildResponseDto, AttachCenChildInstanceResponse> {

    private String requestId;

    public CoreAttachCenChildResponseDto() {
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
