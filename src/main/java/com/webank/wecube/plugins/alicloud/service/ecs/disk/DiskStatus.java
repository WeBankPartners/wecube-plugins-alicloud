package com.webank.wecube.plugins.alicloud.service.ecs.disk;

/**
 * @author howechen
 */

public enum DiskStatus {
    // The disk is in use
    IN_USE("In_use"),
    // disk is available
    AVAILABLE("Available"),
    // disk is attaching to another resource
    ATTACHING("Attaching"),
    // disk is detaching from another resource
    DETACHING("Detaching"),
    // disk is creating
    CREATING("Creating"),
    // disk is re-initing
    REINITING("ReIniting");


    private String status;

    DiskStatus(String status) {
        this.status = status;
    }

    protected String getStatus() {
        return status;
    }
}
