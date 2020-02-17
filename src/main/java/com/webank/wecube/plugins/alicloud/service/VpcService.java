package com.webank.wecube.plugins.alicloud.service;

import com.aliyuncs.vpc.model.v20160428.DeleteVpcRequest;
import com.aliyuncs.vpc.model.v20160428.DescribeVpcsResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.CoreCreateVpcResponseDto;

import java.util.List;

/**
 * @author howechen
 */
public interface VpcService {

    /**
     * Create VPC
     *
     * @param coreCreateVpcRequestDtoList list of CoreCreateVpcRequestDto
     * @return list of CoreCreateVpcResponseDto
     * @throws PluginException for both server and local client exception
     */
    List<CoreCreateVpcResponseDto> createVpc(List<CoreCreateVpcRequestDto> coreCreateVpcRequestDtoList) throws PluginException;

    /**
     * Retrieve VPC by ID
     *
     * @param vpcId vpc ID
     * @return Found VPC info
     * @throws PluginException for both server and local client exception
     */
    DescribeVpcsResponse retrieveVpc(String regionId, String vpcId) throws PluginException;

    /**
     * Delete VPC
     *
     * @param deleteVpcRequestList list of DeleteVpcRequest
     * @throws PluginException for both server and local client exception
     */
    void deleteVpc(List<DeleteVpcRequest> deleteVpcRequestList) throws PluginException;


}