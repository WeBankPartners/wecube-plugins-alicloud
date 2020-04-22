package com.webank.wecube.plugins.alicloud.utils;

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

    private static final String LIST_STR_REGEX = "\\[.*]";
    private static final String CORE_MEMORY_STR_REGEX = "^(\\d*)[C|c](\\d*)[G|g]$";

    /**
     * Remove String's EOF symbol
     *
     * @param string input string
     * @return formatted string
     */
    public static String formatStr(String string) {
        return string.replaceAll("([\\r\\n])", ". ");
    }

    /**
     * Stringify object list to AliCloud's list string
     *
     * @param strings String list
     * @return formatted string
     */
    public static String stringifyList(String... strings) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (String string : strings) {
            joiner.add("\"" + string + "\"");
        }
        return joiner.toString();
    }

    /**
     * Stringify object list to AliCloud's list string with inside quotation mark
     *
     * @param strings String list
     * @return formatted string
     */
    public static String stringifyList(List<String> strings) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (String string : strings) {
            joiner.add("\"" + string + "\"");
        }
        return joiner.toString();
    }

    /**
     * Stringify object list to AliCloud's list string
     *
     * @param strings String list
     * @return formatted string
     */
    public static String stringifyObjectList(List<String> strings) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (String string : strings) {
            joiner.add(string);
        }
        return joiner.toString();
    }

    /**
     * Remove square bracket and split the string into list
     *
     * @param rawStringList raw String list
     * @return formatted string
     */
    public static List<String> splitStringList(String rawStringList) {
        String str;
        str = StringUtils.removeStart(rawStringList, "[");
        str = StringUtils.removeEnd(str, "]");

        return Arrays.asList(str.split(","));
    }

    /**
     * Split coreAndMemory string to a pair
     *
     * @param coreAndMemoryString core and memory string which match the CORE_MEMORY_STR_REGEX
     * @return formatted string
     */
    public static Pair<String, String> splitCoreAndMemory(String coreAndMemoryString) {

        final Pattern pattern = Pattern.compile(CORE_MEMORY_STR_REGEX, Pattern.MULTILINE);
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

    /**
     * Handle core list Str
     *
     * @param rawStr raw string which might have square bracket or not.
     * @return formatted str with square bracket
     */
    public static String handleCoreListStr(String rawStr) {
        if (rawStr.matches(LIST_STR_REGEX)) {
            return rawStr;
        }
        return "[" + rawStr + "]";
    }

    /**
     * Handle core list Str
     *
     * @param rawStr  raw string which might have square bracket or not.
     * @param reverse if reverse is set to true, then it will remove rawStr with square bracket
     * @return formatted str
     */
    public static String handleCoreListStr(String rawStr, boolean reverse) {
        if (reverse) {
            return removeSquareBracket(rawStr);
        } else {
            return handleCoreListStr(rawStr);
        }
    }

    /**
     * Remove square bracket
     *
     * @param rawStr raw string with / without square bracket
     * @return formatted string
     */
    private static String removeSquareBracket(String rawStr) {
        if (rawStr.matches(LIST_STR_REGEX)) {
            String listStr = StringUtils.removeStart(rawStr, "[");
            listStr = StringUtils.removeEnd(listStr, "]");
            return listStr;
        } else {
            return rawStr;
        }

    }

    /**
     * kb to larger unit
     *
     * @param kbSize kb
     * @return transferred larger unit string
     */
    public static String kbToLargerUnit(long kbSize) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        long tb = gb * 1024;

        if (kbSize >= tb) {
            return String.format("%.0fT", (float) kbSize / tb);
        } else if (kbSize >= gb) {
            return String.format("%.0fG", (float) kbSize / gb);
        } else if (kbSize >= mb) {
            float f = (float) kbSize / mb;
            return String.format(f > 100 ? "%.0M" : "%.1f MB", f);
        } else if (kbSize >= kb) {
            float f = (float) kbSize / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", kbSize);
        }
    }
}
