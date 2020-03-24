package com.webank.wecube.plugins.alicloud.service.vpc.vswitch;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.CoreAssociateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.CoreCreateRouteTableResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.routeTable.CoreDeleteRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreCreateVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreCreateVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreDeleteVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreDeleteVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.service.vpc.routeTable.RouteTableService;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudConstant;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimer;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimerTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
    public List<CoreCreateVSwitchResponseDto> createVSwitch(List<CoreCreateVSwitchRequestDto> requestDtoList) {
        List<CoreCreateVSwitchResponseDto> resultList = new ArrayList<>();
        for (CoreCreateVSwitchRequestDto requestDto : requestDtoList) {
            CoreCreateVSwitchResponseDto result = new CoreCreateVSwitchResponseDto();
            try {
                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                final String vSwitchId = requestDto.getvSwitchId();

                if (!StringUtils.isEmpty(vSwitchId)) {
                    final DescribeVSwitchesResponse response = this.retrieveVSwitch(client, regionId, requestDto.getvSwitchId());
                    if (response.getTotalCount() == 1) {
                        final DescribeVSwitchesResponse.VSwitch foundVSwitch = response.getVSwitches().get(0);
                        result = result.fromSdkCrossLineage(foundVSwitch);
                        continue;
                    }
                }

                if (StringUtils.isAnyEmpty(requestDto.getCidrBlock(), requestDto.getVpcId(), requestDto.getZoneId(), regionId)) {
                    String msg = "The requested field: CidrBlock, VpcId, ZoneId, RegionId cannot be null or empty";
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                // create VSwitch
                final CreateVSwitchRequest aliCloudRequest = requestDto.toSdk(requestDto);
                aliCloudRequest.setRegionId(regionId);

                CreateVSwitchResponse createVSwitchResponse;
                createVSwitchResponse = this.acsClientStub.request(client, aliCloudRequest);


                // create route table
                CoreCreateRouteTableRequestDto routeTableRequestDto = new CoreCreateRouteTableRequestDto();
                routeTableRequestDto.setIdentityParams(requestDto.getIdentityParams());
                routeTableRequestDto.setCloudParams(requestDto.getCloudParams());
                routeTableRequestDto.setRegionId(regionId);
                routeTableRequestDto.setVpcId(requestDto.getVpcId());
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

                result = result.fromSdk(createVSwitchResponse);
                result.setRouteTableId(createdRouteTableId);
            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }

        }
        return resultList;

    }

    @Override
    public DescribeVSwitchesResponse retrieveVSwitch(IAcsClient client, String regionId, String vSwitchId) throws PluginException, AliCloudException {
        if (StringUtils.isAnyEmpty(regionId, vSwitchId)) {
            String msg = "Either the regionId or vSwitchId cannot be null or empty.";
            throw new PluginException(msg);
        }

        logger.info(String.format("Retrieving VSwitch info, region ID: [%s], VSwtich ID: [%s]", regionId, vSwitchId));

        DescribeVSwitchesResponse response;
        DescribeVSwitchesRequest request = new DescribeVSwitchesRequest();
        request.setRegionId(regionId);
        request.setVSwitchId(vSwitchId);
        response = this.acsClientStub.request(client, request);

        return response;
    }

    @Override
    public List<CoreDeleteVSwitchResponseDto> deleteVSwitch(List<CoreDeleteVSwitchRequestDto> requestDtoList) {
        List<CoreDeleteVSwitchResponseDto> resultList = new ArrayList<>();
        for (CoreDeleteVSwitchRequestDto requestDto : requestDtoList) {
            CoreDeleteVSwitchResponseDto result = new CoreDeleteVSwitchResponseDto();
            try {
                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                final String vSwitchId = requestDto.getVSwitchId();
                logger.info("Deleting VSwitch, VSwitch ID: [{}], VSwitch region:[{}]", vSwitchId, regionId);
                if (StringUtils.isEmpty(vSwitchId)) {
                    throw new PluginException("The VSwitch id cannot be empty or null.");
                }

                final DescribeVSwitchesResponse retrieveVSwtichResponse = this.retrieveVSwitch(client, regionId, vSwitchId);

                // check if VSwitch already deleted
                if (0 == retrieveVSwtichResponse.getTotalCount()) {
                    continue;
                }

                // check if there is route table associate with given VSwitch ID
                final DescribeVSwitchesResponse.VSwitch vSwitch = retrieveVSwtichResponse.getVSwitches().get(0);
                final DescribeVSwitchesResponse.VSwitch.RouteTable associatedRouteTable = vSwitch.getRouteTable();
                final String foundVSwitchId = vSwitch.getVSwitchId();
                if (null != associatedRouteTable) {
                    // can only un-associate and delete non-systematic route table
                    logger.info(associatedRouteTable.getRouteTableType());
                    if (!StringUtils.equals(AliCloudConstant.ROUTE_TABLE_TYPE_SYSTEM, associatedRouteTable.getRouteTableType())) {

                        // check VSwtich status until it's available
                        final String routeTableId = associatedRouteTable.getRouteTableId();

                        Function<?, Boolean> func = o -> this.routeTableService.checkIfRouteTableAvailable(client, regionId, routeTableId);
                        final PluginTimerTask checkRouteTableStatusTask = new PluginTimerTask(func);
                        PluginTimer.runTask(checkRouteTableStatusTask);

                        // un-associate route table and vSwitch
                        UnassociateRouteTableRequest unassociateRouteTableRequest = new UnassociateRouteTableRequest();
                        unassociateRouteTableRequest.setRegionId(regionId);
                        unassociateRouteTableRequest.setRouteTableId(routeTableId);
                        unassociateRouteTableRequest.setVSwitchId(foundVSwitchId);
                        this.routeTableService.unAssociateRouteTable(client, unassociateRouteTableRequest);

                        PluginTimer.runTask(checkRouteTableStatusTask);

                        // delete route table
                        CoreDeleteRouteTableRequestDto deleteRouteTableRequest = new CoreDeleteRouteTableRequestDto();
                        deleteRouteTableRequest.setIdentityParams(requestDto.getIdentityParams());
                        deleteRouteTableRequest.setCloudParams(requestDto.getCloudParams());
                        deleteRouteTableRequest.setRegionId(regionId);
                        deleteRouteTableRequest.setRouteTableId(routeTableId);
                        this.routeTableService.deleteRouteTable(Collections.singletonList(deleteRouteTableRequest));
                    }
                }

                // delete VSwitch
                logger.info("Deleting VSwitch: [{}]", foundVSwitchId);
                final DeleteVSwitchRequest deleteVSwitchRequest = requestDto.toSdk(requestDto);
                deleteVSwitchRequest.setRegionId(regionId);
                this.acsClientStub.request(client, deleteVSwitchRequest);

                // re-check if VSwitch has already been deleted
                if (0 != this.retrieveVSwitch(client, regionId, foundVSwitchId).getTotalCount()) {
                    String msg = String.format("The VSwitch: [%s] from region: [%s] hasn't been deleted", foundVSwitchId, regionId);
                    logger.error(msg);
                    throw new PluginException(msg);
                }
            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                resultList.add(result);
            }
        }
        return resultList;
    }

    public boolean checkIfVSwitchAvailable(IAcsClient client, String regionId, String vSwitchId) throws PluginException, AliCloudException {
        logger.info("Retrieving VSwitch status info.\nValidating regionId field.");
        if (StringUtils.isEmpty(regionId)) {
            String msg = "The regionId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        DescribeVSwitchesResponse response;
        DescribeVSwitchesRequest request = new DescribeVSwitchesRequest();
        request.setRegionId(regionId);
        request.setVSwitchId(vSwitchId);
        response = this.acsClientStub.request(client, request);

        return StringUtils.equals(AliCloudConstant.RESOURCE_AVAILABLE_STATUS, response.getVSwitches().get(0).getStatus());
    }
}
