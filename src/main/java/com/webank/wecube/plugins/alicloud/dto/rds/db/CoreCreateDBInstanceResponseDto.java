package com.webank.wecube.plugins.alicloud.dto.rds.db;

import com.aliyuncs.rds.model.v20140815.CreateDBInstanceResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;

import java.lang.reflect.ParameterizedType;

/**
 * @author howechen
 */
public class CoreCreateDBInstanceResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreCreateDBInstanceResponseDto, CreateDBInstanceResponse> {

    private String requestId;
    private String dBInstanceId;
    private String orderId;
    private String connectionString;
    private String port;

    // RDS account info
    private String accountName;
    @JsonProperty(value = "accountPassword")
    private String accountEncryptedPassword;

    public CoreCreateDBInstanceResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDBInstanceId() {
        return dBInstanceId;
    }

    public void setDBInstanceId(String dBInstanceId) {
        this.dBInstanceId = dBInstanceId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountEncryptedPassword() {
        return accountEncryptedPassword;
    }

    public void setAccountEncryptedPassword(String accountEncryptedPassword) {
        this.accountEncryptedPassword = accountEncryptedPassword;
    }

    public CoreCreateDBInstanceResponseDto fromSdk(CreateDBInstanceResponse response, String accountName, String accountEncryptedPassword) {
        final CoreCreateDBInstanceResponseDto result = this.fromSdk(response);
        result.setAccountName(accountName);
        result.setAccountEncryptedPassword(accountEncryptedPassword);
        return result;
    }

}
