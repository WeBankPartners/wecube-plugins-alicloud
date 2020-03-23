package com.webank.wecube.plugins.alicloud.dto.rds.db;

import com.aliyuncs.rds.model.v20140815.CreateDBInstanceRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author howechen
 */
public class CoreCreateDBInstanceRequestDto extends CreateDBInstanceRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;


    @JsonProperty("dBInstanceId")
    private String dBInstanceId;

    @JsonDeserialize(as = Long.class)
    @Override
    public void setResourceOwnerId(Long resourceOwnerId) {
        super.setResourceOwnerId(resourceOwnerId);
    }

    public CoreCreateDBInstanceRequestDto() {
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

    public String getDBInstanceId() {
        return dBInstanceId;
    }

    public void setDBInstanceId(String dBInstanceId) {
        this.dBInstanceId = dBInstanceId;
    }

    @JsonProperty("dBInstanceClass")
    @Override
    public void setDBInstanceClass(String dBInstanceClass) {
        super.setDBInstanceClass(dBInstanceClass);
    }

    @JsonProperty("dBInstanceStorage")
    @Override
    public void setDBInstanceStorage(Integer dBInstanceStorage) {
        super.setDBInstanceStorage(dBInstanceStorage);
    }

    @JsonProperty("dBInstanceDescription")
    @Override
    public void setDBInstanceDescription(String dBInstanceDescription) {
        super.setDBInstanceDescription(dBInstanceDescription);
    }

    @JsonProperty("dBInstanceStorageType")
    @Override
    public void setDBInstanceStorageType(String dBInstanceStorageType) {
        super.setDBInstanceStorageType(dBInstanceStorageType);
    }

    @JsonProperty("dBInstanceNetType")
    @Override
    public void setDBInstanceNetType(String dBInstanceNetType) {
        super.setDBInstanceNetType(dBInstanceNetType);
    }
}
