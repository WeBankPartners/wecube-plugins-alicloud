package com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry;

import com.aliyuncs.vpc.model.v20160428.DeleteRouteEntryResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreDeleteRouteEntryResponseDto extends DeleteRouteEntryResponse {

    private String guid;
    private String callbackParameter;

    public CoreDeleteRouteEntryResponseDto() {
    }

    public static CoreDeleteRouteEntryResponseDto fromSdk(DeleteRouteEntryResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, CoreDeleteRouteEntryResponseDto.class);
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
