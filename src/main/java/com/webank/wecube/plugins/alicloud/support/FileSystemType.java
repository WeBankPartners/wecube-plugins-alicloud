package com.webank.wecube.plugins.alicloud.support;

/**
 * @author howechen
 */

public enum FileSystemType {
    // ext3
    EXT3("ext3"),
    // ext4
    EXT4("ext4"),
    // xfs
    XFS("xfs");

    private String type;

    FileSystemType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
