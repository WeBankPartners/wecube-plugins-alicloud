package com.webank.wecube.plugins.alicloud.dto;

import com.aliyuncs.AcsRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author howechen
 */
public interface PluginSdkInputBridge<T> {
    default <K extends AcsRequest<?>> K toSdk(T requestDto, Class<K> clazz) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, clazz);
    }


}
