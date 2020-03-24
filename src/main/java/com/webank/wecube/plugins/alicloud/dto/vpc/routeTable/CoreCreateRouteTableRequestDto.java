package com.webank.wecube.plugins.alicloud.dto.vpc.routeTable;

import com.aliyuncs.vpc.model.v20160428.CreateRouteTableRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author howechen
 */
public class CoreCreateRouteTableRequestDto extends CreateRouteTableRequest {
    private String routeTableId;
    private String identityParams;
    private String cloudParams;

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

    public CoreCreateRouteTableRequestDto() {
    }

    public CoreCreateRouteTableRequestDto(String routeTableId, String identityParams, String cloudParams) {
        this.routeTableId = routeTableId;
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
    }

    public static CreateRouteTableRequest toSdk(CoreCreateRouteTableRequestDto coreCreateRouteTableRequestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(coreCreateRouteTableRequestDto, CreateRouteTableRequest.class);
    }

    public String getRouteTableId() {
        return routeTableId;
    }

    public void setRouteTableId(String routeTableId) {
        this.routeTableId = routeTableId;
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
