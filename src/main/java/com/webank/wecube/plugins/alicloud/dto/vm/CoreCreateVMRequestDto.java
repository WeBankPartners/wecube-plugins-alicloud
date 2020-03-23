package com.webank.wecube.plugins.alicloud.dto.vm;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author howechen
 */
public class CoreCreateVMRequestDto extends CreateInstanceRequest {
    private String instanceId;
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;

    @JsonDeserialize(as = Float.class)
    @Override
    public void setSpotPriceLimit(Float spotPriceLimit) {
        super.setSpotPriceLimit(spotPriceLimit);
    }

    @JsonDeserialize(as = Integer.class)
    @Override
    public void setStorageSetPartitionNumber(Integer storageSetPartitionNumber) {
        super.setStorageSetPartitionNumber(storageSetPartitionNumber);
    }

    @JsonDeserialize(as = Integer.class)
    @Override
    public void setAutoRenewPeriod(Integer autoRenewPeriod) {
        super.setAutoRenewPeriod(autoRenewPeriod);
    }

    @JsonDeserialize(as = Integer.class)
    @Override
    public void setPeriod(Integer period) {
        super.setPeriod(period);
    }

    @JsonDeserialize(as = Integer.class)
    @Override
    public void setInternetMaxBandwidthIn(Integer internetMaxBandwidthIn) {
        super.setInternetMaxBandwidthIn(internetMaxBandwidthIn);
    }

    @JsonDeserialize(as = Integer.class)
    @Override
    public void setInternetMaxBandwidthOut(Integer internetMaxBandwidthOut) {
        super.setInternetMaxBandwidthOut(internetMaxBandwidthOut);
    }

    @JsonDeserialize(as = Integer.class)
    @Override
    public void setSpotDuration(Integer spotDuration) {
        super.setSpotDuration(spotDuration);
    }

    @JsonDeserialize(as = Integer.class)
    @Override
    public void setSystemDiskSize(Integer systemDiskSize) {
        super.setSystemDiskSize(systemDiskSize);
    }

    @JsonDeserialize(as = Boolean.class)
    @Override
    public void setDeletionProtection(Boolean deletionProtection) {
        super.setDeletionProtection(deletionProtection);
    }

    @JsonDeserialize(as = Boolean.class)
    @Override
    public void setDryRun(Boolean dryRun) {
        super.setDryRun(dryRun);
    }

    @JsonDeserialize(as = Boolean.class)
    @Override
    public void setAutoRenew(Boolean autoRenew) {
        super.setAutoRenew(autoRenew);
    }

    @JsonDeserialize(as = Boolean.class)
    @Override
    public void setUseAdditionalService(Boolean useAdditionalService) {
        super.setUseAdditionalService(useAdditionalService);
    }

    @JsonDeserialize(as = Boolean.class)
    @Override
    public void setPasswordInherit(Boolean passwordInherit) {
        super.setPasswordInherit(passwordInherit);
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

    public CoreCreateVMRequestDto(String instanceId, String identityParams, String cloudParams) {
        this.instanceId = instanceId;
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
    }

    public CoreCreateVMRequestDto() {
    }

    public static CreateInstanceRequest toSdk(CoreCreateVMRequestDto coreCreateVMRequestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(coreCreateVMRequestDto, CreateInstanceRequest.class);
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
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
