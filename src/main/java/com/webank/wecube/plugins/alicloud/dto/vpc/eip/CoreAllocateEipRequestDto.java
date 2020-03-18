package com.webank.wecube.plugins.alicloud.dto.vpc.eip;

import com.aliyuncs.vpc.model.v20160428.AllocateEipAddressRequest;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreAllocateEipRequestDto extends AllocateEipAddressRequest {
    @NotEmpty(message = "identityParams cannot be null or empty")
    private String identityParams;
    @NotEmpty(message = "cloudParams cannot be null or empty")
    private String cloudParams;
    private String guid = StringUtils.EMPTY;
    private String callbackParameter = StringUtils.EMPTY;

    private String allocationId;

    @JsonDeserialize(as = Boolean.class)
    @Override
    public void setAutoPay(Boolean autoPay) {
        super.setAutoPay(autoPay);
    }

    @JsonDeserialize(as = Boolean.class)
    @Override
    public void setIgnoreSSLCerts(boolean ignoreSSLCerts) {
        super.setIgnoreSSLCerts(ignoreSSLCerts);
    }

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

    @JsonDeserialize(as = Long.class)
    @Override
    public void setActivityId(Long activityId) {
        super.setActivityId(activityId);
    }

    public CoreAllocateEipRequestDto() {
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

    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String allocationId) {
        this.allocationId = allocationId;
    }
}
