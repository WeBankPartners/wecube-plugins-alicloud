package com.webank.wecube.plugins.alicloud.dto.rds.db;

import com.aliyuncs.rds.model.v20140815.DeleteDBInstanceResponse;

/**
 * @author howechen
 */
public class CoreDeleteDBInstanceResponseDto extends DeleteDBInstanceResponse {
    private String guid;
    private String callbackParameter;

    public CoreDeleteDBInstanceResponseDto() {
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
