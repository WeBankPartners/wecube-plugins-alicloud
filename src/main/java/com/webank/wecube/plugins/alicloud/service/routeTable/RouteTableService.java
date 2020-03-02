package com.webank.wecube.plugins.alicloud.service.routeTable;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.DescribeRouteTablesResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreAssociateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableResponseDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreDeleteRouteTableRequestDto;

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
     * @param client       AliCloud client
     * @param regionId     region ID
     * @param routeTableId route table ID
     * @return response from ali cloud server
     * @throws PluginException exception while retrieving the route table info
     */
    DescribeRouteTablesResponse retrieveRouteTable(IAcsClient client, String regionId, String routeTableId) throws PluginException;

    /**
     * Delete the route table
     *
     * @param deleteRouteTableRequestList core delete route table request list
     * @throws PluginException while deleting the route table
     */
    void deleteRouteTable(List<CoreDeleteRouteTableRequestDto> deleteRouteTableRequestList) throws PluginException;

    /**
     * Associate VSwtich with route table
     *
     * @param associateRouteTableRequest associate route table request
     * @return AssociateRouteTableResponse
     * @throws PluginException while associate VSwitch with route table
     */
    void associateVSwitch(List<CoreAssociateRouteTableRequestDto> associateRouteTableRequest) throws PluginException;

//    /**
//     * Un-associate VSwtich with route table
//     *
//     * @param identityParamDto             identity params
//     * @param cloudParamDto                cloud params
//     * @param unassociateRouteTableRequest un-associate route table request
//     * @return UnassociateRouteTableResponse
//     * @throws PluginException while unassociate VSwitch with route table
//     */
//    UnassociateRouteTableResponse unAssociateVSwitch(IdentityParamDto identityParamDto, CloudParamDto cloudParamDto, UnassociateRouteTableRequest unassociateRouteTableRequest) throws PluginException;

    /**
     * Check if route table is available for subsequent operation
     *
     * @param client       generated alicloud client
     * @param regionId     regionId
     * @param routeTableId routeTableId
     * @return if route table is available
     * @throws PluginException when checking the route table status
     */
    boolean checkIfRouteTableAvailable(IAcsClient client, String regionId, String routeTableId) throws PluginException;
}
