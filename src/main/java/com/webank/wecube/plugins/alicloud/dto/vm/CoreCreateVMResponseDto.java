package com.webank.wecube.plugins.alicloud.dto.vm;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateVMResponseDto extends CreateInstanceResponse {

    public CoreCreateVMResponseDto() {
    }

    public static CoreCreateVMResponseDto fromSdk(CreateInstanceResponse createInstanceResponse) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(createInstanceResponse, CoreCreateVMResponseDto.class);
    }

    public static CoreCreateVMResponseDto fromSdk(DescribeInstancesResponse.Instance instance) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(instance, CoreCreateVMResponseDto.class);
    }
}
