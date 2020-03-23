package com.webank.wecube.plugins.alicloud.dto.disk;

import com.aliyuncs.ecs.model.v20140526.CreateDiskRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author howechen
 */
public class CoreCreateDiskRequestDto extends CreateDiskRequest {
    private String identityParams;
    private String cloudParams;
    private String guid;
    private String callbackParameter;
    private String diskId;

    @JsonDeserialize(as = Integer.class)
    @Override
    public void setStorageSetPartitionNumber(Integer storageSetPartitionNumber) {
        super.setStorageSetPartitionNumber(storageSetPartitionNumber);
    }

    @JsonDeserialize(as = Integer.class)
    @Override
    public void setSize(Integer size) {
        super.setSize(size);
    }

    @JsonDeserialize(as = Boolean.class)
    @Override
    public void setEncrypted(Boolean encrypted) {
        super.setEncrypted(encrypted);
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

    public CoreCreateDiskRequestDto() {
    }

    public static CreateDiskRequest toSdk(CoreCreateDiskRequestDto requestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(requestDto, CreateDiskRequest.class);
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

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }
}
