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
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import com.webank.wecube.plugins.alicloud.dto.IdentityParamDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author howechen
 */
@Service
public class AcsClientStub {
    private final static Logger logger = LoggerFactory.getLogger(AcsClientStub.class);
    private AliCloudProperties aliCloudProperties;

    @Autowired
    public AcsClientStub(AliCloudProperties aliCloudProperties) {
        this.aliCloudProperties = aliCloudProperties;
    }


    public IAcsClient generateAcsClient(IdentityParamDto identityParamDto, CloudParamDto cloudParamDto) throws PluginException {

        if (StringUtils.isAnyEmpty(identityParamDto.getAccessKeyId(), identityParamDto.getSecret(), cloudParamDto.getRegionId())) {
            throw new PluginException("Either id, secret from [identityParams] or regionId from [cloudParams] cannot be empty or null");
        }

        DefaultProfile defaultProfile = DefaultProfile.getProfile(
                cloudParamDto.getRegionId(),
                identityParamDto.getAccessKeyId(),
                identityParamDto.getSecret()
        );
        return new DefaultAcsClient(defaultProfile);
    }

    public IAcsClient generateAcsClient(String identityParamStr, String cloudParamStr) throws PluginException {

        final IdentityParamDto identityParamDto = IdentityParamDto.convertFromString(cloudParamStr);
        final CloudParamDto cloudParamDto = CloudParamDto.convertFromString(identityParamStr);

        return this.generateAcsClient(identityParamDto, cloudParamDto);
    }

    public <T extends AcsResponse> T request(IAcsClient client, AcsRequest<T> request) throws AliCloudException {
        T response;
        try {
            response = client.getAcsResponse(request);
        } catch (ServerException serverEx) {
            logger.error("AliCloud server error! Error type: [{}], code: [{}], msg: [{}], description: [{}]", serverEx.getErrorType(), serverEx.getErrCode(), serverEx.getMessage(), serverEx.getErrorDescription());
            throw new AliCloudException(String.format("AliCloud server error: [%s]", serverEx.getMessage()));
        } catch (ClientException clientEx) {
            logger.error("AliCloud local client error! Error type: [{}], code: [{}], msg: [{}], description: [{}]", clientEx.getErrorType(), clientEx.getErrCode(), clientEx.getMessage(), clientEx.getErrorDescription());
            throw new AliCloudException(String.format("AliCloud local client error: [%s]", clientEx.getMessage()));
        }
        return response;
    }
}
