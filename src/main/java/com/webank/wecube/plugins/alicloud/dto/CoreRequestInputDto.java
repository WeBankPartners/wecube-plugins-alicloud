package com.webank.wecube.plugins.alicloud.dto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreRequestInputDto implements Cloneable {
    @NotEmpty(message = "identityParams cannot be null or empty")
    private String identityParams;
    @NotEmpty(message = "cloudParams cannot be null or empty")
    private String cloudParams;
    @NotEmpty(message = "GUID cannot be null or empty")
    private String guid = StringUtils.EMPTY;
    private String callbackParameter = StringUtils.EMPTY;

    public CoreRequestInputDto() {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("cloudParams", cloudParams)
                .append("guid", guid)
                .append("callbackParameter", callbackParameter)
                .toString();
    }
}
