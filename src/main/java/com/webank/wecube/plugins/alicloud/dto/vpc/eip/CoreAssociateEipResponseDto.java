package com.webank.wecube.plugins.alicloud.dto.vpc.eip;

import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;

/**
 * @author howechen
 */
public class CoreAssociateEipResponseDto extends CoreResponseOutputDto<CoreAssociateEipResponseDto> {
    private String requestId;

    public CoreAssociateEipResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
