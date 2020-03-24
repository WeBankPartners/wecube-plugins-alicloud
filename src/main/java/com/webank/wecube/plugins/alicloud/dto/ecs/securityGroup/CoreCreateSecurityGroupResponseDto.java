package com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup;

import com.aliyuncs.ecs.model.v20140526.CreateSecurityGroupResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateSecurityGroupResponseDto extends CreateSecurityGroupResponse {

    public CoreCreateSecurityGroupResponseDto() {
    }

    public CoreCreateSecurityGroupResponseDto(String requestId, String securityGroupId) {
        super();
        this.setRequestId(requestId);
        this.setSecurityGroupId(securityGroupId);
    }

    public static CoreCreateSecurityGroupResponseDto fromSdk(CreateSecurityGroupResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, CoreCreateSecurityGroupResponseDto.class);
    }
}
