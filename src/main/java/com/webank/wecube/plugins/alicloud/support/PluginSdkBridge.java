package com.webank.wecube.plugins.alicloud.support;

import com.aliyuncs.AcsRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.common.PluginException;

import java.util.List;

/**
 * @author howechen
 */
public interface PluginSdkBridge {

    /**
     * Transfer from core object to sdk object
     *
     * @param requestDto   core request dto
     * @param clazz        target object class
     * @param <T>          target SDK object
     * @param <K>          request core object
     * @param transLineage if do transLineage transition
     * @return transferred result
     * @throws PluginException plugin exception
     */
    static <T extends AcsRequest<?>, K> T toSdk(K requestDto, Class<T> clazz, boolean transLineage) throws PluginException {
        final T result;
        if (transLineage) {
            ObjectMapper mapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            try {
                result = mapper.convertValue(requestDto, clazz);
            } catch (IllegalArgumentException exception) {
                throw new PluginException(exception.getMessage());
            }

            try {
                result.setSysActionName(clazz.newInstance().getSysActionName());
            } catch (IllegalAccessException | InstantiationException e) {
                throw new PluginException(e.getMessage());
            }
        } else {
            result = toSdk(requestDto, clazz);
        }
        return result;
    }

    /**
     * Transfer from core object to sdk object
     *
     * @param requestDto core request dto
     * @param clazz      target object class
     * @param <T>        target SDK object
     * @param <K>        request core object
     * @return transferred result
     * @throws PluginException plugin exception
     */
    static <T extends AcsRequest<?>, K> T toSdk(K requestDto, Class<T> clazz) throws PluginException {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            return mapper.convertValue(requestDto, clazz);
        } catch (IllegalArgumentException exception) {
            throw new PluginException(exception.getMessage());
        }
    }

    /**
     * Transfer from core object to sdk object in strict ode
     *
     * @param requestDto core request dto
     * @param clazz      target object class
     * @param <T>        target SDK object
     * @param <K>        request core object
     * @return transferred result
     * @throws PluginException plugin exception
     */
    static <T extends AcsRequest<?>, K> T toSdkStrict(K requestDto, Class<T> clazz) throws PluginException {
        ObjectMapper mapper = new ObjectMapper();
        final T result;
        try {
            result = mapper.convertValue(requestDto, clazz);
        } catch (IllegalArgumentException exception) {
            throw new PluginException(exception.getMessage());
        }
        try {
            result.setSysActionName(clazz.newInstance().getSysActionName());
        } catch (IllegalAccessException | InstantiationException e) {
            throw new PluginException(e.getMessage());
        }
        return result;
    }

    /**
     * Transfer from SDK object to core object
     *
     * @param responseDto SDK response  dto
     * @param clazz       target object class
     * @param <T>         target core object
     * @param <K>         SDK response  object
     * @return transferred result
     */
    static <T, K> T fromSdk(K responseDto, Class<T> clazz) throws PluginException {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            return mapper.convertValue(responseDto, clazz);
        } catch (IllegalArgumentException exception) {
            throw new PluginException(exception.getMessage());
        }
    }

    static <T, K> List<T> fromSdkList(List<K> responseDtoList, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            return mapper.convertValue(responseDtoList, new TypeReference<List<T>>() {
            });
        } catch (IllegalArgumentException exception) {
            throw new PluginException(exception.getMessage());
        }
    }
}
