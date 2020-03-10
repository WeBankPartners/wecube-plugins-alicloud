package com.webank.wecube.plugins.alicloud.dto.routeTable.routeEntry;

import com.aliyuncs.vpc.model.v20160428.DeleteRouteEntryRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreDeleteRouteEntryRequestDto extends DeleteRouteEntryRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;

    public CoreDeleteRouteEntryRequestDto() {
    }

    public static DeleteRouteEntryRequest toSdk(CoreDeleteRouteEntryRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, DeleteRouteEntryRequest.class);
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
}
