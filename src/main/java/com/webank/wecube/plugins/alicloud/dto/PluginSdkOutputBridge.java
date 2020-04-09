package com.webank.wecube.plugins.alicloud.dto;

import com.aliyuncs.AcsResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.ParameterizedType;

/**
 * @author howechen
 */
@SuppressWarnings("unchecked")
public interface PluginSdkOutputBridge<T extends CoreResponseOutputDto, K extends AcsResponse> {

    /**
     * Transfer from SDK response DTO back to CoreResponseOutputDto
     *
     * @param response SDK response DTO
     * @return transferred CoreResponseOutputDto
     */
    default T fromSdk(K response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, (Class<T>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]);
    }

    /**
     * Transfer from SDK response DTO (cross lineage) back to CoreResponseOutputDto
     *
     * @param response SDK response DTO (cross lineage)
     * @param <V>      Cross lineage class
     * @return CoreResponseOutputDto
     */
    default <V> T fromSdkCrossLineage(V response) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(response, (Class<T>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]);
    }
}
