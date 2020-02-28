package com.webank.wecube.plugins.alicloud.support;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.webank.wecube.plugins.alicloud.common.AliCloudProperties;
import org.apache.commons.lang3.StringUtils;
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
        String locRegionId = regionId;
        logger.info("Generating ACS Client...");
        if (StringUtils.isEmpty(regionId)) {
            String msg = "The regionID not specified from function caller. The profile's regionID would be called.";
            logger.info(msg);
            locRegionId = this.aliCloudProperties.getRegionId();
        }
        DefaultProfile defaultProfile = DefaultProfile.getProfile(
                locRegionId,
                aliCloudProperties.getAccessId(),
                aliCloudProperties.getAccessSecret()
        );
        return new DefaultAcsClient(defaultProfile);
    }

    public <T extends AcsResponse> T request(IAcsClient client, AcsRequest<T> request) throws AliCloudException {
        T response;
        try {
            response = client.getAcsResponse(request);
        } catch (ServerException serverEx) {
            logger.error("AliCloud server error! Code: [{}], Msg: [{}]", serverEx.getErrCode(), serverEx.getMessage());
            throw new AliCloudException("AliCloud server error: " + serverEx.getMessage());
        } catch (ClientException clientEx) {
            logger.error("Plugin local client error! Code: [{}]. Msg: [{}]", clientEx.getErrCode(), clientEx.getMessage());
            throw new AliCloudException("AliCloud local client error: " + clientEx.getMessage());
        }
        return response;
    }
}
