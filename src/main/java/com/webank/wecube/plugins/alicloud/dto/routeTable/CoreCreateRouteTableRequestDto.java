package com.webank.wecube.plugins.alicloud.dto.routeTable;

import com.aliyuncs.vpc.model.v20160428.CreateRouteTableRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateRouteTableRequestDto extends CreateRouteTableRequest {
    private String routeTableId;

    public CoreCreateRouteTableRequestDto() {
    }

    public CoreCreateRouteTableRequestDto(String routeTableId) {
        this.routeTableId = routeTableId;
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
}
