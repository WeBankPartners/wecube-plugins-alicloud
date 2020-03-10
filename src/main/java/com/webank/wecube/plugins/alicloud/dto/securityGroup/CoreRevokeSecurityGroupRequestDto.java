package com.webank.wecube.plugins.alicloud.dto.securityGroup;

import com.aliyuncs.ecs.model.v20140526.RevokeSecurityGroupEgressRequest;
import com.aliyuncs.ecs.model.v20140526.RevokeSecurityGroupRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreRevokeSecurityGroupRequestDto extends RevokeSecurityGroupRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;

    private boolean isEgress;

    private String destGroupId;
    private String destGroupOwnerAccount;
    private Long destGroupOwnerId;

    public CoreRevokeSecurityGroupRequestDto() {
    }

    public static RevokeSecurityGroupRequest toSdk(CoreRevokeSecurityGroupRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, RevokeSecurityGroupRequest.class);
    }

    public static RevokeSecurityGroupEgressRequest toEgressSdk(CoreRevokeSecurityGroupRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, RevokeSecurityGroupEgressRequest.class);
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

    public String getDestGroupId() {
        return destGroupId;
    }

    public void setDestGroupId(String destGroupId) {
        this.destGroupId = destGroupId;
    }

    public String getDestGroupOwnerAccount() {
        return destGroupOwnerAccount;
    }

    public void setDestGroupOwnerAccount(String destGroupOwnerAccount) {
        this.destGroupOwnerAccount = destGroupOwnerAccount;
    }

    public Long getDestGroupOwnerId() {
        return destGroupOwnerId;
    }

    public void setDestGroupOwnerId(Long destGroupOwnerId) {
        this.destGroupOwnerId = destGroupOwnerId;
    }

    public boolean isEgress() {
        return isEgress;
    }

    public void setEgress(boolean egress) {
        isEgress = egress;
    }
}
