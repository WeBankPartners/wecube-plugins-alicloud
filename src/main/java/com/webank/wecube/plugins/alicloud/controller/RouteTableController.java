package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.*;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry.CoreCreateRouteEntryRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry.CoreCreateRouteEntryResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry.CoreDeleteRouteEntryRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry.CoreDeleteRouteEntryResponseDto;
import com.webank.wecube.plugins.alicloud.service.vpc.routeTable.RouteTableService;
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

    @PostMapping(path = "/create")
    @ResponseBody
    public CoreResponseDto<CoreCreateRouteTableResponseDto> createRouteTable(@RequestBody CoreRequestDto<CoreCreateRouteTableRequestDto> request) {
        List<CoreCreateRouteTableResponseDto> result = this.routeTableService.createRouteTable(request.getInputs());
        return new CoreResponseDto<CoreCreateRouteTableResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteRouteTableResponseDto> deleteRouteTable(@RequestBody CoreRequestDto<CoreDeleteRouteTableRequestDto> request) {
        List<CoreDeleteRouteTableResponseDto> result = this.routeTableService.deleteRouteTable(request.getInputs());
        return new CoreResponseDto<CoreDeleteRouteTableResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/vswitch/associate")
    @ResponseBody
    public CoreResponseDto<CoreAssociateRouteTableResponseDto> associateRouteTable(@RequestBody CoreRequestDto<CoreAssociateRouteTableRequestDto> request) {
        List<CoreAssociateRouteTableResponseDto> result = this.routeTableService.associateRouteTable(request.getInputs());
        return new CoreResponseDto<CoreAssociateRouteTableResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/route_entry/create")
    @ResponseBody
    public CoreResponseDto<CoreCreateRouteEntryResponseDto> createRouteEntry(@RequestBody CoreRequestDto<CoreCreateRouteEntryRequestDto> request) {
        List<CoreCreateRouteEntryResponseDto> result = this.routeTableService.createRouteEntry(request.getInputs());
        return new CoreResponseDto<CoreCreateRouteEntryResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/route_entry/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteRouteEntryResponseDto> deleteRouteEntry(@RequestBody CoreRequestDto<CoreDeleteRouteEntryRequestDto> request) {
        List<CoreDeleteRouteEntryResponseDto> result = this.routeTableService.deleteRouteEntry(request.getInputs());
        return new CoreResponseDto<CoreDeleteRouteEntryResponseDto>().withErrorCheck(result);
    }
}
