package com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer;

import com.aliyuncs.slb.model.v20140515.AddBackendServersResponse;
import com.aliyuncs.slb.model.v20140515.CreateVServerGroupResponse;
import com.aliyuncs.slb.model.v20140515.DescribeVServerGroupAttributeResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @author howechen
 */
public class CoreAddBackendServerResponseDto extends AddBackendServersResponse {
    private String guid;
    private String callbackParameter;

    public CoreAddBackendServerResponseDto() {
    }

    public static List<AddBackendServersResponse.BackendServer> transferRetrieveBackendServerInfoFromSDK(List<DescribeVServerGroupAttributeResponse.BackendServer> backendServerList) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(backendServerList, new TypeReference<List<BackendServer>>() {
        });

    }

    public static List<AddBackendServersResponse.BackendServer> transferCreatedBackendServerInfoFromSDK(List<CreateVServerGroupResponse.BackendServer> backendServerList) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(backendServerList, new TypeReference<List<BackendServer>>() {
        });

    }

    public static CoreAddBackendServerResponseDto fromSdk(AddBackendServersResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, CoreAddBackendServerResponseDto.class);
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
