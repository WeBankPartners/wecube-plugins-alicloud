package com.webank.wecube.plugins.alicloud.service.vpc.vswitch;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreCreateVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreCreateVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreDeleteVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreDeleteVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.service.vpc.routeTable.RouteTableService;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import com.webank.wecube.plugins.alicloud.support.AliCloudConstant;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;
import com.webank.wecube.plugins.alicloud.support.DtoValidator;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimer;
import com.webank.wecube.plugins.alicloud.support.timer.PluginTimerTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author howechen
 */
@Service
public class VSwitchServiceImpl implements VSwitchService {

    private static final Logger logger = LoggerFactory.getLogger(VSwitchService.class);

    private final AcsClientStub acsClientStub;
    private final RouteTableService routeTableService;
    private final DtoValidator dtoValidator;

    @Autowired
    public VSwitchServiceImpl(AcsClientStub acsClientStub, RouteTableService routeTableService, DtoValidator dtoValidator) {
        this.acsClientStub = acsClientStub;
        this.routeTableService = routeTableService;
        this.dtoValidator = dtoValidator;
    }


    @Override
    public List<CoreCreateVSwitchResponseDto> createVSwitch(List<CoreCreateVSwitchRequestDto> requestDtoList) {
        List<CoreCreateVSwitchResponseDto> resultList = new ArrayList<>();
        for (CoreCreateVSwitchRequestDto requestDto : requestDtoList) {
            CoreCreateVSwitchResponseDto result = new CoreCreateVSwitchResponseDto();
            try {

                dtoValidator.validate(requestDto);

                logger.info("Creating VSwitch with info: {}", requestDto.toString());

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                final String vSwitchId = requestDto.getvSwitchId();

                if (!StringUtils.isEmpty(vSwitchId)) {
                    final DescribeVSwitchesResponse response = this.retrieveVSwitch(client, regionId, vSwitchId);
                    if (response.getTotalCount() == 1) {
                        final DescribeVSwitchesResponse.VSwitch foundVSwitch = response.getVSwitches().get(0);
                        result = result.fromSdkCrossLineage(foundVSwitch);
                        result.setRequestId(response.getRequestId());
                        continue;
                    }
                }

                // create VSwitch
                final CreateVSwitchRequest aliCloudRequest = requestDto.toSdk();
                aliCloudRequest.setRegionId(regionId);

                CreateVSwitchResponse createVSwitchResponse;
                createVSwitchResponse = this.acsClientStub.request(client, aliCloudRequest);


                // create route table
                CreateRouteTableRequest createRouteTableRequest = new CreateRouteTableRequest();
                createRouteTableRequest.setRegionId(regionId);
                createRouteTableRequest.setVpcId(requestDto.getVpcId());
                final CreateRouteTableResponse createRouteTableResponse = this.routeTableService.createRouteTable(client, createRouteTableRequest);
                final String createdRouteTableId = createRouteTableResponse.getRouteTableId();

                // wait till both route table and vSwitch are available to be configured
                Function<?, Boolean> func = this.ifBothRouteTableAndVSwitchAvailable(client, regionId, createdRouteTableId, vSwitchId);
                final PluginTimerTask checkRouteTableStatusTask = new PluginTimerTask(func);
                PluginTimer.runTask(checkRouteTableStatusTask);

                // associate route table with VSwitch
                AssociateRouteTableRequest associateRouteTableRequest = new AssociateRouteTableRequest();
                associateRouteTableRequest.setRegionId(regionId);
                associateRouteTableRequest.setRouteTableId(createdRouteTableId);
                associateRouteTableRequest.setVSwitchId(createVSwitchResponse.getVSwitchId());

                this.routeTableService.associateRouteTable(client, associateRouteTableRequest);

                result = result.fromSdk(createVSwitchResponse);
                result.setRouteTableId(createdRouteTableId);
            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
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

                dtoValidator.validate(requestDto);

                final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
                final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
                final String regionId = cloudParamDto.getRegionId();
                final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);

                final String vSwitchId = requestDto.getVSwitchId();
                logger.info("Deleting VSwitch with info: {}", requestDto.toString());

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

                        // wait till both route table and vSwitch are available to be configured
                        Function<?, Boolean> func = this.ifBothRouteTableAndVSwitchAvailable(client, regionId, routeTableId, foundVSwitchId);
                        final PluginTimerTask checkRouteTableStatusTask = new PluginTimerTask(func);
                        PluginTimer.runTask(checkRouteTableStatusTask);

                        // un-associate route table and vSwitch
                        UnassociateRouteTableRequest unassociateRouteTableRequest = new UnassociateRouteTableRequest();
                        unassociateRouteTableRequest.setRegionId(regionId);
                        unassociateRouteTableRequest.setRouteTableId(routeTableId);
                        unassociateRouteTableRequest.setVSwitchId(foundVSwitchId);
                        this.routeTableService.unAssociateRouteTable(client, unassociateRouteTableRequest);

                        // wait till both route table and vSwitch are available to be configured
                        PluginTimer.runTask(checkRouteTableStatusTask);

                        // delete route table
                        DeleteRouteTableRequest deleteRouteTableRequest = new DeleteRouteTableRequest();
                        deleteRouteTableRequest.setRegionId(regionId);
                        deleteRouteTableRequest.setRouteTableId(routeTableId);
                        this.routeTableService.deleteRouteTable(client, deleteRouteTableRequest);
                    }
                }

                // delete VSwitch
                logger.info("Deleting VSwitch: [{}]", foundVSwitchId);
                final DeleteVSwitchRequest deleteVSwitchRequest = requestDto.toSdk();
                deleteVSwitchRequest.setRegionId(regionId);
                final DeleteVSwitchResponse deleteVSwitchResponse = this.acsClientStub.request(client, deleteVSwitchRequest);

                // re-check if VSwitch has already been deleted
                if (0 != this.retrieveVSwitch(client, regionId, foundVSwitchId).getTotalCount()) {
                    String msg = String.format("The VSwitch: [%s] from region: [%s] hasn't been deleted", foundVSwitchId, regionId);
                    logger.error(msg);
                    throw new PluginException(msg);
                }

                result.setRequestId(deleteVSwitchResponse.getRequestId());
            } catch (PluginException | AliCloudException ex) {
                result.setErrorCode(CoreResponseDto.STATUS_ERROR);
                result.setErrorMessage(ex.getMessage());
            } finally {
                result.setGuid(requestDto.getGuid());
                result.setCallbackParameter(requestDto.getCallbackParameter());
                logger.info("Result: {}", result.toString());
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

    private Function<?, Boolean> ifBothRouteTableAndVSwitchAvailable(IAcsClient client, String regionId, String routeTableId, String vSwitchId) {
        return o -> {
            boolean ifRouteTableAvailable = this.routeTableService.checkIfRouteTableAvailable(client, regionId, routeTableId);
            boolean ifVSwitchAvailable = this.checkIfVSwitchAvailable(client, regionId, vSwitchId);
            return ifRouteTableAvailable && ifVSwitchAvailable;
        };
    }
}
