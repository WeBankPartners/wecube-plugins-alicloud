package com.webank.wecube.plugins.alicloud.service.redis;

/**
 * @author howechen
 */

public enum InstanceStatus {
    // normal
    NORMAL("Normal"),
    // creating
    CREATING("Creating"),
    // changing
    CHANGING("Changing"),
    // in-active
    INACTIVE("Inactive"),
    // flushing
    FLUSHING("Flushing"),
    // released
    // delete redis instance will not change the status to released, but found empty list
    RELEASED("Released"),
    // transforming
    TRANSFORMING("Transforming"),
    // un-available
    UNAVAILABLE("Unavailable"),
    // error
    ERROR("Error"),
    // migrating
    MIGRATING("Migrating"),
    // backup recovering
    BACKUP_RECOVERING("BackupRecovering"),
    // minor version upgrading
    MINOR_VERSION_UPGRADING("MinorVersionUpgrading"),
    // network modifying
    NETWORK_MODIFYING("NetworkModifying"),
    // ssl modifying
    SSL_MODIFYING("SSLModifying"),
    // major version upgrading
    MAJOR_VERSION_UPGRADING("MajorVersionUpgrading");

    private String status;

    InstanceStatus(String status) {
        this.status = status;
    }

    protected String getStatus() {
        return status;
    }
}
