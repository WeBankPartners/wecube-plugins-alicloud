package com.webank.wecube.plugins.alicloud.service.vpc;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcResponseDto;
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
public class VpcServiceImpl implements VpcService {

    private static Logger logger = LoggerFactory.getLogger(VpcService.class);

    private AcsClientStub acsClientStub;

    @Autowired
    public VpcServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    @Override
    public List<CoreCreateVpcResponseDto> createVpc(List<CoreCreateVpcRequestDto> vpcCreateVpcRequestDto) {
        List<CoreCreateVpcResponseDto> resultList = new ArrayList<>();
        for (CoreCreateVpcRequestDto coreCreateVpcRequestDto : vpcCreateVpcRequestDto) {
            logger.info("Sending create VPC request: {} to AliCloud.", coreCreateVpcRequestDto.toString());
            // check region id
            final String regionId = coreCreateVpcRequestDto.getRegionId();
            if (StringUtils.isEmpty(regionId)) {
                throw new PluginException("The region id cannot be NULL or empty.");
            }

            // if vpc id is not empty, retrieve vpc info
            final String vpcId = coreCreateVpcRequestDto.getVpcId();
            if (!StringUtils.isEmpty(vpcId)) {
                final DescribeVpcsResponse response = this.retrieveVpc(regionId, vpcId);
                if (response.getVpcs().size() == 1) {
                    final DescribeVpcsResponse.Vpc foundVpc = response.getVpcs().get(0);
                    resultList.add(CoreCreateVpcResponseDto.fromSdk(foundVpc));
                }
                continue;
            }

            // if vpc id is empty, create vpc
            final IAcsClient client = this.acsClientStub.generateAcsClient(regionId);
            final CreateVpcRequest createVpcRequest = CoreCreateVpcRequestDto.toSdk(coreCreateVpcRequestDto);
            CreateVpcResponse response = this.acsClientStub.request(client, createVpcRequest);

            resultList.add(CoreCreateVpcResponseDto.fromSdk(response));
        }
        return resultList;
    }

    @Override
    public DescribeVpcsResponse retrieveVpc(String regionId, String vpcId) throws PluginException {
        logger.info("Retrieving VPC info.\nValidating regionId field.");
        if (StringUtils.isEmpty(regionId)) {
            String msg = "The regionId cannot be null or empty.";
            logger.error(msg);
            throw new PluginException(msg);
        }

        logger.info("Retrieving VPC info, the region ID: [{}], the vpc ID: [{}].", regionId, vpcId);


        final IAcsClient client = this.acsClientStub.generateAcsClient(regionId);
        // create new request
        DescribeVpcsRequest describeVpcsRequest = new DescribeVpcsRequest();
        describeVpcsRequest.setRegionId(regionId);
        describeVpcsRequest.setVpcId(vpcId);
        // send the request and handle the error, then return the response
        return this.acsClientStub.request(client, describeVpcsRequest);
    }

    @Override
    public void deleteVpc(List<DeleteVpcRequest> deleteVpcRequestList) throws PluginException {
        for (DeleteVpcRequest deleteVpcRequest : deleteVpcRequestList) {
            logger.info("Deleting VPC, VPC ID: [{}], VPC region:[{}]", deleteVpcRequest.getVpcId(), deleteVpcRequest.getRegionId());
            if (StringUtils.isEmpty(deleteVpcRequest.getVpcId())) {
                throw new PluginException("The VPC id cannot be empty or null.");
            }

            // check if VPC already deleted
            if (0 == this.retrieveVpc(deleteVpcRequest.getRegionId(), deleteVpcRequest.getVpcId()).getTotalCount()) {
                continue;
            }

            // delete VPC
            final IAcsClient client = this.acsClientStub.generateAcsClient(deleteVpcRequest.getRegionId());
            this.acsClientStub.request(client, deleteVpcRequest);

            // re-check if VPC has already been deleted
            if (0 != this.retrieveVpc(deleteVpcRequest.getRegionId(), deleteVpcRequest.getVpcId()).getTotalCount()) {
                String msg = String.format("The VPC: [%s] from region: [%s] hasn't been deleted", deleteVpcRequest.getVpcId(), deleteVpcRequest.getRegionId());
                logger.error(msg);
                throw new PluginException(msg);
            }
        }
    }
}
