package com.webank.wecube.plugins.alicloud.service;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.RpcAcsRequest;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author howechen
 */
public abstract class AbstractAliCloudService<T extends AcsRequest, K extends RpcAcsRequest> {

    public void regionIdCheck(String regionId) throws PluginException {
        if (StringUtils.isEmpty(regionId)) {
            final String msg = String.format("The region ID cannot be empty, your region id is: [%s]", regionId);
            throw new PluginException(msg);
        }
    }

    /**
     * Request required fields check
     *
     * @param request request object
     * @throws PluginException exception while checking the request fields
     */
    public void fieldCheck(T request) throws PluginException {

    }

    /**
     * Request required fields check
     *
     * @param request request object
     * @throws PluginException exception while checking the request fields
     */
    public void fieldCheck(K request) throws PluginException {

    }
}
