package com.webank.wecube.plugins.alicloud.dto.cen;

import com.aliyuncs.cbn.model.v20170912.CreateCenRequest;

/**
 * @author howechen
 */
public class CoreCreateCenRequestDto extends CreateCenRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;

    private String cenId;

    private String resourceOwnerId;
    private String ownerId;

    public CoreCreateCenRequestDto() {
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

    public String getCenId() {
        return cenId;
    }

    public void setCenId(String cenId) {
        this.cenId = cenId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        super.setResourceOwnerId(Long.valueOf(resourceOwnerId));
    }

    public void setOwnerId(String ownerId) {
        super.setOwnerId(Long.valueOf(ownerId));
    }
}
