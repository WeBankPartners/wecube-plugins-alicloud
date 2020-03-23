package com.webank.wecube.plugins.alicloud.dto.vpc.eip;

import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;

/**
 * @author howechen
 */
public class CoreReleaseEipResponseDto extends CoreResponseOutputDto<CoreReleaseEipResponseDto> {
    private String requestId;

    public CoreReleaseEipResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
