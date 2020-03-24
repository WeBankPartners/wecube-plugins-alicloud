package com.webank.wecube.plugins.alicloud.service.routeTable;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.AssociateRouteTableRequest;
import com.aliyuncs.vpc.model.v20160428.DescribeRouteTablesResponse;
import com.aliyuncs.vpc.model.v20160428.UnassociateRouteTableRequest;
import com.aliyuncs.vpc.model.v20160428.UnassociateRouteTableResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.CoreAssociateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.CoreCreateRouteTableResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.CoreDeleteRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry.CoreCreateRouteEntryRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry.CoreCreateRouteEntryResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry.CoreDeleteRouteEntryRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry.CoreDeleteRouteEntryResponseDto;

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
     * @param associateRouteTableRequestList associate route table request
     * @throws PluginException while associate VSwitch with route table
     */
    void associateRouteTable(List<CoreAssociateRouteTableRequestDto> associateRouteTableRequestList) throws PluginException;

    /**
     * Associate VSwtich with route table
     *
     * @param client                      AWS client
     * @param associateRouteTableRequests associate route table request
     * @throws PluginException while associate VSwitch with route table
     */
    void associateRouteTable(IAcsClient client, List<AssociateRouteTableRequest> associateRouteTableRequests) throws PluginException;

    /**
     * Un-associate VSwtich with route table
     *
     * @param client                       AWS client
     * @param unassociateRouteTableRequest un-associate route table request
     * @return UnassociateRouteTableResponse
     * @throws PluginException while unassociate VSwitch with route table
     */
    UnassociateRouteTableResponse unAssociateRouteTable(IAcsClient client, UnassociateRouteTableRequest unassociateRouteTableRequest) throws PluginException;

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

    /**
     * Create route entry
     *
     * @param coreCreateRouteEntryRequestDtoList core create route entry request dto list
     * @return core create route entry response dto list
     * @throws PluginException plugin exception
     */
    List<CoreCreateRouteEntryResponseDto> createRouteEntry(List<CoreCreateRouteEntryRequestDto> coreCreateRouteEntryRequestDtoList) throws PluginException;

    /**
     * Delete route entry
     *
     * @param coreDeleteRouteEntryRequestDtoList core delete route entry request dto list
     * @return core delete route entry response dto list
     * @throws PluginException plugin exception
     */
    List<CoreDeleteRouteEntryResponseDto> deleteRouteEntry(List<CoreDeleteRouteEntryRequestDto> coreDeleteRouteEntryRequestDtoList) throws PluginException;
}
