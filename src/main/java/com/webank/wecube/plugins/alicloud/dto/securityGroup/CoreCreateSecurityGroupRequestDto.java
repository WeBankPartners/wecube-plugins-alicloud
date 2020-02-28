package com.webank.wecube.plugins.alicloud.dto.securityGroup;

import com.aliyuncs.ecs.model.v20140526.CreateSecurityGroupRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateSecurityGroupRequestDto extends CreateSecurityGroupRequest {
    private String securityGroupId;

    public CoreCreateSecurityGroupRequestDto() {
    }

    public CoreCreateSecurityGroupRequestDto(String securityGroupId) {
        this.securityGroupId = securityGroupId;
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
}
