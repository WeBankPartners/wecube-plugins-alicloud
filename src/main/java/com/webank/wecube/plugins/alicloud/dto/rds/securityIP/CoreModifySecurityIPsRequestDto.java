package com.webank.wecube.plugins.alicloud.dto.rds.securityIP;

import com.aliyuncs.rds.model.v20140815.CreateDBInstanceRequest;
import com.aliyuncs.rds.model.v20140815.ModifySecurityIpsRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;

/**
 * @author howechen
 */
public class CoreModifySecurityIPsRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<ModifySecurityIpsRequest> {
    private String dBInstanceIPArrayName;
    private String resourceOwnerId;
    @NotEmpty(message = "securityIps field is mandatory.")
    private String securityIps;
    private String whitelistNetworkType;
    private String securityIPType = "IPv4";
    @NotEmpty(message = "dBInstanceId field is mandatory.")
    @JsonProperty(value = "dBInstanceId")
    private String dBInstanceId;
    @NotEmpty(message = "modifyMode field is mandatory.")
    private String modifyMode;
    @JsonProperty(value = "dBInstanceIPArrayAttribute")
    private String dBInstanceIPArrayAttribute;

    public CoreModifySecurityIPsRequestDto() {
    }

    public String getdBInstanceIPArrayName() {
        return dBInstanceIPArrayName;
    }

    public void setdBInstanceIPArrayName(String dBInstanceIPArrayName) {
        this.dBInstanceIPArrayName = dBInstanceIPArrayName;
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getSecurityIps() {
        return securityIps;
    }

    public void setSecurityIps(String securityIps) {
        this.securityIps = securityIps;
    }

    public String getWhitelistNetworkType() {
        return whitelistNetworkType;
    }

    public void setWhitelistNetworkType(String whitelistNetworkType) {
        this.whitelistNetworkType = whitelistNetworkType;
    }

    public String getSecurityIPType() {
        return securityIPType;
    }

    public void setSecurityIPType(String securityIPType) {
        this.securityIPType = securityIPType;
    }

    public String getdBInstanceId() {
        return dBInstanceId;
    }

    public void setdBInstanceId(String dBInstanceId) {
        this.dBInstanceId = dBInstanceId;
    }

    public String getModifyMode() {
        return modifyMode;
    }

    public void setModifyMode(String modifyMode) {
        this.modifyMode = modifyMode;
    }

    public String getdBInstanceIPArrayAttribute() {
        return dBInstanceIPArrayAttribute;
    }

    public void setdBInstanceIPArrayAttribute(String dBInstanceIPArrayAttribute) {
        this.dBInstanceIPArrayAttribute = dBInstanceIPArrayAttribute;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("dBInstanceIPArrayName", dBInstanceIPArrayName)
                .append("resourceOwnerId", resourceOwnerId)
                .append("securityIps", securityIps)
                .append("whitelistNetworkType", whitelistNetworkType)
                .append("securityIPType", securityIPType)
                .append("dBInstanceId", dBInstanceId)
                .append("modifyMode", modifyMode)
                .append("dBInstanceIPArrayAttribute", dBInstanceIPArrayAttribute)
                .toString();
    }

    @Override
    public ModifySecurityIpsRequest toSdk() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return mapper.convertValue(this, ModifySecurityIpsRequest.class);
    }
}
