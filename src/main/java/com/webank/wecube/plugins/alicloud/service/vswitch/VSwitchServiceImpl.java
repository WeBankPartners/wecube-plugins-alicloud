package com.webank.wecube.plugins.alicloud.service.vswitch;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.CreateVSwitchRequest;
import com.aliyuncs.vpc.model.v20160428.DeleteVSwitchRequest;
import com.aliyuncs.vpc.model.v20160428.DescribeVSwitchesRequest;
import com.aliyuncs.vpc.model.v20160428.DescribeVSwitchesResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VSwitchServiceImpl implements VSwitchService {
    private static Logger logger = LoggerFactory.getLogger(VSwitchService.class);

    private AcsClientStub acsClientStub;

    @Autowired
    public VSwitchServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
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

            createVSwitchFieldCheck(request);

            final IAcsClient client = this.acsClientStub.generateAcsClient(request.getRegionId());
            final CreateVSwitchRequest aliCloudRequest = CoreCreateVSwitchRequestDto.toSdk(request);
            resultList.add(CoreCreateVSwitchResponseDto.fromSdk(this.acsClientStub.request(client, aliCloudRequest)));

        }
        return resultList;

    }

    @Override
    public DescribeVSwitchesResponse retrieveVSwtich(String regionId, String vSwitchId) throws PluginException {

        if (StringUtils.isEmpty(regionId)) {
            final String msg = String.format("The region ID cannot be empty, your region id is: [%s]", regionId);
            logger.error(msg);
            throw new PluginException(msg);
        }

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

    private void createVSwitchFieldCheck(CoreCreateVSwitchRequestDto request) throws PluginException {
        final boolean isCidrBlockEmpty = StringUtils.isEmpty(request.getCidrBlock());
        final boolean isVpcIdEmpty = StringUtils.isEmpty(request.getVpcId());
        final boolean isZoneIdEmpty = StringUtils.isEmpty(request.getZoneId());
        final boolean isRegionIdEmpty = StringUtils.isEmpty(request.getRegionId());
        if (isCidrBlockEmpty || isVpcIdEmpty || isZoneIdEmpty || isRegionIdEmpty) {
            String msg = String.format("Four fields, cidr block: [%s], vpc ID: [%s], zone ID: [%s], region ID: [%s] cannot be empty or null while creating VSwitch", request.getCidrBlock(), request.getVpcId(), request.getZoneId(), request.getRegionId());
            logger.error(msg);
            throw new PluginException(msg);
        }
    }


}
