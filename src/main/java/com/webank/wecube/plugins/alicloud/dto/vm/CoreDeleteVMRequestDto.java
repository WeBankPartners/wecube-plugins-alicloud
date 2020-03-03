package com.webank.wecube.plugins.alicloud.dto.vm;

import com.aliyuncs.ecs.model.v20140526.DeleteInstanceRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreDeleteVMRequestDto extends DeleteInstanceRequest {
    private String identityParams;
    private String cloudParams;

    public CoreDeleteVMRequestDto(String instanceId, String identityParams, String cloudParams) {
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
    }

    public CoreDeleteVMRequestDto() {
    }

    public static DeleteInstanceRequest toSdk(CoreDeleteVMRequestDto coreCreateVMRequestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(coreCreateVMRequestDto, DeleteInstanceRequest.class);
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
