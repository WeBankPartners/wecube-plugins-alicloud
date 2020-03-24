package com.webank.wecube.plugins.alicloud.dto;

import com.aliyuncs.AcsRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.ParameterizedType;

/**
 * @author howechen
 */
@SuppressWarnings("unchecked")
public interface PluginSdkInputBridge<T extends CoreRequestInputDto, K extends AcsRequest<?>> {

    /**
     * From CoreRequestInputDto to Sdk request DTO
     *
     * @param requestDto CoreRequestInputDto
     * @return transferred SDK request DTO
     */
    default K toSdk(T requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, (Class<K>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[1]);
    }


}
