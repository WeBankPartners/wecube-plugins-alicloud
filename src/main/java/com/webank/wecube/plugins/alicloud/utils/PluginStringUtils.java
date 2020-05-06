package com.webank.wecube.plugins.alicloud.utils;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
public class PluginStringUtils {

    private static final String LIST_STR_REGEX = "\\[.*]";
    private static final String CORE_MEMORY_STR_REGEX = "^(\\d*)[C|c](\\d*)[G|g]$";
    private static final String CIDR_FORMAT = "^.+/\\d+$";

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
        String str = rawStringList;
        if (str.matches(LIST_STR_REGEX)) {
            str = StringUtils.removeStart(rawStringList, "[");
            str = StringUtils.removeEnd(str, "]");
        }
        return Arrays.stream(str.split(",")).map(String::trim).collect(Collectors.toList());
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
    public static String removeSquareBracket(String rawStr) {
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

    /**
     * Split resource tag raw string into pair list.
     * Example: a=b;c=d -> [<a,b>, <c,d>]
     * Example: a=b -> [<a,b>]
     *
     * @param rawTagStr resource tag raw string
     * @return result
     * @throws PluginException plugin exception
     */
    public static List<Pair<String, String>> splitResourceTag(String rawTagStr) throws PluginException {

        List<Pair<String, String>> result = new ArrayList<>();

        final String[] kvPairs = rawTagStr.split(";");

        for (String kvPair : kvPairs) {
            final String[] split = kvPair.split("=");
            if (2 != split.length) {
                throw new PluginException("The resource tag format is invalid");
            }

            result.add(new ImmutablePair<>(split[0], split[1]));
        }
        return result;
    }


    /**
     * Handle core cidr list string
     * Example: 192.168.1.1 -> 192.168.1.1/32
     * Example: [192.168.1.1] -> 192.168.1.1/32
     * Example: [192.168.1.1,192.168.2.1/32] -> 192.168.1.1/32,192.168.2.1/32
     * Example: [192.168.1.1/32,192.168.2.1/32] -> 192.168.1.1/32,192.168.2.1/32
     *
     * @param rawStr raw list string
     * @return transferred result
     * @throws PluginException plugin exception
     */
    public static String handleCidrListString(String rawStr) throws PluginException {
        String result;
        if (rawStr.matches(LIST_STR_REGEX)) {
            final List<String> strings = splitStringList(rawStr);
            result = strings.stream().map(s -> {
                if (!s.matches(CIDR_FORMAT)) {
                    s = s.concat("/32");
                }
                return s;
            }).collect(Collectors.joining(","));
        } else {
            if (!rawStr.matches(CIDR_FORMAT)) {
                result = rawStr.concat("/32");
            } else {
                result = rawStr;
            }
        }
        return result;
    }

    /**
     * If the rawString matches the list string pattern
     *
     * @param rawString rawString
     * @return boolean match result
     */
    public static boolean isListStr(String rawString) {
        return rawString.matches(LIST_STR_REGEX);
    }
}
