package com.webank.wecube.plugins.alicloud.controller;

import com.aliyuncs.vpc.model.v20160428.DeleteRouteTableRequest;
import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableResponseDto;
import com.webank.wecube.plugins.alicloud.service.routeTable.RouteTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/route_table")
public class RouteTableController {

    private RouteTableService routeTableService;

    @Autowired
    public RouteTableController(RouteTableService routeTableService) {
        this.routeTableService = routeTableService;
    }

    @PostMapping(path = "/")
    @ResponseBody
    public CoreResponseDto<?> createRouteTable(@RequestBody CoreRequestDto<CoreCreateRouteTableRequestDto> requestBody) {
        List<CoreCreateRouteTableResponseDto> result;
        try {
            result = this.routeTableService.createRouteTable(requestBody.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreCreateRouteTableResponseDto>().okayWithData(result);
    }

    @DeleteMapping(path = "/")
    @ResponseBody
    public CoreResponseDto<?> deleteRouteTable(@RequestBody CoreRequestDto<DeleteRouteTableRequest> request) {
        try {
            this.routeTableService.deleteRouteTable(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return CoreResponseDto.okay();
    }
}
