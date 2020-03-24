package com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup;

import com.aliyuncs.ecs.model.v20140526.CreateSecurityGroupRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author howechen
 */
public class CoreCreateSecurityGroupRequestDto extends CreateSecurityGroupRequest {
    private String securityGroupId;
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

    public CoreCreateSecurityGroupRequestDto() {
    }

    public CoreCreateSecurityGroupRequestDto(String securityGroupId, String identityParams, String cloudParams) {
        this.securityGroupId = securityGroupId;
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
    }

    public static CreateSecurityGroupRequest toSdk(CoreCreateSecurityGroupRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, CreateSecurityGroupRequest.class);
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
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
