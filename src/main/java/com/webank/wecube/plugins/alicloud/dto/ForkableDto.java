package com.webank.wecube.plugins.alicloud.dto;

import com.webank.wecube.plugins.alicloud.common.PluginException;

/**
 * @author howechen
 */
public interface ForkableDto<T> {

    /**
     * Fork then update fields to that child dto
     *
     * @param fields field array
     * @return forked object
     * @throws PluginException plugin exception
     */
    T forkThenUpdateFields(Object... fields) throws PluginException;
}
