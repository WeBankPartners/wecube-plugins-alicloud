package com.webank.wecube.plugins.alicloud.dto.routeTable.routeEntry;

import com.aliyuncs.vpc.model.v20160428.CreateRouteEntryRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author howechen
 */
public class CoreCreateRouteEntryRequestDto extends CreateRouteEntryRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;

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

    public CoreCreateRouteEntryRequestDto() {
    }

    public static CreateRouteEntryRequest toSdk(CoreCreateRouteEntryRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, CreateRouteEntryRequest.class);
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
