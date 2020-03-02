package com.webank.wecube.plugins.alicloud.dto.routeTable;

import com.aliyuncs.vpc.model.v20160428.DeleteRouteTableRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreDeleteRouteTableRequestDto extends DeleteRouteTableRequest {
    private String identityParams;
    private String cloudParams;

    public CoreDeleteRouteTableRequestDto() {
    }

    public CoreDeleteRouteTableRequestDto(String routeTableId, String identityParams, String cloudParams) {
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
    }

    public static DeleteRouteTableRequest toSdk(CoreDeleteRouteTableRequestDto coreCreateRouteTableRequestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(coreCreateRouteTableRequestDto, DeleteRouteTableRequest.class);
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
