package com.webank.wecube.plugins.alicloud.dto;

import com.aliyuncs.AcsResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.common.PluginException;

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

        adaptToCore(response);

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        try {
            return mapper.convertValue(response, (Class<T>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]);
        } catch (IllegalArgumentException exception) {
            throw new PluginException(exception.getMessage());
        }
    }

    /**
     * Transfer from SDK response DTO (cross lineage) back to CoreResponseOutputDto
     *
     * @param response SDK response DTO (cross lineage)
     * @param <V>      Cross lineage class
     * @return CoreResponseOutputDto
     */
    default <V> T fromSdkCrossLineage(V response) {

        adaptToCore(response);

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            return mapper.convertValue(response, (Class<T>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]);
        } catch (IllegalArgumentException exception) {
            throw new PluginException(exception.getMessage());
        }
    }

    /**
     * Adapt to core required field
     *
     * @param response AliCloud's response
     * @param <J>      AliCloud response type
     */
    default <J> void adaptToCore(J response) {
    }
}
