package com.webank.wecube.plugins.alicloud.dto.ecs.vm;

import com.aliyuncs.ecs.model.v20140526.DeleteInstanceRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author howechen
 */
public class CoreDeleteVMRequestDto extends DeleteInstanceRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;

    @JsonDeserialize(as = Boolean.class)
    @Override
    public void setTerminateSubscription(Boolean terminateSubscription) {
        super.setTerminateSubscription(terminateSubscription);
    }

    @JsonDeserialize(as = Boolean.class)
    @Override
    public void setForce(Boolean force) {
        super.setForce(force);
    }

    @JsonDeserialize(as = Long.class)
    @Override
    public void setResourceOwnerId(Long resourceOwnerId) {
        super.setResourceOwnerId(resourceOwnerId);
    }

    @JsonDeserialize(as = Long.class)
    @Override
    public void setOwnerId(Long ownerId) {
        super.setOwnerId(ownerId);
    }

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
