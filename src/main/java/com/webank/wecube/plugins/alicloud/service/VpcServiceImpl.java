package com.webank.wecube.plugins.alicloud.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vpc.model.v20160428.CreateVpcRequest;
import com.aliyuncs.vpc.model.v20160428.CreateVpcResponse;
import com.webank.wecube.plugins.alicloud.common.AliCloudProperties;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VpcServiceImpl implements VpcService {
    @Autowired
    private AliCloudProperties aliCloudProperties;

    @Override
    public List<CreateVpcResponse> createVpc(List<CreateVpcRequest> vpcCreateVpcRequestDto) {
        List<CreateVpcResponse> resultList = new ArrayList<>();
        for (CreateVpcRequest createVpcRequest : vpcCreateVpcRequestDto) {
            DefaultProfile defaultProfile = DefaultProfile.getProfile(
                    createVpcRequest.getRegionId(),
                    aliCloudProperties.getAccessId(),
                    aliCloudProperties.getAccessSecret()
            );
            final IAcsClient client = new DefaultAcsClient(defaultProfile);
            CreateVpcResponse response;
            try {
                response = client.getAcsResponse(createVpcRequest);
            } catch (ServerException serverEx) {
                throw new PluginException("AliCloud server error: " + serverEx.getMessage());
            } catch (ClientException clientEx) {
                throw new PluginException("AliCloud local client error: " + clientEx.getMessage());
            }
            resultList.add(response);
        }
        return resultList;
    }
}
