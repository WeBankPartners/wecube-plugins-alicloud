package com.webank.wecube.plugins.alicloud.service.routeTable;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableResponseDto;
import com.webank.wecube.plugins.alicloud.service.AbstractAliCloudService;
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
public class RouteTableServiceImpl extends AbstractAliCloudService<CoreCreateRouteTableRequestDto, DeleteRouteTableRequest> implements RouteTableService {
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

            fieldCheck(request);

            final IAcsClient client = this.acsClientStub.generateAcsClient(request.getRegionId());
            final CreateRouteTableRequest aliCloudRequest = CoreCreateRouteTableRequestDto.toSdk(request);
            resultList.add(CoreCreateRouteTableResponseDto.fromSdk(this.acsClientStub.request(client, aliCloudRequest)));

        }
        return resultList;
    }


    @Override
    public DescribeRouteTablesResponse retrieveRouteTable(String regionId, String routeTableId) throws PluginException {
        regionIdCheck(regionId);

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
            logger.info("Deleting VSwitch, VSwitch ID: [{}], VSwitch region:[{}]", deleteRouteTableRequest.getRouteTableId(), deleteRouteTableRequest.getRegionId());
            if (StringUtils.isEmpty(deleteRouteTableRequest.getRouteTableId())) {
                throw new PluginException("The VSwitch id cannot be empty or null.");
            }

            fieldCheck(deleteRouteTableRequest);

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

    @Override
    public void fieldCheck(CoreCreateRouteTableRequestDto request) throws PluginException {
        final boolean isRegionIdEmpty = StringUtils.isEmpty(request.getRegionId());
        final boolean isVpcIdEmpty = StringUtils.isEmpty(request.getVpcId());

        if (isRegionIdEmpty || isVpcIdEmpty) {
            String msg = "The requested field cannot be empty or null.";
            logger.error(msg);
            throw new PluginException(msg);
        }
    }

    @Override
    public void fieldCheck(DeleteRouteTableRequest request) throws PluginException {
        final boolean isRegionIdEmpty = StringUtils.isEmpty(request.getRegionId());
        final boolean isRouteTableIdEmpty = StringUtils.isEmpty(request.getRouteTableId());

        if (isRegionIdEmpty || isRouteTableIdEmpty) {
            String msg = "The requested field cannot be empty or null.";
            logger.error(msg);
            throw new PluginException(msg);
        }
    }


}
