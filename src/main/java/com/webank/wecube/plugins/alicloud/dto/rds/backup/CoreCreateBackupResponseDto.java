package com.webank.wecube.plugins.alicloud.dto.rds.backup;

import com.aliyuncs.rds.model.v20140815.CreateBackupResponse;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author howechen
 */
public class CoreCreateBackupResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateBackupResponseDto, CreateBackupResponse> {
    private String requestId;
    private String backupJobId;
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBackupJobId() {
        return backupJobId;
    }

    public void setBackupJobId(String backupJobId) {
        this.backupJobId = backupJobId;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .append("backupJobId", backupJobId)
                .append("backupProgressStatus", backupProgressStatus)
                .append("backupStatus", backupStatus)
                .append("jobMode", jobMode)
                .append("process", process)
                .append("taskAction", taskAction)
                .append("backupId", backupId)
                .toString();
    }
}
