package com.webank.wecube.plugins.alicloud.utils;

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
}
