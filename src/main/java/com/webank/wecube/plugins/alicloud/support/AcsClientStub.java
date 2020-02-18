package com.webank.wecube.plugins.alicloud.support;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.webank.wecube.plugins.alicloud.common.AliCloudProperties;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcsClientStub {
    private final static Logger logger = LoggerFactory.getLogger(AcsClientStub.class);
    private AliCloudProperties aliCloudProperties;

    @Autowired
    public AcsClientStub(AliCloudProperties aliCloudProperties) {
        this.aliCloudProperties = aliCloudProperties;
    }

    public IAcsClient generateAcsClient(String regionId) {
        DefaultProfile defaultProfile = DefaultProfile.getProfile(
                regionId,
                aliCloudProperties.getAccessId(),
                aliCloudProperties.getAccessSecret()
        );
        return new DefaultAcsClient(defaultProfile);
    }

    public <T extends AcsResponse> T request(IAcsClient client, AcsRequest<T> request) throws PluginException {
        T response;
        try {
            response = client.getAcsResponse(request);
        } catch (ServerException serverEx) {
            logger.error("AliCloud server error! Code: [{}], Msg: [{}]", serverEx.getErrCode(), serverEx.getMessage());
            throw new PluginException("AliCloud server error: " + serverEx.getMessage());
        } catch (ClientException clientEx) {
            logger.error("Plugin local client error! Code: [{}]. Msg: [{}]", clientEx.getErrCode(), clientEx.getMessage());
            throw new PluginException("AliCloud local client error: " + clientEx.getMessage());
        }
        return response;
    }
}
