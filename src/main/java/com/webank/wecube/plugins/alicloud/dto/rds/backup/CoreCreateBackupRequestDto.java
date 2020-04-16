package com.webank.wecube.plugins.alicloud.dto.rds.backup;

import com.aliyuncs.rds.model.v20140815.CreateBackupRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestInputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkInputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author howechen
 */
public class CoreCreateBackupRequestDto extends CoreRequestInputDto implements PluginSdkInputBridge<CreateBackupRequest> {
    private String backupId;

    private String resourceOwnerId;
    private String backupStrategy;
    @JsonProperty("dBInstanceId")
    private String dBInstanceId;
    private String backupType;
    private String backupMethod;
    @JsonProperty("dBName")
    private String dBName;

    public CoreCreateBackupRequestDto() {
    }

    public String getBackupId() {
        return backupId;
    }

    public void setBackupId(String backupId) {
        this.backupId = backupId;
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public String getBackupStrategy() {
        return backupStrategy;
    }

    public void setBackupStrategy(String backupStrategy) {
        this.backupStrategy = backupStrategy;
    }

    public String getDBInstanceId() {
        return dBInstanceId;
    }

    public void setDBInstanceId(String dBInstanceId) {
        this.dBInstanceId = dBInstanceId;
    }

    public String getBackupType() {
        return backupType;
    }

    public void setBackupType(String backupType) {
        this.backupType = backupType;
    }

    public String getBackupMethod() {
        return backupMethod;
    }

    public void setBackupMethod(String backupMethod) {
        this.backupMethod = backupMethod;
    }

    public String getDBName() {
        return dBName;
    }

    public void setDBName(String dBName) {
        this.dBName = dBName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("backupId", backupId)
                .append("resourceOwnerId", resourceOwnerId)
                .append("backupStrategy", backupStrategy)
                .append("dBInstanceId", dBInstanceId)
                .append("backupType", backupType)
                .append("backupMethod", backupMethod)
                .append("dBName", dBName)
                .toString();
    }

    @Override
    public CreateBackupRequest toSdk() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return mapper.convertValue(this, CreateBackupRequest.class);
    }
}
