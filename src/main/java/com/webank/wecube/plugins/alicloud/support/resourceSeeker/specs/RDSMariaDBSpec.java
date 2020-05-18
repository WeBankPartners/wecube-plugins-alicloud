package com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
public interface RDSMariaDBSpec {

    Map<String, String> CLASS_TO_SPEC_MAP = new HashMap<String, String>() {{
        put("mariadb.n2.small.2c", "1C2G");
        put("mariadb.n2.medium.2c", "2C4G");
        put("mariadb.x2.large.2c", "4C8G");
        put("mariadb.x4.large.2c", "4C16G");
        put("mariadb.x2.xlarge.2c", "8C16G");
        put("mariadb.x4.xlarge.2c", "8C32G");
        put("mariadb.x2.2xlarge.2c", "16C32G");
        put("mariadb.x4.2xlarge.2c", "16C64G");
        put("mariadb.x8.2xlarge.2c", "16C128G");
        put("mariadb.x4.4xlarge.2c", "32C128G");
        put("mariadb.x8.4xlarge.2c", "32C256G");
        put("mariadb.x4.8xlarge.2c", "56C224G");
        put("mariadb.x8.8xlarge.2c", "56C480G");
    }};

    static List<Map.Entry<String, CoreMemorySpec>> matchResource(List<String> availableResourceList, String coreMemoryString) {
        CoreMemorySpec target = new CoreMemorySpec(coreMemoryString);

        final List<Map.Entry<String, CoreMemorySpec>> matchedResourceList = CLASS_TO_SPEC_MAP.entrySet().stream()
                .filter(entry -> availableResourceList.contains(entry.getKey()))
                .map(entry -> Maps.immutableEntry(entry.getKey(), new CoreMemorySpec(entry.getValue())))
                .collect(Collectors.toList());

        return matchedResourceList.stream()
                .filter(CoreMemorySpec.greaterThan(target))
                .sorted(Map.Entry.comparingByValue(CoreMemorySpec.COMPARATOR))
                .collect(Collectors.toList());
    }


}
