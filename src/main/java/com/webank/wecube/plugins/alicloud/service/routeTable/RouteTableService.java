package com.webank.wecube.plugins.alicloud.service.routeTable;

import com.aliyuncs.vpc.model.v20160428.AssociateRouteTableResponse;
import com.aliyuncs.vpc.model.v20160428.DeleteRouteTableRequest;
import com.aliyuncs.vpc.model.v20160428.DescribeRouteTablesResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableResponseDto;

import java.util.List;

/**
 * @author howechen
 */
public interface RouteTableService {

    /**
     * Create route table
     *
     * @param coreCreateRouteTableRequestDtoList create route table request dto list
     * @return response from ali cloud server
     * @throws PluginException exception while creating the route table
     */
    List<CoreCreateRouteTableResponseDto> createRouteTable(List<CoreCreateRouteTableRequestDto> coreCreateRouteTableRequestDtoList) throws PluginException;

    /**
     * Retrieve the route table data from ali cloud
     *
     * @param regionId     region ID
     * @param routeTableId route table ID
     * @return response from ali cloud server
     * @throws PluginException exception while retrieving the route table info
     */
    DescribeRouteTablesResponse retrieveRouteTable(String regionId, String routeTableId) throws PluginException;

    /**
     * Delete the route table
     *
     * @param deleteRouteTableRequestList delete route table request list
     * @throws PluginException while deleting the route table
     */
    void deleteRouteTable(List<DeleteRouteTableRequest> deleteRouteTableRequestList) throws PluginException;

    /**
     * Associate VSwtich with route table
     *
     * @param regionId     associate region id
     * @param routeTableId route table id
     * @param vSwitchId    VSwitch id
     * @return AssociateRouteTableResponse
     * @throws PluginException while associate VSwitch with route table
     */
    AssociateRouteTableResponse associateVSwitch(String regionId, String routeTableId, String vSwitchId) throws PluginException;
}
