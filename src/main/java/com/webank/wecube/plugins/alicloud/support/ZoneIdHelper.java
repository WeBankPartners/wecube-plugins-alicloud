package com.webank.wecube.plugins.alicloud.support;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
public interface ZoneIdHelper {

    Logger logger = LoggerFactory.getLogger(ZoneIdHelper.class);

    String WECUBE_HIGH_AVAILABLE_ZONE_PATTERN = "^(.*)(MAZ|maz)(\\d+)-(.+)$";
    String ALICLOUD_HIGH_AVAILABLE_ZONE_PATTERN = "^(?!\\[)+(.*)(MAZ)(\\d+)(.+)(?!])+$";
    String ALICLOUD_HIGH_AVAILABLE_ZONE_CHILD_ZONE_PATTERN = "^.*\\((.+)\\)$";
    int ZONE_ID_SEPERATED_LENGTH = 3;

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

    static List<String> getChildZoneList(String mazZone) {
        final Pattern pattern = Pattern.compile(ALICLOUD_HIGH_AVAILABLE_ZONE_CHILD_ZONE_PATTERN, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(mazZone);
        List<String> result = new ArrayList<>();

        /*
         * ap-southeast-1MAZ2(a,b)
         * group 0: full word
         * group 1: a,b
         */
        while (matcher.find()) {
            result = Arrays.asList(matcher.group(1).split(","));
        }
        return result;
    }

    static boolean isValidBasicZoneId(String zoneId) {
        return !StringUtils.containsIgnoreCase(zoneId, "MAZ");
    }

    static boolean isValidMAZZoneId(String rawString) {
        return rawString.matches(ALICLOUD_HIGH_AVAILABLE_ZONE_PATTERN);
    }

    static boolean ifZoneIdContainsMAZ(String rawString) {
        return StringUtils.containsIgnoreCase(rawString, "MAZ");
    }

    static List<String> getZonePostfixList(String rawString) {

        return PluginStringUtils.splitStringList(rawString)
                .stream()
                .map(s -> s.substring(s.length() - 1))
                .collect(Collectors.toList());
    }

    static Set<String> getZonePrefixList(String rawString) {

        return PluginStringUtils.splitStringList(rawString)
                .stream()
                .map(s -> {
                    final String[] split = s.split("-");
                    if (split.length != ZONE_ID_SEPERATED_LENGTH) {
                        throw new PluginException(String.format("Invalid zoneId: [%s]", s));
                    }
                    return split[0].concat("-").concat(split[1]);
                })
                .collect(Collectors.toSet());
    }

    static void ifZoneInAvailableZoneId(String zoneId, String availableZoneId) {
        final List<String> availableChildZoneList = ZoneIdHelper.getChildZoneList(availableZoneId);

        final Set<String> zonePrefixSet = ZoneIdHelper.getZonePrefixList(zoneId);
        final List<String> zonePostfixList = ZoneIdHelper.getZonePostfixList(zoneId);

        logger.info("Found availableZoneId: [{}], give zoneId prefix list is: [{}] and postFix list is: [{}]", availableZoneId, zonePrefixSet, zonePostfixList);

        if (zonePrefixSet.size() != 1) {
            throw new PluginException(String.format("Given redundant zoneId: [%s]", zonePrefixSet));
        }

        final String prefix = zonePrefixSet.iterator().next();
        if (!StringUtils.containsIgnoreCase(availableZoneId, prefix)) {
            throw new PluginException(String.format("The available zoneId: [%s] doesn't contains given zoneId prefix: [%s]", availableZoneId, prefix));
        }

        for (String postFix : zonePostfixList) {
            if (!availableChildZoneList.contains(postFix)) {
                throw new PluginException(String.format("The available zoneId: [%s] doesn't contain the postfix: [%s] in given zoneId: [%s]", availableZoneId, postFix, zoneId));
            }
        }
    }
}
