package com.webank.wecube.plugins.alicloud.dto.vswitch;

import com.aliyuncs.vpc.model.v20160428.CreateVSwitchRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author howechen
 */
public class CoreCreateVSwitchRequestDto extends CreateVSwitchRequest {
    private String vSwitchId;
    private String identityParams;
    private String cloudParams;

    @JsonDeserialize(as = Integer.class)
    @Override
    public void setIpv6CidrBlock(Integer ipv6CidrBlock) {
        super.setIpv6CidrBlock(ipv6CidrBlock);
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

    public CoreCreateVSwitchRequestDto(String vSwitchId, String identityParams, String cloudParams) {
        this.vSwitchId = vSwitchId;
        this.identityParams = identityParams;
        this.cloudParams = cloudParams;
    }

    public CoreCreateVSwitchRequestDto() {
    }

    public static CreateVSwitchRequest toSdk(CoreCreateVSwitchRequestDto coreCreateVSwitchRequestDto) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(coreCreateVSwitchRequestDto, CreateVSwitchRequest.class);
    }

    public String getvSwitchId() {
        return vSwitchId;
    }

    public void setvSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
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
}
