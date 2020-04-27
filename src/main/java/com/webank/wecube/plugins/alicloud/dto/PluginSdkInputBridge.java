package com.webank.wecube.plugins.alicloud.dto;

import com.aliyuncs.AcsRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
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
    default K toSdk() throws PluginException {
        adaptToAliCloud();

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            return mapper.convertValue(this, (Class<K>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]);
        } catch (IllegalArgumentException ex) {
            throw new PluginException(ex.getMessage());
        }
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
        adaptToAliCloud();

        final T result;
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            result = mapper.convertValue(this, clazz);
        } catch (IllegalArgumentException ex) {
            throw new PluginException(ex.getMessage());
        }

        try {
            result.setActionName(clazz.newInstance().getActionName());
        } catch (IllegalAccessException | InstantiationException e) {
            throw new PluginException(e.getMessage());
        }
        return result;
    }

    /**
     * adapt self to Alicloud required field
     */
    default void adaptToAliCloud() {
    }


}
