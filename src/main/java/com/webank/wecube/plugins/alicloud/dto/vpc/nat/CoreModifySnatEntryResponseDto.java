package com.webank.wecube.plugins.alicloud.dto.vpc.nat;

import com.aliyuncs.vpc.model.v20160428.ModifySnatEntryResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

/**
 * @author howechen
 */
public class CoreModifySnatEntryResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreModifySnatEntryResponseDto, ModifySnatEntryResponse> {

    private String requestId;

    public CoreModifySnatEntryResponseDto() {
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


}
