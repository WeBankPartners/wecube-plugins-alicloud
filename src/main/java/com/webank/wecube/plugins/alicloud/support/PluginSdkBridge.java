package com.webank.wecube.plugins.alicloud.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @author howechen
 */
public interface PluginSdkBridge {

    /**
     * Transfer from core object to sdk object
     *
     * @param requestDto core request dto
     * @param clazz      target object class
     * @param <T>        target SDK object
     * @param <K>        request core object
     * @return transferred result
     */
    static <T, K> T toSdk(K requestDto, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, clazz);
    }

    /**
     * Transfer from core object to sdk object in strict ode
     *
     * @param requestDto core request dto
     * @param clazz      target object class
     * @param <T>        target SDK object
     * @param <K>        request core object
     * @return transferred result
     */
    static <T, K> T toSdkStrict(K requestDto, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(requestDto, clazz);
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
    static <T, K> T fromSdk(K responseDto, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(responseDto, clazz);
    }

    static <T, K> List<T> fromSdkList(List<K> responseDtoList, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(responseDtoList, new TypeReference<List<T>>() {
        });
    }
}
