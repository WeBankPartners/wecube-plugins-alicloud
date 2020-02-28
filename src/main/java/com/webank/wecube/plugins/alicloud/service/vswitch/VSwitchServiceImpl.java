package com.webank.wecube.plugins.alicloud.service.vswitch;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.service.routeTable.RouteTableService;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudConstant;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author howechen
 */
@Service
public class VSwitchServiceImpl implements VSwitchService {

    public static int COUNT_DOWN_TIME = 5;
    private static Logger logger = LoggerFactory.getLogger(VSwitchService.class);

    private AcsClientStub acsClientStub;
    private RouteTableService routeTableService;

    @Autowired
    public VSwitchServiceImpl(AcsClientStub acsClientStub, RouteTableService routeTableService) {
        this.acsClientStub = acsClientStub;
        this.routeTableService = routeTableService;
    }


    @Override
    public List<CoreCreateVSwitchResponseDto> createVSwitch(List<CoreCreateVSwitchRequestDto> coreCreateVSwitchRequestDtoList) throws PluginException {
        List<CoreCreateVSwitchResponseDto> resultList = new ArrayList<>();
        for (CoreCreateVSwitchRequestDto request : coreCreateVSwitchRequestDtoList) {
            final String vSwitchId = request.getvSwitchId();
            if (!StringUtils.isEmpty(vSwitchId)) {
                final DescribeVSwitchesResponse response = this.retrieveVSwtich(request.getRegionId(), request.getvSwitchId());
                if (response.getTotalCount() == 1) {
                    final DescribeVSwitchesResponse.VSwitch foundVSwitch = response.getVSwitches().get(0);
                    resultList.add(new CoreCreateVSwitchResponseDto(response.getRequestId(), foundVSwitch.getVSwitchId()));

                }
                continue;
            }

            if (StringUtils.isAnyEmpty(request.getCidrBlock(), request.getVpcId(), request.getZoneId(), request.getRegionId())) {
                String msg = "The requested field: CidrBlock, VpcId, ZoneId, RegionId cannot be null or empty";
                logger.error(msg);
                throw new PluginException(msg);
            }

            final IAcsClient client = this.acsClientStub.generateAcsClient(request.getRegionId());

            // create VSwitch
            final CreateVSwitchRequest aliCloudRequest = CoreCreateVSwitchRequestDto.toSdk(request);
            CreateVSwitchResponse createVSwitchResponse;
            try {
                createVSwitchResponse = this.acsClientStub.request(client, aliCloudRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }


            // create route table
            CoreCreateRouteTableRequestDto routeTableRequestDto = new CoreCreateRouteTableRequestDto();
            routeTableRequestDto.setRegionId(request.getRegionId());
            routeTableRequestDto.setVpcId(request.getVpcId());
            final List<CoreCreateRouteTableResponseDto> createRouteTableResponseDtoList = this.routeTableService.createRouteTable(Collections.singletonList(routeTableRequestDto));
            final String createdRouteTableId = createRouteTableResponseDtoList.get(0).getRouteTableId();

            // associate route table with VSwitch
            // todo: need to optimize the timer
            try {
                for (int i = 0; i < COUNT_DOWN_TIME; i++) {
                    final boolean ifVSwitchAvailable = this.checkIfVSwitchAvailable(client, request.getRegionId(), vSwitchId);
                    final boolean ifRouteTableAvailable = this.routeTableService.checkIfRouteTableAvailable(client, request.getRegionId(), createdRouteTableId);
                    if (ifVSwitchAvailable && ifRouteTableAvailable) {
                        break;
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException ex) {
                throw new PluginException(ex.getMessage());
            }

            if (!createRouteTableResponseDtoList.isEmpty()) {
                AssociateRouteTableRequest associateRouteTableRequest = new AssociateRouteTableRequest();
                associateRouteTableRequest.setRegionId(request.getRegionId());
                associateRouteTableRequest.setRouteTableId(createdRouteTableId);
                associateRouteTableRequest.setVSwitchId(createVSwitchResponse.getVSwitchId());
                this.routeTableService.associateVSwitch(Collections.singletonList(associateRouteTableRequest));
            }

            CoreCreateVSwitchResponseDto result = CoreCreateVSwitchResponseDto.fromSdk(createVSwitchResponse);
            result.setRouteTableId(createdRouteTableId);

            resultList.add(result);

        }
        return resultList;

    }

    @Override
    public DescribeVSwitchesResponse retrieveVSwtich(String regionId, String vSwitchId) throws PluginException {
        if (StringUtils.isEmpty(regionId)) {
            String msg = "The regionId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        logger.info(String.format("Retrieving VSwitch info, region ID: [%s], VSwtich ID: [%s]", regionId, vSwitchId));

        DescribeVSwitchesResponse response;
        try {
            final IAcsClient client = this.acsClientStub.generateAcsClient(regionId);
            DescribeVSwitchesRequest request = new DescribeVSwitchesRequest();
            request.setRegionId(regionId);
            request.setVSwitchId(vSwitchId);
            response = this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }


        return response;
    }

    @Override
    public void deleteVSwitch(List<DeleteVSwitchRequest> deleteVSwitchRequestList) throws PluginException {
        for (DeleteVSwitchRequest deleteVSwitchRequest : deleteVSwitchRequestList) {
            logger.info("Deleting VSwitch, VSwitch ID: [{}], VSwitch region:[{}]", deleteVSwitchRequest.getVSwitchId(), deleteVSwitchRequest.getRegionId());
            if (StringUtils.isEmpty(deleteVSwitchRequest.getVSwitchId())) {
                throw new PluginException("The VSwitch id cannot be empty or null.");
            }

            final DescribeVSwitchesResponse retrieveVSwtichResponse = this.retrieveVSwtich(deleteVSwitchRequest.getRegionId(), deleteVSwitchRequest.getVSwitchId());

            // check if VSwitch already deleted
            if (0 == retrieveVSwtichResponse.getTotalCount()) {
                continue;
            }

            // check if there is route table associate with given VSwitch ID
            final DescribeVSwitchesResponse.VSwitch vSwitch = retrieveVSwtichResponse.getVSwitches().get(0);
            final DescribeVSwitchesResponse.VSwitch.RouteTable associatedRouteTable = vSwitch.getRouteTable();
            if (null != associatedRouteTable) {
                // can only un-associate and delete non-systematic route table
                if (!StringUtils.equals(AliCloudConstant.ROUTE_TABLE_TYPE_SYSTEM, associatedRouteTable.getRouteTableType())) {
                    final String routeTableId = associatedRouteTable.getRouteTableId();

                    // un-associate route table with VSwitch
                    UnassociateRouteTableRequest unassociateRouteTableRequest = new UnassociateRouteTableRequest();
                    unassociateRouteTableRequest.setRegionId(deleteVSwitchRequest.getRegionId());
                    unassociateRouteTableRequest.setRouteTableId(routeTableId);
                    unassociateRouteTableRequest.setVSwitchId(vSwitch.getVSwitchId());
                    this.routeTableService.unAssociateVSwitch(unassociateRouteTableRequest);

                    // delete route table
                    DeleteRouteTableRequest deleteRouteTableRequest = new DeleteRouteTableRequest();
                    deleteRouteTableRequest.setRegionId(deleteVSwitchRequest.getRegionId());
                    deleteRouteTableRequest.setRouteTableId(routeTableId);
                    this.routeTableService.deleteRouteTable(Collections.singletonList(deleteRouteTableRequest));
                }

            }

            // delete VSwitch
            logger.info("Deleting VSwitch: [{}]", vSwitch.getVSwitchId());
            try {
                final IAcsClient client = this.acsClientStub.generateAcsClient(deleteVSwitchRequest.getRegionId());
                this.acsClientStub.request(client, deleteVSwitchRequest);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }


            // re-check if VSwitch has already been deleted
            if (0 != this.retrieveVSwtich(deleteVSwitchRequest.getRegionId(), deleteVSwitchRequest.getVSwitchId()).getTotalCount()) {
                String msg = String.format("The VSwitch: [%s] from region: [%s] hasn't been deleted", deleteVSwitchRequest.getVSwitchId(), deleteVSwitchRequest.getRegionId());
                logger.error(msg);
                throw new PluginException(msg);
            }
        }
    }

    public boolean checkIfVSwitchAvailable(IAcsClient client, String regionId, String vSwitchId) throws PluginException {
        logger.info("Retrieving VSwitch status info.\nValidating regionId field.");
        if (StringUtils.isEmpty(regionId)) {
            String msg = "The regionId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        DescribeVSwitchesResponse response;
        try {
            DescribeVSwitchesRequest request = new DescribeVSwitchesRequest();
            request.setRegionId(regionId);
            request.setVSwitchId(vSwitchId);
            response = this.acsClientStub.request(client, request);
        } catch (AliCloudException ex) {
            throw new PluginException(ex.getMessage());
        }

        return StringUtils.equals(AliCloudConstant.RESOURCE_AVAILABLE_STATUS, response.getVSwitches().get(0).getStatus());
    }
}
