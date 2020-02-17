package com.webank.wecube.plugins.alicloud.service;

import com.aliyuncs.vpc.model.v20160428.CreateVpcRequest;
import com.aliyuncs.vpc.model.v20160428.CreateVpcResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;

import java.util.List;

/**
 * @author howechen
 */
public interface VpcService {

    /**
     * Create VPC
     *
     * @param createVpcRequestList list of CreateVpcRequest
     * @return list of
     * @throws PluginException plugin exception
     */
    List<CreateVpcResponse> createVpc(List<CreateVpcRequest> createVpcRequestList) throws PluginException;
}
