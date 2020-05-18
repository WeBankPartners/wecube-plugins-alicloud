package com.webank.wecube.plugins.alicloud.support;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author howechen
 */
public interface ZoneIdHelper {
    String WECUBE_HIGH_AVAILABLE_ZONE_PATTERN = "^(.*)(MAZ|maz)(\\d+)-(.+)$";
    String ALICLOUD_HIGH_AVAILABLE_ZONE_PATTERN = "^(?!\\[)+(.*)(MAZ)(\\d+)(.+)(?!])+$";

    /**
     * Remove MAZ field from given zoneId
     * example:
     * ap-southeast-MAZ2-b -> ap-southeast-2b
     *
     * @param s rawZoneId string
     * @return result string
     * @throws PluginException plugin exception
     */
    static String removeMAZField(String s) throws PluginException {
        final Pattern pattern = Pattern.compile(WECUBE_HIGH_AVAILABLE_ZONE_PATTERN, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(s);
        String result = StringUtils.EMPTY;

        /*
         * ap-southeast-1MAZ2-a
         * group 0: full word
         * group 1: ap-southeast-1
         * group 2: MAZ
         * group 3: 2
         * group 4: a
         */
        while (matcher.find()) {
            try {
                result = result.concat(matcher.group(1)).concat(matcher.group(4));
            } catch (IndexOutOfBoundsException ex) {
                throw new PluginException(ex.getMessage());
            }
        }
        return result;
    }

    /**
     * Concat dhigh available zone ID to alicloud's requirement
     * example:
     * [ap-southeast-1MAZ2-a, ap-southeast-1MAZ2-b] -> ap-southeast-1MAZ2(a,b)
     *
     * @param rawZoneStringList raw zone string list
     * @return concat result
     * @throws PluginException plugin exception
     */
    static String concatHighAvailableZoneId(List<String> rawZoneStringList) throws PluginException {
        final Pattern pattern = Pattern.compile(WECUBE_HIGH_AVAILABLE_ZONE_PATTERN, Pattern.MULTILINE);
        Set<String> prefixSet = new HashSet<>();
        Set<String> indexSet = new HashSet<>();
        Set<String> postFixSet = new HashSet<>();
        // find prefix, store postfix
        for (String rawStr : rawZoneStringList) {
            /*
             * ap-southeast-1MAZ2-a
             * group 0: full word
             * group 1: ap-southeast-1
             * group 2: MAZ
             * group 3: 2
             * group 4: a
             */
            final Matcher matcher = pattern.matcher(rawStr);
            while (matcher.find()) {
                try {
                    prefixSet.add(matcher.group(1).concat(matcher.group(2).toUpperCase()));
                    indexSet.add(matcher.group(3));
                    postFixSet.add(matcher.group(4));
                } catch (IndexOutOfBoundsException ex) {
                    throw new PluginException(ex.getMessage());
                }
            }
        }

        if (prefixSet.size() != 1) {
            throw new PluginException("Given multiple prefixes while handling zoneId.");
        }

        if (indexSet.size() != 1) {
            throw new PluginException("Given multiple indexes while handling zoneId");
        }
        List<String> postFixList = new ArrayList<>(postFixSet);
        Collections.sort(postFixList);

        // assembling postfix
        StringJoiner joiner = new StringJoiner(",", "(", ")");

        for (String postFix : postFixList) {
            joiner.add(postFix);
        }

        String prefix = prefixSet.iterator().next();
        String index = indexSet.iterator().next();
        return prefix.concat(index).concat(joiner.toString());
    }

    static boolean isValidBasicZoneId(String zoneId) {
        return !StringUtils.containsIgnoreCase(zoneId, "MAZ");
    }

    static boolean isValidMAZZoneId(String rawString) {
        return rawString.matches(ALICLOUD_HIGH_AVAILABLE_ZONE_PATTERN);
    }
}
