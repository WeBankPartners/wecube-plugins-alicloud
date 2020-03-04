package com.webank.wecube.plugins.alicloud.service.routeTable;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreAssociateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableResponseDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreDeleteRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudConstant;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author howechen
 */
@Service
public class RouteTableServiceImpl implements RouteTableService {
    private static Logger logger = LoggerFactory.getLogger(RouteTableService.class);
    private AcsClientStub acsClientStub;

    @Autowired
    public RouteTableServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    @Override
    public List<CoreCreateRouteTableResponseDto> createRouteTable(List<CoreCreateRouteTableRequestDto> coreCreateRouteTableRequestDtoList) throws PluginException {
        List<CoreCreateRouteTableResponseDto> resultList = new ArrayList<>();
        for (CoreCreateRouteTableRequestDto request : coreCreateRouteTableRequestDtoList) {

            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(request.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(request.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final String routeTableId = request.getRouteTableId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);


            // if route table id exists, retrieve the info
            if (StringUtils.isNotEmpty(routeTableId)) {
                final DescribeRouteTablesResponse response = this.retrieveRouteTable(client, regionId, request.getRouteTableId());
                if (response.getTotalCount() == 1) {
                    final DescribeRouteTablesResponse.RouteTable foundRouteTable = response.getRouteTables().get(0);
                    resultList.add(new CoreCreateRouteTableResponseDto(response.getRequestId(), foundRouteTable.getRouteTableId()));
                }

            } else {
                // create the route table

                if (StringUtils.isEmpty(request.getVpcId())) {
                    String msg = "The vpcId cannot be null or empty.";
                    logger.info(msg);
                    throw new PluginException(msg);
                }

                request.setRegionId(regionId);

                final CreateRouteTableRequest aliCloudRequest = CoreCreateRouteTableRequestDto.toSdk(request);

                CreateRouteTableResponse response;
                try {
                    response = this.acsClientStub.request(client, aliCloudRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }

                resultList.add(CoreCreateRouteTableResponseDto.fromSdk(response));
            }
        }
        return resultList;
    }


    @Override
    public DescribeRouteTablesResponse retrieveRouteTable(IAcsClient client, String regionId, String routeTableId) throws PluginException {
        if (StringUtils.isEmpty(regionId)) {
            String msg = "The regionId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        logger.info(String.format("Retrieving route table info, region ID: [%s], route table ID: [%s]", regionId, routeTableId));

        DescribeRouteTablesRequest request = new DescribeRouteTablesRequest();
        request.setRegionId(regionId);
        request.setRouteTableId(routeTableId);

        DescribeRouteTablesResponse response;
        try {
            response = this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }

        return response;
    }

    @Override
    public void deleteRouteTable(List<CoreDeleteRouteTableRequestDto> coreDeleteRouteTableRequestDtoList) throws PluginException {
        for (CoreDeleteRouteTableRequestDto coreDeleteRouteTableRequestDto : coreDeleteRouteTableRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(coreDeleteRouteTableRequestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(coreDeleteRouteTableRequestDto.getCloudParams());
            final String routeTableId = coreDeleteRouteTableRequestDto.getRouteTableId();
            final String regionId = cloudParamDto.getRegionId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            if (StringUtils.isAnyEmpty(regionId, routeTableId)) {
                String msg = "The regionId or route table ID cannot be null";
                logger.error(msg);
                throw new PluginException(msg);
            }


            logger.info("Deleting route table, route table ID: [{}], route table region:[{}]", routeTableId, regionId);


            DeleteRouteTableRequest deleteRouteTableRequest = CoreDeleteRouteTableRequestDto.toSdk(coreDeleteRouteTableRequestDto);
            deleteRouteTableRequest.setRegionId(regionId);

            // check if route table already deleted
            final DescribeRouteTablesResponse retrieveRouteTableResponse = this.retrieveRouteTable(client, regionId, routeTableId);
            if (0 == retrieveRouteTableResponse.getTotalCount()) {
                continue;
            }

            final DescribeRouteTablesResponse.RouteTable foundRouteTable = retrieveRouteTableResponse.getRouteTables().get(0);

            // do not handle the system route table
            if (StringUtils.equals(AliCloudConstant.ROUTE_TABLE_TYPE_SYSTEM, foundRouteTable.getRouteTableType())) {
                continue;
            }

            // un-associate all related VSwitches
            if (!foundRouteTable.getVSwitchIds().isEmpty()) {
                for (String vSwitchId : foundRouteTable.getVSwitchIds()) {
                    UnassociateRouteTableRequest unassociateRouteTableRequest = new UnassociateRouteTableRequest();
                    unassociateRouteTableRequest.setRegionId(regionId);
                    unassociateRouteTableRequest.setRouteTableId(foundRouteTable.getRouteTableId());
                    unassociateRouteTableRequest.setVSwitchId(vSwitchId);
                    this.unAssociateRouteTable(client, unassociateRouteTableRequest);
                }
            }

            // delete route table
            try {
                this.acsClientStub.request(client, coreDeleteRouteTableRequestDto);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }


            // re-check if route table has already been deleted
            if (0 != this.retrieveRouteTable(client, regionId, routeTableId).getTotalCount()) {
                String msg = String.format("The route table: [%s] from region: [%s] hasn't been deleted", routeTableId, regionId);
                logger.error(msg);
                throw new PluginException(msg);
            }
        }
    }

    @Override
    public void associateRouteTable(List<CoreAssociateRouteTableRequestDto> coreAssociateRouteTableRequestDtoList) throws PluginException {

        for (CoreAssociateRouteTableRequestDto coreAssociateRouteTableRequestDto : coreAssociateRouteTableRequestDtoList) {

            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(coreAssociateRouteTableRequestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(coreAssociateRouteTableRequestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final String routeTableId = coreAssociateRouteTableRequestDto.getRouteTableId();
            final String vSwitchId = coreAssociateRouteTableRequestDto.getVSwitchId();

            if (StringUtils.isAnyEmpty(regionId, routeTableId, vSwitchId)) {
                String msg = "Either regionId, routeTableId, vSwitchID cannot be null or empty.";
                logger.error(msg);
                throw new PluginException(msg);
            }

            logger.info(String.format("Associating route table: [%s] with VSwitch: [%s]", coreAssociateRouteTableRequestDto.getRouteTableId(), coreAssociateRouteTableRequestDto.getVSwitchId()));

            AssociateRouteTableRequest associateRouteTableRequest = CoreAssociateRouteTableRequestDto.toSdk(coreAssociateRouteTableRequestDto);
            associateRouteTableRequest.setRegionId(regionId);

            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            try {
                this.acsClientStub.request(client, associateRouteTableRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }
        }
        logger.info("Route table and VSwitch successfully un-associated.");
    }

    @Override
    public void associateRouteTable(IAcsClient client, List<AssociateRouteTableRequest> associateRouteTableRequestList) throws PluginException {
        for (AssociateRouteTableRequest request : associateRouteTableRequestList) {

            if (StringUtils.isAnyEmpty(request.getRegionId(), request.getRouteTableId(), request.getVSwitchId())) {
                String msg = "Either the region ID, route table ID or VSwitchID cannot be null or empty while associating route table with VSwitch";
                logger.error(msg);
                throw new PluginException(msg);
            }
            logger.info(String.format("Associating route table: [%s] with VSwitch: [%s]", request.getRouteTableId(), request.getVSwitchId()));

            try {
                this.acsClientStub.request(client, request);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }
        }
        logger.info("Route table and VSwitch successfully associated.");
    }

    @Override
    public UnassociateRouteTableResponse unAssociateRouteTable(IAcsClient client, UnassociateRouteTableRequest unassociateRouteTableRequest) throws PluginException {
        if (StringUtils.isAnyEmpty(unassociateRouteTableRequest.getRegionId(), unassociateRouteTableRequest.getRouteTableId(), unassociateRouteTableRequest.getVSwitchId())) {
            String msg = "Either the region ID, route table ID or VSwitchID cannot be null or empty while associating route table with VSwitch";
            logger.error(msg);
            throw new PluginException(msg);
        }

        logger.info(String.format("Un-associating route table: [%s] with VSwitch: [%s]", unassociateRouteTableRequest.getRouteTableId(), unassociateRouteTableRequest.getVSwitchId()));
        UnassociateRouteTableResponse response;
        try {
            response = this.acsClientStub.request(client, unassociateRouteTableRequest);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }
        logger.info("Route table and VSwitch successfully un-associated.");
        return response;
    }

    @Override
    public boolean checkIfRouteTableAvailable(IAcsClient client, String regionId, String routeTableId) throws PluginException {
        logger.info("Retrieving route table status.");
        if (StringUtils.isEmpty(regionId)) {
            String msg = "The regionId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        DescribeRouteTablesRequest request = new DescribeRouteTablesRequest();
        request.setRegionId(regionId);
        request.setRouteTableId(routeTableId);

        DescribeRouteTablesResponse response;
        try {
            response = this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }

        return StringUtils.equals(AliCloudConstant.RESOURCE_AVAILABLE_STATUS, response.getRouteTables().get(0).getStatus());
    }


}
