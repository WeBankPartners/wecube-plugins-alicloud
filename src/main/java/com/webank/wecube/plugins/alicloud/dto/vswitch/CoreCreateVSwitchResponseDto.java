package com.webank.wecube.plugins.alicloud.dto.vswitch;

import com.aliyuncs.vpc.model.v20160428.CreateVSwitchResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateVSwitchResponseDto extends CreateVSwitchResponse {

    public CoreCreateVSwitchResponseDto() {
    }

    public CoreCreateVSwitchResponseDto(String requestId, String vSwitchId) {
        super();
        this.setRequestId(requestId);
        this.setVSwitchId(vSwitchId);
    }


    public static CoreCreateVSwitchResponseDto fromSdk(CreateVSwitchResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, CoreCreateVSwitchResponseDto.class);
    }
}
