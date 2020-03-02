package com.webank.wecube.plugins.alicloud.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
public class PluginMapUtils {

    public static Map<String, String> fromCoreParamString(String paramString) {
        return Arrays.stream(paramString.split(";"))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
    }
}
