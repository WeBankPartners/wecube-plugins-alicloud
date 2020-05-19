package com.webank.wecube.plugins.alicloud.dto;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.utils.PluginMapUtils;

import java.util.Map;

/**
 * @author howechen
 */
public class IdentityParamDto {
    private String accessKeyId;
    private String secret;

    public IdentityParamDto(String accessKeyId, String secret) {
        this.accessKeyId = accessKeyId;
        this.secret = secret;
    }

    public IdentityParamDto() {
    }

    public static IdentityParamDto convertFromString(String paramStr) throws PluginException {
        final Map<String, String> map;
        try {
            map = PluginMapUtils.fromCoreParamString(paramStr);
        } catch (PluginException ex) {
            throw new PluginException("Error when handling identityParam, please check your request.");
        }

        return new IdentityParamDto(map.get("accessKeyId"), map.get("secret"));
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
