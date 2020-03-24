package com.webank.wecube.plugins.alicloud.dto.vpc.routeTable;

import com.aliyuncs.vpc.model.v20160428.CreateRouteTableResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public class CoreCreateRouteTableResponseDto extends CreateRouteTableResponse {
    public CoreCreateRouteTableResponseDto() {
        super();
    }

    public CoreCreateRouteTableResponseDto(String requestId, String routeTableId) {
        super();
        this.setRequestId(requestId);
        this.setRouteTableId(routeTableId);
    }

    public static CoreCreateRouteTableResponseDto fromSdk(CreateRouteTableResponse response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, CoreCreateRouteTableResponseDto.class);
    }
}
