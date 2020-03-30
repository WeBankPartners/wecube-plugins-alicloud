package com.webank.wecube.plugins.alicloud.service.ecs.vm;

/**
 * @author howechen
 */

public enum InstanceStatus {
    // Pending after create operation
    PENDING("Pending"),
    // Starting: the instance type when submitting the request to start the instance
    STARTING("Starting"),
    // Running: after the instance starts
    RUNNING("Running"),
    // Stopping: the instance type when submitting the request to stop the instance
    STOPPING("Stopping"),
    // Stopped: the instance has been stopped
    STOPPED("Stopped");

    private String instanceStatus;

    InstanceStatus(String status) {
        this.instanceStatus = status;
    }

    protected String getStatus() {
        return instanceStatus;
    }
}
