package com.webank.wecube.plugins.alicloud.dto.vpc.eip;

import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;

/**
 * @author howechen
 */
public class CoreUnAssociateEipResponseDto extends CoreResponseOutputDto<CoreUnAssociateEipResponseDto> {
    private String requestId;

    public CoreUnAssociateEipResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
