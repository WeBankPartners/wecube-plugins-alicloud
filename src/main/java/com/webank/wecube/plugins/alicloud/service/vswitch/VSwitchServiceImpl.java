package com.webank.wecube.plugins.alicloud.service.vswitch;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.CreateVSwitchRequest;
import com.aliyuncs.vpc.model.v20160428.CreateVSwitchResponse;
import com.aliyuncs.vpc.model.v20160428.DescribeVSwitchesRequest;
import com.aliyuncs.vpc.model.v20160428.DescribeVSwitchesResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreAssociateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableResponseDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreDeleteRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreDeleteVSwitchRequestDto;
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
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(request.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(request.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            final String vSwitchId = request.getvSwitchId();
            CoreCreateVSwitchResponseDto result = new CoreCreateVSwitchResponseDto();

            if (!StringUtils.isEmpty(vSwitchId)) {
                final DescribeVSwitchesResponse response = this.retrieveVSwtich(client, regionId, request.getvSwitchId());
                if (response.getTotalCount() == 1) {
                    final DescribeVSwitchesResponse.VSwitch foundVSwitch = response.getVSwitches().get(0);
                    result = new CoreCreateVSwitchResponseDto(response.getRequestId(), foundVSwitch.getVSwitchId());

                }
            } else {
                if (StringUtils.isAnyEmpty(request.getCidrBlock(), request.getVpcId(), request.getZoneId(), regionId)) {
                    String msg = "The requested field: CidrBlock, VpcId, ZoneId, RegionId cannot be null or empty";
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                // create VSwitch
                final CreateVSwitchRequest aliCloudRequest = CoreCreateVSwitchRequestDto.toSdk(request);
                aliCloudRequest.setRegionId(regionId);

                CreateVSwitchResponse createVSwitchResponse;
                try {
                    createVSwitchResponse = this.acsClientStub.request(client, aliCloudRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }


                // create route table
                CoreCreateRouteTableRequestDto routeTableRequestDto = new CoreCreateRouteTableRequestDto();
                routeTableRequestDto.setIdentityParams(request.getIdentityParams());
                routeTableRequestDto.setCloudParams(request.getCloudParams());
                routeTableRequestDto.setRegionId(regionId);
                routeTableRequestDto.setVpcId(request.getVpcId());
                final List<CoreCreateRouteTableResponseDto> createRouteTableResponseDtoList = this.routeTableService.createRouteTable(Collections.singletonList(routeTableRequestDto));
                final String createdRouteTableId = createRouteTableResponseDtoList.get(0).getRouteTableId();

                // associate route table with VSwitch
                // todo: need to optimize the timer
                try {
                    for (int i = 0; i < COUNT_DOWN_TIME; i++) {
                        final boolean ifVSwitchAvailable = this.checkIfVSwitchAvailable(client, regionId, vSwitchId);
                        final boolean ifRouteTableAvailable = this.routeTableService.checkIfRouteTableAvailable(client, regionId, createdRouteTableId);
                        if (ifVSwitchAvailable && ifRouteTableAvailable) {
                            break;
                        }
                        TimeUnit.SECONDS.sleep(1);
                    }
                } catch (InterruptedException ex) {
                    throw new PluginException(ex.getMessage());
                }

                if (!createRouteTableResponseDtoList.isEmpty()) {
                    CoreAssociateRouteTableRequestDto associateRouteTableRequest = new CoreAssociateRouteTableRequestDto();
                    associateRouteTableRequest.setRegionId(regionId);
                    associateRouteTableRequest.setRouteTableId(createdRouteTableId);
                    associateRouteTableRequest.setVSwitchId(createVSwitchResponse.getVSwitchId());

                    this.routeTableService.associateRouteTable(client, Collections.singletonList(associateRouteTableRequest));
                }

                result = CoreCreateVSwitchResponseDto.fromSdk(createVSwitchResponse);
                result.setRouteTableId(createdRouteTableId);
            }
            resultList.add(result);
        }
        return resultList;

    }

    @Override
    public DescribeVSwitchesResponse retrieveVSwtich(IAcsClient client, String regionId, String vSwitchId) throws PluginException {
        if (StringUtils.isEmpty(regionId)) {
            String msg = "The regionId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        client.shutdown();
        logger.info(String.format("Retrieving VSwitch info, region ID: [%s], VSwtich ID: [%s]", regionId, vSwitchId));

        DescribeVSwitchesResponse response;
        try {
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
    public void deleteVSwitch(List<CoreDeleteVSwitchRequestDto> coreDeleteVSwitchRequestDtoList) throws PluginException {
        for (CoreDeleteVSwitchRequestDto request : coreDeleteVSwitchRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(request.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(request.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

            logger.info("Deleting VSwitch, VSwitch ID: [{}], VSwitch region:[{}]", request.getVSwitchId(), regionId);
            if (StringUtils.isEmpty(request.getVSwitchId())) {
                throw new PluginException("The VSwitch id cannot be empty or null.");
            }

            final DescribeVSwitchesResponse retrieveVSwtichResponse = this.retrieveVSwtich(client, regionId, request.getVSwitchId());

            // check if VSwitch already deleted
            if (0 == retrieveVSwtichResponse.getTotalCount()) {
                continue;
            }

            // check if there is route table associate with given VSwitch ID
            final DescribeVSwitchesResponse.VSwitch vSwitch = retrieveVSwtichResponse.getVSwitches().get(0);
            final DescribeVSwitchesResponse.VSwitch.RouteTable associatedRouteTable = vSwitch.getRouteTable();
            final String vSwitchId = vSwitch.getVSwitchId();
            if (null != associatedRouteTable) {
                // can only un-associate and delete non-systematic route table
                logger.info(associatedRouteTable.getRouteTableType());
                if (!StringUtils.equals(AliCloudConstant.ROUTE_TABLE_TYPE_SYSTEM, associatedRouteTable.getRouteTableType())) {

                    // check VSwtich status until it's available
                    final String routeTableId = associatedRouteTable.getRouteTableId();

                    // delete route table
                    CoreDeleteRouteTableRequestDto deleteRouteTableRequest = new CoreDeleteRouteTableRequestDto();
                    deleteRouteTableRequest.setIdentityParams(request.getIdentityParams());
                    deleteRouteTableRequest.setCloudParams(request.getCloudParams());
                    deleteRouteTableRequest.setRegionId(regionId);
                    deleteRouteTableRequest.setRouteTableId(routeTableId);
                    this.routeTableService.deleteRouteTable(Collections.singletonList(deleteRouteTableRequest));
                }
            }

            // delete VSwitch
            logger.info("Deleting VSwitch: [{}]", vSwitchId);
            try {
                this.acsClientStub.request(client, request);
            } catch (AliCloudException ex) {
                throw new PluginException(ex.getMessage());
            }

            // re-check if VSwitch has already been deleted
            if (0 != this.retrieveVSwtich(client, regionId, request.getVSwitchId()).getTotalCount()) {
                String msg = String.format("The VSwitch: [%s] from region: [%s] hasn't been deleted", request.getVSwitchId(), regionId);
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
