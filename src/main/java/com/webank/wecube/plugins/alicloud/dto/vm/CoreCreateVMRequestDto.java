package com.webank.wecube.plugins.alicloud.dto.vm;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateVMRequestDto extends CreateInstanceRequest {
    private String instanceId;

    public CoreCreateVMRequestDto(String instanceId) {
        this.instanceId = instanceId;
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
}
