package com.webank.wecube.plugins.alicloud.dto;

import com.aliyuncs.AcsRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.common.PluginException;

import java.lang.reflect.ParameterizedType;

/**
 * @author howechen
 */
@SuppressWarnings("unchecked")
public interface PluginSdkInputBridge<K extends AcsRequest<?>> {

    /**
     * From CoreRequestInputDto to Sdk request DTO
     *
     * @return transferred SDK request DTO
     */
    default K toSdk() {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(this, (Class<K>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]);
    }

    /**
     * From CoreRequestInputDto to Sdk request DTO (cross lineage)
     *
     * @param clazz class of T
     * @param <T>   T class
     * @return transferred result
     * @throws PluginException while transferring the DTO
     */
    default <T extends AcsRequest<?>> T toSdkCrossLineage(Class<T> clazz) throws PluginException {
        final T result;
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        result = mapper.convertValue(this, clazz);
        try {
            result.setActionName(clazz.newInstance().getActionName());
        } catch (IllegalAccessException | InstantiationException e) {
            throw new PluginException(e.getMessage());
        }
        return result;
    }


}
