package com.webank.wecube.plugins.alicloud.dto.cbn.cen;

import com.aliyuncs.cbn.model.v20170912.DeleteCenRequest;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreDeleteCenRequestDto extends DeleteCenRequest {
    @NotEmpty(message = "identityParams cannot be null or empty")
    private String identityParams;
    @NotEmpty(message = "cloudParams cannot be null or empty")
    private String cloudParams;
    private String guid = StringUtils.EMPTY;
    private String callbackParameter = StringUtils.EMPTY;

    @JsonDeserialize(as = Long.class)
    @Override
    public void setResourceOwnerId(Long resourceOwnerId) {
        super.setResourceOwnerId(resourceOwnerId);
    }

    @JsonDeserialize(as = Long.class)
    @Override
    public void setOwnerId(Long ownerId) {
        super.setOwnerId(ownerId);
    }

    public CoreDeleteCenRequestDto() {
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
