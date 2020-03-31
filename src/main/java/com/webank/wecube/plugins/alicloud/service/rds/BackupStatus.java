package com.webank.wecube.plugins.alicloud.service.rds;

public enum BackupStatus {

    // not start
    NOT_START("NotStart"),
    // checking
    CHECKING("Checking"),
    // preparing
    PREPARING("Preparing"),
    // waiting
    WAITING("Waiting"),
    // uploading
    UPLOADING("Uploading"),
    // finished
    FINISHED("Finished"),
    // failed
    FAILED("Failed");

    private String status;

    BackupStatus(String status) {
        this.status = status;
    }

    protected String getStatus() {
        return status;
    }
}
