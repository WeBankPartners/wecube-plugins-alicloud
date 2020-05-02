package com.webank.wecube.plugins.alicloud.utils;

import com.webank.wecube.plugins.alicloud.common.PluginException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
public class PluginMapUtils {

    public static Map<String, String> fromCoreParamString(String paramString) throws PluginException {
        try {
            return Arrays.stream(paramString.split(";"))
                    .map(entry -> entry.split("="))
                    .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
        } catch (Exception ex) {
            throw new PluginException("Cannot extract param from param string.");
        }

    }

    public static <K, V> Map<K, V> zipToMap(List<K> keyList, List<V> valueList) throws PluginException {
        Map<K, V> result = new HashMap<>();
        Iterator<K> keyIterator = keyList.iterator();
        Iterator<V> valueIterator = valueList.iterator();
        while (keyIterator.hasNext() && valueIterator.hasNext()) {
            result.put(keyIterator.next(), valueIterator.next());
        }
        if (keyIterator.hasNext() || valueIterator.hasNext()) {
            throw new PluginException("The keyList and valueList size doesn't match.");
        }

        return result;
    }
}
