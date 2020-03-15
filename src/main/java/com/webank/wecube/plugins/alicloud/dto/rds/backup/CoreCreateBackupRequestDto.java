package com.webank.wecube.plugins.alicloud.dto.rds.backup;

import com.aliyuncs.rds.model.v20140815.CreateBackupRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author howechen
 */
public class CoreCreateBackupRequestDto extends CreateBackupRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;
    private String backupId;

    @JsonProperty(value = "dBInstanceId")
    @Override
    public void setDBInstanceId(String dBInstanceId) {
        super.setDBInstanceId(dBInstanceId);
    }

    @JsonProperty(value = "dBName")
    @Override
    public void setDBName(String dBName) {
        super.setDBName(dBName);
    }

    public CoreCreateBackupRequestDto() {
    }

    public String getIdentityParams() {
        return identityParams;
    }

    public void setIdentityParams(String identityParams) {
        this.identityParams = identityParams;
    }

    public String getCloudParams() {
        return cloudParams;
    }

    public void setCloudParams(String cloudParams) {
        this.cloudParams = cloudParams;
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

    public String getBackupId() {
        return backupId;
    }

    public void setBackupId(String backupId) {
        this.backupId = backupId;
    }
}
