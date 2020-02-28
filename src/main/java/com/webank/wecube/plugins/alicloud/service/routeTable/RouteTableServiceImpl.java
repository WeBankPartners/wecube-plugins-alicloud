package com.webank.wecube.plugins.alicloud.service.routeTable;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableResponseDto;
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
            final String routeTableId = request.getRouteTableId();
            if (!StringUtils.isEmpty(routeTableId)) {
                final DescribeRouteTablesResponse response = this.retrieveRouteTable(request.getRegionId(), request.getRouteTableId());
                if (response.getTotalCount() == 1) {
                    final DescribeRouteTablesResponse.RouteTable foundRouteTable = response.getRouteTables().get(0);
                    resultList.add(new CoreCreateRouteTableResponseDto(response.getRequestId(), foundRouteTable.getRouteTableId()));

                }
                continue;
            }

            if (StringUtils.isAnyEmpty(request.getRegionId(), request.getVpcId())) {
                String msg = "Either requested regionId or VPC id field cannot be empty or null.";
                logger.info(msg);
                throw new PluginException(msg);
            }

            final IAcsClient client = this.acsClientStub.generateAcsClient(request.getRegionId());
            final CreateRouteTableRequest aliCloudRequest = CoreCreateRouteTableRequestDto.toSdk(request);

            CreateRouteTableResponse response;
            try {
                response = this.acsClientStub.request(client, aliCloudRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }


            resultList.add(CoreCreateRouteTableResponseDto.fromSdk(response));

        }
        return resultList;
    }


    @Override
    public DescribeRouteTablesResponse retrieveRouteTable(String regionId, String routeTableId) throws PluginException {
        if (StringUtils.isEmpty(regionId)) {
            String msg = "The regionId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        logger.info(String.format("Retrieving route table info, region ID: [%s], route table ID: [%s]", regionId, routeTableId));

        final IAcsClient client = this.acsClientStub.generateAcsClient(regionId);
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
    public void deleteRouteTable(List<DeleteRouteTableRequest> deleteRouteTableRequestList) throws PluginException {
        for (DeleteRouteTableRequest deleteRouteTableRequest : deleteRouteTableRequestList) {
            if (StringUtils.isAnyEmpty(deleteRouteTableRequest.getRegionId(), deleteRouteTableRequest.getRouteTableId())) {
                String msg = "The regionId or route table ID cannot be null";
                logger.error(msg);
                throw new PluginException(msg);
            }

            logger.info("Deleting route table, route table ID: [{}], route table region:[{}]", deleteRouteTableRequest.getRouteTableId(), deleteRouteTableRequest.getRegionId());
            final DescribeRouteTablesResponse retrieveRouteTableResponse = this.retrieveRouteTable(deleteRouteTableRequest.getRegionId(), deleteRouteTableRequest.getRouteTableId());
            // check if route table already deleted
            if (0 == retrieveRouteTableResponse.getTotalCount()) {
                continue;
            }

            // un-associate all related VSwitches
            final DescribeRouteTablesResponse.RouteTable routeTable = retrieveRouteTableResponse.getRouteTables().get(0);
            if (!routeTable.getVSwitchIds().isEmpty()) {
                for (String vSwitchId : routeTable.getVSwitchIds()) {
                    UnassociateRouteTableRequest unassociateRouteTableRequest = new UnassociateRouteTableRequest();
                    unassociateRouteTableRequest.setRegionId(deleteRouteTableRequest.getRegionId());
                    unassociateRouteTableRequest.setRouteTableId(routeTable.getRouteTableId());
                    unassociateRouteTableRequest.setVSwitchId(vSwitchId);
                    this.unAssociateVSwitch(unassociateRouteTableRequest);
                }
            }

            // delete route table
            final IAcsClient client = this.acsClientStub.generateAcsClient(deleteRouteTableRequest.getRegionId());
            try {
                this.acsClientStub.request(client, deleteRouteTableRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }


            // re-check if route table has already been deleted
            if (0 != this.retrieveRouteTable(deleteRouteTableRequest.getRegionId(), deleteRouteTableRequest.getRouteTableId()).getTotalCount()) {
                String msg = String.format("The route table: [%s] from region: [%s] hasn't been deleted", deleteRouteTableRequest.getRouteTableId(), deleteRouteTableRequest.getRegionId());
                logger.error(msg);
                throw new PluginException(msg);
            }
        }
    }

    @Override
    public void associateVSwitch(List<AssociateRouteTableRequest> associateRouteTableRequestList) throws PluginException {


        for (AssociateRouteTableRequest associateRouteTableRequest : associateRouteTableRequestList) {
            final String regionId = associateRouteTableRequest.getRegionId();
            final String routeTableId = associateRouteTableRequest.getRouteTableId();
            final String vSwitchId = associateRouteTableRequest.getVSwitchId();

            if (StringUtils.isAnyEmpty(regionId, routeTableId, vSwitchId)) {
                String msg = "Either regionId, routeTableId, vSwitchID cannot be null or empty.";
                logger.error(msg);
                throw new PluginException(msg);
            }

            final IAcsClient client = this.acsClientStub.generateAcsClient(regionId);
            try {
                this.acsClientStub.request(client, associateRouteTableRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }
        }
    }

    @Override
    public UnassociateRouteTableResponse unAssociateVSwitch(UnassociateRouteTableRequest unassociateRouteTableRequest) throws PluginException {
        final String regionId = unassociateRouteTableRequest.getRegionId();
        final String routeTableId = unassociateRouteTableRequest.getRouteTableId();
        final String vSwitchId = unassociateRouteTableRequest.getVSwitchId();
        if (StringUtils.isAnyEmpty(regionId, routeTableId, vSwitchId)) {
            String msg = "Either regionId, routeTableId, vSwitchID cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }
        logger.info("Un-associating route table: [{}] with VSwitch: [{}].", routeTableId, vSwitchId);

        final IAcsClient client = this.acsClientStub.generateAcsClient(regionId);
        UnassociateRouteTableResponse response;
        try {
            response = this.acsClientStub.request(client, unassociateRouteTableRequest);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }
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
