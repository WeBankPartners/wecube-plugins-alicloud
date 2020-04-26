package com.webank.wecube.plugins.alicloud.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.common.PluginException;

/**
 * @author howechen
 */
public class PluginObjectUtils {

    public static <T, V> T mapToObject(V requestDto, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, clazz);
    }

    public static <T, V> T mapJsonStringToObject(String jsonString, Class<T> clazz) throws PluginException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new PluginException("Error while transforming JSON string to class");
        }
    }

    public static <T> String mapObjectToJsonStr(T obj) throws PluginException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new PluginException("Cannot map object to JSON string.");
        }
    }
}
