package com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup;

import com.aliyuncs.ecs.model.v20140526.AuthorizeSecurityGroupRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreAuthorizeSecurityGroupRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<AuthorizeSecurityGroupRequest> {

    @NotEmpty(message = "isEgress field is mandatory")
    @JsonProperty(value = "isEgress")
    private String isEgress = "false";

    private String nicType;
    private String resourceOwnerId;
    private String sourcePortRange;
    private String clientToken;
    @NotEmpty(message = "securityGroupId field is mandatory")
    private String securityGroupId;
    private String description;
    private String sourceGroupOwnerId;
    private String sourceGroupOwnerAccount;
    private String ipv6SourceCidrIp;
    private String ipv6DestCidrIp;
    private String policy;
    @NotEmpty(message = "portRange field is mandatory")
    private String portRange;
    private String resourceOwnerAccount;
    @NotEmpty(message = "ipProtocol field is mandatory")
    private String ipProtocol;
    private String ownerAccount;
    private String sourceCidrIp;
    private String ownerId;
    private String priority;
    private String destCidrIp;
    private String sourceGroupId;

    public CoreAuthorizeSecurityGroupRequestDto() {
    }

    public String getIsEgress() {
        return isEgress;
    }

    public void setIsEgress(String isEgress) {
        this.isEgress = isEgress;
    }

    public String getNicType() {
        return nicType;
    }

    public void setNicType(String nicType) {
        this.nicType = nicType;
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getSourcePortRange() {
        return sourcePortRange;
    }

    public void setSourcePortRange(String sourcePortRange) {
        this.sourcePortRange = sourcePortRange;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceGroupOwnerId() {
        return sourceGroupOwnerId;
    }

    public void setSourceGroupOwnerId(String sourceGroupOwnerId) {
        this.sourceGroupOwnerId = sourceGroupOwnerId;
    }

    public String getSourceGroupOwnerAccount() {
        return sourceGroupOwnerAccount;
    }

    public void setSourceGroupOwnerAccount(String sourceGroupOwnerAccount) {
        this.sourceGroupOwnerAccount = sourceGroupOwnerAccount;
    }

    public String getIpv6SourceCidrIp() {
        return ipv6SourceCidrIp;
    }

    public void setIpv6SourceCidrIp(String ipv6SourceCidrIp) {
        this.ipv6SourceCidrIp = ipv6SourceCidrIp;
    }

    public String getIpv6DestCidrIp() {
        return ipv6DestCidrIp;
    }

    public void setIpv6DestCidrIp(String ipv6DestCidrIp) {
        this.ipv6DestCidrIp = ipv6DestCidrIp;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getPortRange() {
        return portRange;
    }

    public void setPortRange(String portRange) {
        this.portRange = portRange;
    }

    public String getResourceOwnerAccount() {
        return resourceOwnerAccount;
    }

    public void setResourceOwnerAccount(String resourceOwnerAccount) {
        this.resourceOwnerAccount = resourceOwnerAccount;
    }

    public String getIpProtocol() {
        return ipProtocol;
    }

    public void setIpProtocol(String ipProtocol) {
        this.ipProtocol = ipProtocol;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public String getSourceCidrIp() {
        return sourceCidrIp;
    }

    public void setSourceCidrIp(String sourceCidrIp) {
        this.sourceCidrIp = sourceCidrIp;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDestCidrIp() {
        return destCidrIp;
    }

    public void setDestCidrIp(String destCidrIp) {
        this.destCidrIp = destCidrIp;
    }

    public String getSourceGroupId() {
        return sourceGroupId;
    }

    public void setSourceGroupId(String sourceGroupId) {
        this.sourceGroupId = sourceGroupId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("isEgress", isEgress)
                .append("nicType", nicType)
                .append("resourceOwnerId", resourceOwnerId)
                .append("sourcePortRange", sourcePortRange)
                .append("clientToken", clientToken)
                .append("securityGroupId", securityGroupId)
                .append("description", description)
                .append("sourceGroupOwnerId", sourceGroupOwnerId)
                .append("sourceGroupOwnerAccount", sourceGroupOwnerAccount)
                .append("ipv6SourceCidrIp", ipv6SourceCidrIp)
                .append("ipv6DestCidrIp", ipv6DestCidrIp)
                .append("policy", policy)
                .append("portRange", portRange)
                .append("resourceOwnerAccount", resourceOwnerAccount)
                .append("ipProtocol", ipProtocol)
                .append("ownerAccount", ownerAccount)
                .append("sourceCidrIp", sourceCidrIp)
                .append("ownerId", ownerId)
                .append("priority", priority)
                .append("destCidrIp", destCidrIp)
                .append("sourceGroupId", sourceGroupId)
                .toString();
    }

    @Override
    public AuthorizeSecurityGroupRequest toSdk() {
        if (!StringUtils.isEmpty(this.getIpProtocol())) {
            this.setIpProtocol(this.getIpProtocol().toLowerCase());
        }

        if (!StringUtils.isEmpty(this.getPolicy())) {
            this.setPolicy(this.getPolicy().toLowerCase());
        }

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return mapper.convertValue(this, AuthorizeSecurityGroupRequest.class);
    }
}
