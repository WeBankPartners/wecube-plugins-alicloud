package com.webank.wecube.plugins.alicloud.dto.rds.securityIP;

import com.aliyuncs.rds.model.v20140815.ModifySecurityIpsRequest;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;

/**
 * @author howechen
 */
public class CoreModifySecurityIPsRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<ModifySecurityIpsRequest> {
    private String dBInstanceIPArrayName;
    private String resourceOwnerId;
    private String securityIps;
    private String whitelistNetworkType;
    private String securityIPType;
    private String dBInstanceId;
    private String modifyMode;
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
}
