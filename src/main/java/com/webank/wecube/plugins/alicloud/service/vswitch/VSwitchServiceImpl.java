package com.webank.wecube.plugins.alicloud.service.vswitch;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableRequestDto;
import com.webank.wecube.plugins.alicloud.dto.routeTable.CoreCreateRouteTableResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.service.AbstractAliCloudService;
import com.webank.wecube.plugins.alicloud.service.routeTable.RouteTableService;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author howechen
 */
@Service
public class VSwitchServiceImpl extends AbstractAliCloudService<CoreCreateVSwitchRequestDto, DeleteVSwitchRequest> implements VSwitchService {
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

            fieldCheck(request);

            final IAcsClient client = this.acsClientStub.generateAcsClient(request.getRegionId());

            // create VSwitch
            final CreateVSwitchRequest aliCloudRequest = CoreCreateVSwitchRequestDto.toSdk(request);
            final CreateVSwitchResponse createVSwitchResponse = this.acsClientStub.request(client, aliCloudRequest);

            // create route table
            CoreCreateRouteTableRequestDto routeTableRequestDto = new CoreCreateRouteTableRequestDto();
            routeTableRequestDto.setSysRegionId(request.getRegionId());
            routeTableRequestDto.setVpcId(request.getVpcId());
            final List<CoreCreateRouteTableResponseDto> createRouteTableResponseDtoList = this.routeTableService.createRouteTable(Collections.singletonList(routeTableRequestDto));

            // associate route table with VSwitch
            if (!createRouteTableResponseDtoList.isEmpty()) {
                final String createdRouteTableId = createRouteTableResponseDtoList.get(0).getRouteTableId();
                this.routeTableService.associateVSwitch(request.getRegionId(), createdRouteTableId, createVSwitchResponse.getVSwitchId());
            }

            resultList.add(CoreCreateVSwitchResponseDto.fromSdk(createVSwitchResponse));

        }
        return resultList;

    }

    @Override
    public DescribeVSwitchesResponse retrieveVSwtich(String regionId, String vSwitchId) throws PluginException {
        regionIdCheck(regionId);

        logger.info(String.format("Retrieving VSwitch info, region ID: [%s], VSwtich ID: [%s]", regionId, vSwitchId));

        final IAcsClient client = this.acsClientStub.generateAcsClient(regionId);
        DescribeVSwitchesRequest request = new DescribeVSwitchesRequest();
        request.setRegionId(regionId);
        request.setVSwitchId(vSwitchId);

        return this.acsClientStub.request(client, request);
    }

    @Override
    public void deleteVSwitch(List<DeleteVSwitchRequest> deleteVSwitchRequestList) throws PluginException {
        for (DeleteVSwitchRequest deleteVSwitchRequest : deleteVSwitchRequestList) {
            logger.info("Deleting VSwitch, VSwitch ID: [{}], VSwitch region:[{}]", deleteVSwitchRequest.getVSwitchId(), deleteVSwitchRequest.getRegionId());
            if (StringUtils.isEmpty(deleteVSwitchRequest.getVSwitchId())) {
                throw new PluginException("The VSwitch id cannot be empty or null.");
            }

            // check if VSwitch already deleted
            if (0 == this.retrieveVSwtich(deleteVSwitchRequest.getRegionId(), deleteVSwitchRequest.getVSwitchId()).getTotalCount()) {
                continue;
            }

            // delete VSwitch
            final IAcsClient client = this.acsClientStub.generateAcsClient(deleteVSwitchRequest.getRegionId());
            this.acsClientStub.request(client, deleteVSwitchRequest);

            // re-check if VSwitch has already been deleted
            if (0 != this.retrieveVSwtich(deleteVSwitchRequest.getRegionId(), deleteVSwitchRequest.getVSwitchId()).getTotalCount()) {
                String msg = String.format("The VSwitch: [%s] from region: [%s] hasn't been deleted", deleteVSwitchRequest.getVSwitchId(), deleteVSwitchRequest.getRegionId());
                logger.error(msg);
                throw new PluginException(msg);
            }
        }
    }


    @Override
    public void fieldCheck(CoreCreateVSwitchRequestDto requestDto) throws PluginException {
        final boolean isCidrBlockEmpty = StringUtils.isEmpty(requestDto.getCidrBlock());
        final boolean isVpcIdEmpty = StringUtils.isEmpty(requestDto.getVpcId());
        final boolean isZoneIdEmpty = StringUtils.isEmpty(requestDto.getZoneId());
        final boolean isRegionIdEmpty = StringUtils.isEmpty(requestDto.getRegionId());
        if (isCidrBlockEmpty || isVpcIdEmpty || isZoneIdEmpty || isRegionIdEmpty) {
            String msg = String.format("Four fields, cidr block: [%s], vpc ID: [%s], zone ID: [%s], region ID: [%s] cannot be empty or null while creating VSwitch", requestDto.getCidrBlock(), requestDto.getVpcId(), requestDto.getZoneId(), requestDto.getRegionId());
            logger.error(msg);
            throw new PluginException(msg);
        }
    }
}
