package com.webank.wecube.plugins.alicloud.dto.securityGroup;

import com.aliyuncs.ecs.model.v20140526.DeleteSecurityGroupRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreDeleteSecurityGroupRequestDto extends DeleteSecurityGroupRequest {
    private String identityParams;
    private String cloudParams;

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
