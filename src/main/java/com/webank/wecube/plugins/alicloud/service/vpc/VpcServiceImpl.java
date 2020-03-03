package com.webank.wecube.plugins.alicloud.service.vpc;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.CreateVpcRequest;
import com.aliyuncs.vpc.model.v20160428.CreateVpcResponse;
import com.aliyuncs.vpc.model.v20160428.DescribeVpcsRequest;
import com.aliyuncs.vpc.model.v20160428.DescribeVpcsResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreDeleteVpcRequestDto;
import com.webank.wecube.plugins.alicloud.support.AcsClientStub;
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
public class VpcServiceImpl implements VpcService {

    private static Logger logger = LoggerFactory.getLogger(VpcService.class);

    private AcsClientStub acsClientStub;

    @Autowired
    public VpcServiceImpl(AcsClientStub acsClientStub) {
        this.acsClientStub = acsClientStub;
    }

    @Override
    public List<CoreCreateVpcResponseDto> createVpc(List<CoreCreateVpcRequestDto> coreCreateVpcRequestDtoList) {
        List<CoreCreateVpcResponseDto> resultList = new ArrayList<>();
        for (CoreCreateVpcRequestDto requestDto : coreCreateVpcRequestDtoList) {
            logger.info("Sending create VPC request: {} to AliCloud.", requestDto.toString());
            // check region id
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            if (StringUtils.isEmpty(regionId)) {
                throw new PluginException("The region id cannot be NULL or empty.");
            }
            requestDto.setRegionId(regionId);

            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);
            final String vpcId = requestDto.getVpcId();
            CoreCreateVpcResponseDto result = new CoreCreateVpcResponseDto();

            if (StringUtils.isNotEmpty(vpcId)) {
                // if vpc id is not empty, retrieve vpc info
                final DescribeVpcsResponse retrieveVpcResponse = this.retrieveVpc(client, regionId, vpcId);
                if (retrieveVpcResponse.getVpcs().size() == 1) {
                    final DescribeVpcsResponse.Vpc foundVpc = retrieveVpcResponse.getVpcs().get(0);
                    result = CoreCreateVpcResponseDto.fromSdk(foundVpc);
                }
            } else {
                // if vpc id is empty, create vpc
                final CreateVpcRequest createVpcRequest = CoreCreateVpcRequestDto.toSdk(requestDto);
                CreateVpcResponse response;
                try {
                    response = this.acsClientStub.request(client, createVpcRequest);
                } catch (AliCloudException ex) {
                    throw new PluginException(ex.getMessage());
                }
                result = CoreCreateVpcResponseDto.fromSdk(response);
            }
            resultList.add(result);

        }
        return resultList;
    }

    @Override
    public DescribeVpcsResponse retrieveVpc(IAcsClient client, String regionId, String vpcId) throws PluginException {

        logger.info("Retrieving VPC info, the vpc ID: [{}].", vpcId);

        // create new request
        DescribeVpcsRequest describeVpcsRequest = new DescribeVpcsRequest();
        describeVpcsRequest.setRegionId(regionId);
        describeVpcsRequest.setVpcId(vpcId);
        // send the request and handle the error, then return the response
        return this.acsClientStub.request(client, describeVpcsRequest);
    }

    @Override
    public void deleteVpc(List<CoreDeleteVpcRequestDto> coreDeleteVpcRequestDtoList) throws PluginException {

        for (CoreDeleteVpcRequestDto requestDto : coreDeleteVpcRequestDtoList) {
            final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(requestDto.getIdentityParams());
            final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(requestDto.getCloudParams());
            final String regionId = cloudParamDto.getRegionId();
            final IAcsClient client = this.acsClientStub.generateAcsClient(identityParamDto, cloudParamDto);


            if (StringUtils.isEmpty(requestDto.getVpcId())) {
                throw new PluginException("The VPC id cannot be empty or null.");
            }

            final DescribeVpcsResponse foundedVpcInfo = this.retrieveVpc(client, regionId, requestDto.getVpcId());

            // check if VPC already deleted
            if (0 == foundedVpcInfo.getTotalCount()) {
                continue;
            }


            // delete VPC
            logger.info("Deleting VPC, VPC ID: [{}], VPC region:[{}]", requestDto.getVpcId(), regionId);
            this.acsClientStub.request(client, requestDto);

            // re-check if VPC has already been deleted
            if (0 != this.retrieveVpc(client, regionId, requestDto.getVpcId()).getTotalCount()) {
                String msg = String.format("The VPC: [%s] from region: [%s] hasn't been deleted", requestDto.getVpcId(), regionId);
                logger.error(msg);
                throw new PluginException(msg);
            }
        }
    }
}
