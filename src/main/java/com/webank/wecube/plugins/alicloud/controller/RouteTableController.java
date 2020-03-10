package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreAssociateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableResponseDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreDeleteRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.routeEntry.CoreCreateRouteEntryRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.routeEntry.CoreCreateRouteEntryResponseDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.routeEntry.CoreDeleteRouteEntryRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.routeEntry.CoreDeleteRouteEntryResponseDto;
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

    @PostMapping(path = "/create")
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

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<?> deleteRouteTable(@RequestBody CoreRequestDto<CoreDeleteRouteTableRequestDto> request) {
        try {
            this.routeTableService.deleteRouteTable(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return CoreResponseDto.okay();
    }

    @PostMapping(path = "/vswitch/associate")
    @ResponseBody
    public CoreResponseDto<?> associateVSwitch(@RequestBody CoreRequestDto<CoreAssociateRouteTableRequestDto> request) {
        try {
            this.routeTableService.associateRouteTable(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return CoreResponseDto.okay();
    }

    @PostMapping(path = "/route_entry/create")
    @ResponseBody
    public CoreResponseDto<?> createRouteEntry(@RequestBody CoreRequestDto<CoreCreateRouteEntryRequestDto> request) {
        List<CoreCreateRouteEntryResponseDto> result;
        try {
            result = this.routeTableService.createRouteEntry(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreCreateRouteEntryResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/route_entry/delete")
    @ResponseBody
    public CoreResponseDto<?> deleteRouteEntry(@RequestBody CoreRequestDto<CoreDeleteRouteEntryRequestDto> request) {
        List<CoreDeleteRouteEntryResponseDto> result;
        try {
            result = this.routeTableService.deleteRouteEntry(request.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDto.error(ex.getMessage());
        }
        return new CoreResponseDto<CoreDeleteRouteEntryResponseDto>().okayWithData(result);
    }
}
