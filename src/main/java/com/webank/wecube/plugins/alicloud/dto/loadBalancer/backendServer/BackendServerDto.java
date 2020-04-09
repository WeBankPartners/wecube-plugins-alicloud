package com.webank.wecube.plugins.alicloud.dto.loadBalancer.backendServer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.webank.wecube.plugins.alicloud.common.PluginException;

import java.util.List;

/**
 * @author howechen
 */
public class BackendServerDto {
    @JsonProperty(value = "ServerId")
    private String serverId;
    @JsonProperty(value = "Weight")
    private String weight = "100";
    @JsonProperty(value = "Type")
    private String type = "ecs";
    @JsonProperty(value = "Port")
    private String port;
    @JsonProperty(value = "ServerIp")
    private String serverIp;
    @JsonProperty(value = "Description")
    private String description;

    public BackendServerDto() {
    }

    public BackendServerDto(String serverId, String port) {
        this.serverId = serverId;
        this.port = port;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() throws PluginException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String result;
        try {
            result = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new PluginException("Cannot transfer backend server info to json string");
        }

        return result;
    }
}
