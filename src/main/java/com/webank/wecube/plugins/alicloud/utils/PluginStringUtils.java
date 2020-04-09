package com.webank.wecube.plugins.alicloud.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author howechen
 */
public class PluginStringUtils {

    public static String stringifyList(String... strings) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (String string : strings) {
            joiner.add("\"" + string + "\"");
        }
        return joiner.toString();
    }

    public static String stringifyList(List<String> strings) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (String string : strings) {
            joiner.add("\"" + string + "\"");
        }
        return joiner.toString();
    }

    public static String stringifyObjectList(List<String> strings) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (String string : strings) {
            joiner.add(string);
        }
        return joiner.toString();
    }

    public static List<String> splitStringList(String rawStringList) {
        String str;
        str = StringUtils.removeStart(rawStringList, "[");
        str = StringUtils.removeEnd(str, "]");

        return Arrays.asList(str.split(","));
    }
}
