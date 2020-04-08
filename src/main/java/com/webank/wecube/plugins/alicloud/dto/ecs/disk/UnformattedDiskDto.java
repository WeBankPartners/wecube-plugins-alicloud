package com.webank.wecube.plugins.alicloud.dto.ecs.disk;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UnformattedDiskDto {

    @JsonProperty(value = "unformatedDisks")
    private List<String> volumns;

    public UnformattedDiskDto() {
    }

    public List<String> getVolumns() {
        return volumns;
    }

    public void setVolumns(List<String> volumns) {
        this.volumns = volumns;
    }
}
