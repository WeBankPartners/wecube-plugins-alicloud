package com.webank.wecube.plugins.alicloud.dto;

import com.webank.wecube.plugins.alicloud.utils.PluginMapUtils;

import java.util.Map;

/**
 * @author howechen
 */
public class CloudParamDto {

    String regionId;

    public CloudParamDto(String regionId) {
        this.regionId = regionId;
    }

    public static CloudParamDto convertFromString(String paramStr) {
        final Map<String, String> map = PluginMapUtils.fromCoreParamString(paramStr);
        return new CloudParamDto(map.get("regionId"));
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
