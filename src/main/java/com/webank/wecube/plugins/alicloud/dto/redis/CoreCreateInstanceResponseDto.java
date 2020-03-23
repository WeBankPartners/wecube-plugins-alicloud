package com.webank.wecube.plugins.alicloud.dto.redis;

import com.aliyuncs.r_kvstore.model.v20150101.CreateInstanceResponse;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @author howechen
 */
public class CoreCreateInstanceResponseDto extends CreateInstanceResponse {
    private String guid;
    private String callbackParameter;

    @JsonSerialize(using = ToStringSerializer.class)
    @Override
    public void setPort(Integer port) {
        super.setPort(port);
    }

    @JsonSerialize(using = ToStringSerializer.class)
    @Override
    public void setCapacity(Long capacity) {
        super.setCapacity(capacity);
    }

    @JsonSerialize(using = ToStringSerializer.class)
    @Override
    public void setQPS(Long qPS) {
        super.setQPS(qPS);
    }

    @JsonSerialize(using = ToStringSerializer.class)
    @Override
    public void setBandwidth(Long bandwidth) {
        super.setBandwidth(bandwidth);
    }

    @JsonSerialize(using = ToStringSerializer.class)
    @Override
    public void setConnections(Long connections) {
        super.setConnections(connections);
    }

    public CoreCreateInstanceResponseDto() {
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
