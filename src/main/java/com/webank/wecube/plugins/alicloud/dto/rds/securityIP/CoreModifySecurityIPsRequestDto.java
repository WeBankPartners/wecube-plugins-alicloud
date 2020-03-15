package com.webank.wecube.plugins.alicloud.dto.rds.securityIP;

import com.aliyuncs.rds.model.v20140815.ModifySecurityIpsRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author howechen
 */
public class CoreModifySecurityIPsRequestDto extends ModifySecurityIpsRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;

    @JsonProperty(value = "dBInstanceId")
    @Override
    public void setDBInstanceId(String dBInstanceId) {
        super.setDBInstanceId(dBInstanceId);
    }

    @JsonProperty(value = "dBInstanceIPArrayName")
    @Override
    public void setDBInstanceIPArrayName(String dBInstanceIPArrayName) {
        super.setDBInstanceIPArrayName(dBInstanceIPArrayName);
    }

    @JsonProperty(value = "dBInstanceIPArrayAttribute")
    @Override
    public void setDBInstanceIPArrayAttribute(String dBInstanceIPArrayAttribute) {
        super.setDBInstanceIPArrayAttribute(dBInstanceIPArrayAttribute);
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
}
