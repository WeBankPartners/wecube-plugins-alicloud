package com.webank.wecube.plugins.alicloud.dto.routeTable;

import com.aliyuncs.vpc.model.v20160428.AssociateRouteTableRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreAssociateRouteTableRequestDto extends AssociateRouteTableRequest {
    private String identityParams;
    private String cloudParams;

    public CoreAssociateRouteTableRequestDto() {
    }

    public CoreAssociateRouteTableRequestDto(String routeTableId, String identityParams, String cloudParams) {
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
    }

    public static AssociateRouteTableRequest toSdk(AssociateRouteTableRequest coreCreateRouteTableRequestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(coreCreateRouteTableRequestDto, AssociateRouteTableRequest.class);
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