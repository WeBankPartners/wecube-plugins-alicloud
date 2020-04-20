package com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs;

import java.util.HashMap;
import java.util.Map;

/**
 * @author howechen
 */
public interface RDSMariaDBSpecs {

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

    /**
     * Given classStr return the spec of that class
     *
     * @param classStr db instanceClass str
     * @return the spec of that class
     */
    static String matchByInstanceClass(String classStr) {
        return CLASS_TO_SPEC_MAP.get(classStr);
    }

}
