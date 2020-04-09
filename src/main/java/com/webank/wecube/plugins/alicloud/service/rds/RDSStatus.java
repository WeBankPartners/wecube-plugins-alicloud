package com.webank.wecube.plugins.alicloud.service.rds;

/**
 * @author howechen
 */
public enum RDSStatus {

    // creating
    CREATING("Creating"),
    // running
    RUNNING("Running"),
    // deleting
    DELETING("Deleting"),
    // rebooting
    REBOOTING("Rebooting"),
    // db instance class changing
    DB_INSTNCE_CLASS_CHANGING("DBInstanceClassChanging"),
    // transing
    TRANSING("TRANSING"),
    // engine version upgrading
    ENGINE_VERSION_UPGRADING("EngineVersionUpgrading"),
    // transing to other RDS
    TRANSING_TO_OTHERS("TransingToOthers"),
    // guard db instance creating
    GUARD_DB_INSTANCE_CREATING("GuardDBInstanceCreating"),
    // restoring
    RESTORING("Restoring"),
    // importing
    IMPORTING("Importing"),
    // importing from other RDS
    IMPORTING_FROM_OTHERS("ImportingFromOthers"),
    // db instance net type changing
    DB_INSTANCE_NET_TYPE_CHANGING("DBInstanceNetTypeChanging"),
    // gurad swtiching
    GUARD_SWITCHING("GuardSwitching"),
    // instance is cloning
    INS_CLONING("INS_CLONING");

    private String status;

    RDSStatus(String status) {
        this.status = status;
    }

    protected String getStatus() {
        return this.status;
    }
}
