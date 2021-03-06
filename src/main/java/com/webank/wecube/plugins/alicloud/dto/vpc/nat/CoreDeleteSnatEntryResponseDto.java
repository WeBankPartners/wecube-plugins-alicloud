package com.webank.wecube.plugins.alicloud.dto.vpc.nat;

import com.aliyuncs.vpc.model.v20160428.DeleteSnatEntryResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author howechen
 */
public class CoreDeleteSnatEntryResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreDeleteSnatEntryResponseDto, DeleteSnatEntryResponse> {

    private String requestId;

    public CoreDeleteSnatEntryResponseDto() {
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
