package com.webank.wecube.plugins.alicloud.dto.ecs.securityGroup;

import com.aliyuncs.ecs.model.v20140526.AuthorizeSecurityGroupRequest;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.ForkableDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import com.webank.wecube.plugins.alicloud.service.ecs.securityGroup.SecurityGroupServiceImpl;
import com.webank.wecube.plugins.alicloud.utils.PluginStringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author howechen
 */
public class CoreAuthorizeSecurityGroupRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<AuthorizeSecurityGroupRequest>, ForkableDto<CoreAuthorizeSecurityGroupRequestDto> {

    public static final String CORE_PORT_RANGE_DELIMITER = "-";
    public static final String ALICLOUD_PORT_RANGE_DELIMITER = "/";

    @NotEmpty(message = "policyType field is mandatory")
    private String policyType;
    @NotEmpty(message = "cidrIp field is mandatory")
    private String cidrIp;

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

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
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

    public String getCidrIp() {
        return cidrIp;
    }

    public void setCidrIp(String cidrIp) {
        this.cidrIp = cidrIp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("actionType", policyType)
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
    public void adaptToAliCloud() throws PluginException {
        if (!StringUtils.isEmpty(this.getPortRange())) {
            if (StringUtils.containsAny(this.getPortRange(), CORE_PORT_RANGE_DELIMITER, ALICLOUD_PORT_RANGE_DELIMITER)) {
                this.setPortRange(this.getPortRange().replace(CORE_PORT_RANGE_DELIMITER, ALICLOUD_PORT_RANGE_DELIMITER));
            } else {
                this.setPortRange(this.getPortRange().concat(ALICLOUD_PORT_RANGE_DELIMITER).concat(this.getPortRange()));
            }
        }

        if (!StringUtils.isEmpty(this.getIpProtocol())) {
            this.setIpProtocol(this.getIpProtocol().toLowerCase());
        }

        if (!StringUtils.isEmpty(this.getPolicy())) {
            this.setPolicy(this.getPolicy().toLowerCase());
        }

        if (!StringUtils.isEmpty(this.getCidrIp())) {

            String transferred = PluginStringUtils.handleCidrListString(this.getCidrIp());

            final SecurityGroupServiceImpl.PolicyType policyType = EnumUtils.getEnumIgnoreCase(SecurityGroupServiceImpl.PolicyType.class, this.getPolicyType());
            if (null == policyType) {
                throw new PluginException("Invalid policyType");
            } else {
                switch (policyType) {
                    case EGRESS:
                        this.setDestCidrIp(transferred);
                        this.setSourceCidrIp(null);
                        break;
                    case INGRESS:
                        this.setSourceCidrIp(transferred);
                        this.setDestCidrIp(null);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public CoreAuthorizeSecurityGroupRequestDto forkThenUpdateFields(Object... fields) throws PluginException {
        final CoreAuthorizeSecurityGroupRequestDto clonedDto;
        try {
            clonedDto = (CoreAuthorizeSecurityGroupRequestDto) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new PluginException(e.getMessage());
        }

        final Iterator<Object> iterator = Arrays.asList(fields).iterator();
        try {
            clonedDto.setCidrIp(String.valueOf(iterator.next()));
            clonedDto.setPortRange(String.valueOf(iterator.next()));
            clonedDto.setIpProtocol(String.valueOf(iterator.next()));
        } catch (NoSuchElementException ex) {
            throw new PluginException("Error when updating new fields");
        }

        return clonedDto;
    }
}
