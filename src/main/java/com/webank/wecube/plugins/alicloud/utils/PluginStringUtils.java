package com.webank.wecube.plugins.alicloud.utils;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author howechen
 */
public class PluginStringUtils {

    public static String formatStr(String string) {
        return string.replaceAll("([\\r\\n])", ". ");
    }

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

    public static Pair<String, String> splitCoreAndMemory(String coreAndMemoryString) {

        final String regex = "^(\\d*)[C|c](\\d*)[G|g]$";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(coreAndMemoryString);
        String core = StringUtils.EMPTY;
        String memory = StringUtils.EMPTY;
        // matcher group index: 0 - full group, 1 - core, 2 - memory
        while (matcher.find()) {
            core = matcher.group(1);
            memory = matcher.group(2);
        }

        return new ImmutablePair<>(core, memory);
    }
}
