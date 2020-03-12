package com.webank.wecube.plugins.alicloud.dto.rds.backup;

import com.aliyuncs.rds.model.v20140815.CreateBackupResponse;

/**
 * @author howechen
 */
public class CoreCreateBackupResponseDto extends CreateBackupResponse {
    private String guid;
    private String callbackParameter;

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
}
