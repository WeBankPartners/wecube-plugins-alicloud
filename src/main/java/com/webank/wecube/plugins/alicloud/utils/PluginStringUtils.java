package com.webank.wecube.plugins.alicloud.utils;

import java.util.StringJoiner;

public class PluginStringUtils {

    public static String stringifyList(String... strings) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (String string : strings) {
            joiner.add("\"" + string + "\"");
        }
        return joiner.toString();
    }
}
