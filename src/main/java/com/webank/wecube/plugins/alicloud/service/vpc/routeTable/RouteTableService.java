package com.webank.wecube.plugins.alicloud.service.vpc.routeTable;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.*;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry.CoreCreateRouteEntryRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry.CoreCreateRouteEntryResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry.CoreDeleteRouteEntryRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.routeEntry.CoreDeleteRouteEntryResponseDto;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;

import java.util.List;

/**
 * @author howechen
 */
public interface RouteTableService {

    /**
     * Create route table
     *
     * @param requestDtoList create route table request dto list
     * @return response from ali cloud server
     */
    List<CoreCreateRouteTableResponseDto> createRouteTable(List<CoreCreateRouteTableRequestDto> requestDtoList);

    /**
     * Create route table internally
     *
     * @param createRouteTableRequest create route table request
     * @param regionId                region id
     * @return response from ali cloud server
     */
    CreateRouteTableResponse createRouteTable(IAcsClient client, CreateRouteTableRequest createRouteTableRequest, String regionId) throws PluginException, AliCloudException;

    /**
     * Retrieve the route table data from ali cloud
     *
     * @param client       AliCloud client
     * @param regionId     region ID
     * @param routeTableId route table ID
     * @return response from ali cloud server
     * @throws PluginException   exception while retrieving the route table info
     * @throws AliCloudException exception while retrieving the route table info
     */
    DescribeRouteTablesResponse retrieveRouteTable(IAcsClient client, String regionId, String routeTableId) throws PluginException, AliCloudException;

    /**
     * Delete the route table
     *
     * @param deleteRouteTableRequestList core delete route table request list
     */
    List<CoreDeleteRouteTableResponseDto> deleteRouteTable(List<CoreDeleteRouteTableRequestDto> deleteRouteTableRequestList);


    /**
     * Delete route table
     *
     * @param client                  generated alicloud client
     * @param deleteRouteTableRequest delete route table request
     * @param regionId                regionId
     * @return DeleteRouteTableResponse
     * @throws PluginException   when deleting the route table
     * @throws AliCloudException when deleting the route table
     */
    DeleteRouteTableResponse deleteRouteTable(IAcsClient client, DeleteRouteTableRequest deleteRouteTableRequest, String regionId) throws PluginException, AliCloudException;

    /**
     * Associate VSwtich with route table
     *
     * @param associateRouteTableRequestList associate route table request
     */
    List<CoreAssociateRouteTableResponseDto> associateRouteTable(List<CoreAssociateRouteTableRequestDto> associateRouteTableRequestList);

    /**
     * Associate VSwtich with route table
     *
     * @param client   AWS client
     * @param request  associate route table request
     * @param regionId regionId
     * @throws PluginException   while associate VSwitch with route table
     * @throws AliCloudException while associate VSwitch with route table
     */
    void associateRouteTable(IAcsClient client, AssociateRouteTableRequest request, String regionId) throws PluginException, AliCloudException;

    /**
     * Un-associate VSwtich with route table
     *
     * @param client                       AWS client
     * @param unassociateRouteTableRequest un-associate route table request
     * @param regionId                     regionId
     * @return UnassociateRouteTableResponse
     * @throws PluginException   while un-associate VSwitch with route table
     * @throws AliCloudException while un-associate VSwitch with route table
     */
    UnassociateRouteTableResponse unAssociateRouteTable(IAcsClient client, UnassociateRouteTableRequest unassociateRouteTableRequest, String regionId) throws PluginException, AliCloudException;

    /**
     * Check if route table is available for subsequent operation
     *
     * @param client       generated alicloud client
     * @param regionId     regionId
     * @param routeTableId routeTableId
     * @return if route table is available
     * @throws PluginException   when checking the route table status
     * @throws AliCloudException when checking the route table status
     */
    boolean checkIfRouteTableAvailable(IAcsClient client, String regionId, String routeTableId) throws PluginException, AliCloudException;

    /**
     * Create route entry
     *
     * @param coreCreateRouteEntryRequestDtoList core create route entry request dto list
     * @return core create route entry response dto list
     */
    List<CoreCreateRouteEntryResponseDto> createRouteEntry(List<CoreCreateRouteEntryRequestDto> coreCreateRouteEntryRequestDtoList);

    /**
     * Delete route entry
     *
     * @param coreDeleteRouteEntryRequestDtoList core delete route entry request dto list
     * @return core delete route entry response dto list
     */
    List<CoreDeleteRouteEntryResponseDto> deleteRouteEntry(List<CoreDeleteRouteEntryRequestDto> coreDeleteRouteEntryRequestDtoList);
}
