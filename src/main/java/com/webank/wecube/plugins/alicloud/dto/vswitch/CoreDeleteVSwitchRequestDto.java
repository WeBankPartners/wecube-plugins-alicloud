package com.webank.wecube.plugins.alicloud.dto.vswitch;

import com.aliyuncs.vpc.model.v20160428.DeleteVSwitchRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreDeleteVSwitchRequestDto extends DeleteVSwitchRequest {
    private String identityParams;
    private String cloudParams;

    public CoreDeleteVSwitchRequestDto(String vSwitchId, String identityParams, String cloudParams) {
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
    }

    public CoreDeleteVSwitchRequestDto() {
    }

    public static DeleteVSwitchRequest toSdk(CoreDeleteVSwitchRequestDto coreCreateVSwitchRequestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(coreCreateVSwitchRequestDto, DeleteVSwitchRequest.class);
    }

    public String getIdentityParams() {
        return identityParams;
    }

    public void setIdentityParams(String identityParams) {
        this.identityParams = identityParams;
    }

    public String getCloudParams() {
        return cloudParams;
    }

    public void setCloudParams(String cloudParams) {
        this.cloudParams = cloudParams;
    }
}
