package com.webank.wecube.plugins.alicloud.support;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
