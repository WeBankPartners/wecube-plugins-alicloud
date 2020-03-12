package com.webank.wecube.plugins.alicloud.dto.rds.db;

import com.aliyuncs.rds.model.v20140815.CreateDBInstanceResponse;

/**
 * @author howechen
 */
public class CoreCreateDBInstanceResponseDto extends CreateDBInstanceResponse {
    private String guid;
    private String callbackParameter;

    public CoreCreateDBInstanceResponseDto() {
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
