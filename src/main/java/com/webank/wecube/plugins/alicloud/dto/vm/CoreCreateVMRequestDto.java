package com.webank.wecube.plugins.alicloud.dto.vm;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateVMRequestDto extends CreateInstanceRequest {
    private String instanceId;
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;

    public CoreCreateVMRequestDto(String instanceId, String identityParams, String cloudParams) {
        this.instanceId = instanceId;
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
    }

    public CoreCreateVMRequestDto() {
    }

    public static CreateInstanceRequest toSdk(CoreCreateVMRequestDto coreCreateVMRequestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(coreCreateVMRequestDto, CreateInstanceRequest.class);
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
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

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCallbackParameter() {
        return callbackParameter;
    }

    public void setCallbackParameter(String callbackParameter) {
        this.callbackParameter = callbackParameter;
    }
}
