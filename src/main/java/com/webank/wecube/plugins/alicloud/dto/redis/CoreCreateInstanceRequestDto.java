package com.webank.wecube.plugins.alicloud.dto.redis;

import com.aliyuncs.r_kvstore.model.v20150101.CreateInstanceRequest;

/**
 * @author howechen
 */
public class CoreCreateInstanceRequestDto extends CreateInstanceRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;

    private String instanceId;

    public CoreCreateInstanceRequestDto() {
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

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
