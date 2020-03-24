package com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup;

import com.aliyuncs.ecs.model.v20140526.DeleteSecurityGroupRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author howechen
 */
public class CoreDeleteSecurityGroupRequestDto extends DeleteSecurityGroupRequest {
    private String identityParams;
    private String cloudParams;

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

    public CoreDeleteSecurityGroupRequestDto() {
    }

    public CoreDeleteSecurityGroupRequestDto(String securityGroupId, String identityParams, String cloudParams) {
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
    }

    public static DeleteSecurityGroupRequest toSdk(CoreDeleteSecurityGroupRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, DeleteSecurityGroupRequest.class);
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
