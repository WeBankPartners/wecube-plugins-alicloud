package com.webank.wecube.plugins.alicloud.dto.vswitch;

import com.aliyuncs.vpc.model.v20160428.CreateVSwitchRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateVSwitchRequestDto extends CreateVSwitchRequest {
    private String vSwitchId;

    public CoreCreateVSwitchRequestDto(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    public CoreCreateVSwitchRequestDto() {
    }

    public static CreateVSwitchRequest toSdk(CoreCreateVSwitchRequestDto coreCreateVSwitchRequestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(coreCreateVSwitchRequestDto, CreateVSwitchRequest.class);
    }

    public String getvSwitchId() {
        return vSwitchId;
    }

    public void setvSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }
}
