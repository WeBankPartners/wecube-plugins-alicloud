package com.webank.wecube.plugins.alicloud.service.routeTable;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableResponseDto;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
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
            resultList.add(CoreCreateRouteTableResponseDto.fromSdk(this.acsClientStub.request(client, aliCloudRequest)));

        }
        return resultList;
    }


    @Override
    public DescribeRouteTablesResponse retrieveRouteTable(String regionId, String routeTableId) throws PluginException {
        logger.info("Retrieving route table info.\nValidating regionId field.");
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

        return this.acsClientStub.request(client, request);
    }

    @Override
    public void deleteRouteTable(List<DeleteRouteTableRequest> deleteRouteTableRequestList) throws PluginException {
        for (DeleteRouteTableRequest deleteRouteTableRequest : deleteRouteTableRequestList) {
            if (StringUtils.isAnyEmpty(deleteRouteTableRequest.getRegionId(), deleteRouteTableRequest.getRouteTableId())) {
                String msg = "The regionId or route table ID cannot be null";
                logger.error(msg);
                throw new PluginException(msg);
            }

            logger.info("Deleting VSwitch, VSwitch ID: [{}], VSwitch region:[{}]", deleteRouteTableRequest.getRouteTableId(), deleteRouteTableRequest.getRegionId());

            // check if VSwitch already deleted
            if (0 == this.retrieveRouteTable(deleteRouteTableRequest.getRegionId(), deleteRouteTableRequest.getRouteTableId()).getTotalCount()) {
                continue;
            }

            // delete VSwitch
            final IAcsClient client = this.acsClientStub.generateAcsClient(deleteRouteTableRequest.getRegionId());
            this.acsClientStub.request(client, deleteRouteTableRequest);

            // re-check if VSwitch has already been deleted
            if (0 != this.retrieveRouteTable(deleteRouteTableRequest.getRegionId(), deleteRouteTableRequest.getRouteTableId()).getTotalCount()) {
                String msg = String.format("The route table: [%s] from region: [%s] hasn't been deleted", deleteRouteTableRequest.getRouteTableId(), deleteRouteTableRequest.getRegionId());
                logger.error(msg);
                throw new PluginException(msg);
            }
        }
    }

    @Override
    public AssociateRouteTableResponse associateVSwitch(String regionId, String routeTableId, String vSwitchId) {
        if (regionId == null) {
            System.out.println("Test");
        }

        AssociateRouteTableRequest request = new AssociateRouteTableRequest();
        request.setRegionId(regionId);
        request.setRouteTableId(routeTableId);
        request.setVSwitchId(vSwitchId);

        final IAcsClient client = this.acsClientStub.generateAcsClient(regionId);

        return this.acsClientStub.request(client, request);
    }


}
