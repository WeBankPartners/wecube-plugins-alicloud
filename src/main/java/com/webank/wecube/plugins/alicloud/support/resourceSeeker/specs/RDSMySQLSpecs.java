package com.webank.wecube.plugins.alicloud.support.resourceSeeker.specs;

import java.util.HashMap;
import java.util.Map;

/**
 * @author howechen
 */
public interface RDSMySQLSpecs {

    Map<String, String> CLASS_TO_SPEC_MAP = new HashMap<String, String>() {{
        put("mysql.n1.micro.1", "1C1G");
        put("mysql.n2.small.1", "1C2G");
        put("mysql.n2.medium.1", "2C4G");
        put("mysql.n2.large.1", "4C8G");
        put("mysql.n2.xlarge.1", "8C16G");
        put("mysql.n4.medium.1", "2C8G");
        put("mysql.n4.large.1", "4C16G");
        put("mysql.n4.xlarge.1", "8C32G");
        put("mysql.n2.small.2c", "1C2G");
        put("mysql.n2.medium.2c", "2C4G");
        put("mysql.x2.medium.2c", "2C4G");
        put("mysql.x2.large.2c", "4C8G");
        put("mysql.x2.xlarge.2c", "8C16G");
        put("mysql.x2.3large.2c", "12C24G");
        put("mysql.x2.2xlarge.2c", "16C32G");
        put("mysql.x2.3xlarge.2c", "24C48G");
        put("mysql.x2.4xlarge.2c", "32C64G");
        put("mysql.x2.13large.2c", "52C96G");
        put("mysql.x2.8xlarge.2c", "64C128G");
        put("mysql.x2.13xlarge.2c", "104C192G");
        put("mysql.x4.medium.2c", "2C8G");
        put("mysql.x4.large.2c", "4C16G");
        put("mysql.x4.xlarge.2c", "8C32G");
        put("mysql.x4.3large.2c", "12C48G");
        put("mysql.x4.2xlarge.2c", "16C64G");
        put("mysql.x4.3xlarge.2c", "24C96G");
        put("mysql.x4.4xlarge.2c", "32C128G");
        put("mysql.x4.13large.2c", "52C192G");
        put("mysql.x4.8xlarge.2c", "64C256G");
        put("mysql.x4.13xlarge.2c", "104C384G");
        put("mysql.x8.medium.2c", "2C16G");
        put("mysql.x8.large.2c", "4C32G");
        put("mysql.x8.xlarge.2c", "8C64G");
        put("mysql.x8.3large.2c", "12C96G");
        put("mysql.x8.2xlarge.2c", "16C128G");
        put("mysql.x8.3xlarge.2c", "24C192G");
        put("mysql.x8.4xlarge.2c", "32C256G");
        put("mysql.x8.13large.2c", "52C384G");
        put("mysql.x8.8xlarge.2c", "64C512G");
        put("mysql.x8.13xlarge.2c", "104C768G");
        put("rds.mysql.t1.small", "1C1G");
        put("rds.mysql.s1.small", "1C2G");
        put("rds.mysql.s2.large", "2C4G");
        put("rds.mysql.s2.xlarge", "2C8G");
        put("rds.mysql.s3.large", "4C8G");
        put("rds.mysql.m1.medium", "4C16G");
        put("rds.mysql.c1.large", "8C16G");
        put("rds.mysql.c1.xlarge", "8C32G");
        put("rds.mysql.c2.xlarge", "16C64G");
        put("rds.mysql.c2.xlp2", "16C96G");
        put("rds.mysql.c2.2xlarge", "16C128G");
        put("mysql.x4.large.2", "4C16G");
        put("mysql.x4.xlarge.2", "8C32G");
        put("mysql.x4.2xlarge.2", "16C64G");
        put("mysql.x4.4xlarge.2", "32C128G");
        put("mysql.x8.medium.2", "2C16G");
        put("mysql.x8.large.2", "4C32G");
        put("mysql.x8.xlarge.2", "8C64G");
        put("mysql.x8.2xlarge.2", "16C128G");
        put("mysql.x8.4xlarge.2", "32C256G");
        put("mysql.x8.8xlarge.2", "64C512G");
        put("rds.mysql.st.h43", "60C470G");
        put("rds.mysql.st.v52", "90C720G");
        put("mysql.n2.small.25", "1C2G");
        put("mysql.n2.medium.25", "2C4G");
        put("mysql.n4.medium.25", "2C8G");
        put("mysql.n2.large.25", "4C8G");
        put("mysql.n4.large.25", "4C16G");
        put("mysql.n2.xlarge.25", "8C16G");
        put("mysql.n4.xlarge.25", "8C32G");
        put("mysql.n4.2,xlarge.25", "16C64G");
        put("mysql.n8.2,xlarge.25", "16C128G");
        put("mysql.x4.large.25", "4C16G");
        put("mysql.x4.xlarge.25", "8C32G");
        put("mysql.x4.2xlarge.25", "16C64G");
        put("mysql.x4.4xlarge.25", "32C128G");
        put("mysql.x8.medium.25", "2C16G");
        put("mysql.x8.large.25", "4C32G");
        put("mysql.x8.xlarge.25", "8C64G");
        put("mysql.x8.2xlarge.25", "16C128G");
        put("mysql.x8.4xlarge.25", "32C256G");
        put("mysql.st.8xlarge.25", "60C470G");
        put("mysql.st.12xlarge.25", "90C720G");
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
