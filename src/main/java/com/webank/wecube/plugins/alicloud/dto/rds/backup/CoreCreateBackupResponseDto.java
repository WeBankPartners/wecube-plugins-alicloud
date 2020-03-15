package com.webank.wecube.plugins.alicloud.dto.rds.backup;

import com.aliyuncs.rds.model.v20140815.CreateBackupResponse;

/**
 * @author howechen
 */
public class CoreCreateBackupResponseDto extends CreateBackupResponse {
    private String guid;
    private String callbackParameter;

    /**
     * found backup from backupJobId
     */
    private String backupProgressStatus;
    private String backupStatus;
    private String jobMode;
    private String process;
    private String taskAction;
    private String backupId;

    public CoreCreateBackupResponseDto() {
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCallbackParameter() {
        return callbackParameter;
    }

    public void setCallbackParameter(String callbackParameter) {
        this.callbackParameter = callbackParameter;
    }

    public String getBackupProgressStatus() {
        return backupProgressStatus;
    }

    public void setBackupProgressStatus(String backupProgressStatus) {
        this.backupProgressStatus = backupProgressStatus;
    }

    public String getBackupStatus() {
        return backupStatus;
    }

    public void setBackupStatus(String backupStatus) {
        this.backupStatus = backupStatus;
    }

    public String getJobMode() {
        return jobMode;
    }

    public void setJobMode(String jobMode) {
        this.jobMode = jobMode;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getTaskAction() {
        return taskAction;
    }

    public void setTaskAction(String taskAction) {
        this.taskAction = taskAction;
    }

    public String getBackupId() {
        return backupId;
    }

    public void setBackupId(String backupId) {
        this.backupId = backupId;
    }
}
