package com.webank.wecube.plugins.alicloud.dto.cbn.cen;

import com.aliyuncs.cbn.model.v20170912.CreateCenResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author howechen
 */
public class CoreCreateCenResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateCenResponseDto, CreateCenResponse> {
    private String requestId;
    private String cenId;

    public CoreCreateCenResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCenId() {
        return cenId;
    }

    public void setCenId(String cenId) {
        this.cenId = cenId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .append("cenId", cenId)
                .toString();
    }
}
