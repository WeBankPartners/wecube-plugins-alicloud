package com.webank.wecube.plugins.alicloud.dto;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.utils.PluginMapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author howechen
 */
public class CloudParamDto {

    String regionId;

    public CloudParamDto(String regionId) {
        this.regionId = regionId;
    }

    public static CloudParamDto convertFromString(String paramStr) throws PluginException {
        final Map<String, String> map = PluginMapUtils.fromCoreParamString(paramStr);
        final String regionId = map.get("regionId");
        if (StringUtils.isEmpty(regionId)) {
            throw new PluginException("Cannot get field: [regionId] through the given cloudParams string");
        }
        return new CloudParamDto(regionId);
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
