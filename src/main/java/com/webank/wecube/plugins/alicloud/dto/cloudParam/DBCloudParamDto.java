package com.webank.wecube.plugins.alicloud.dto.cloudParam;

import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.utils.PluginMapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author howechen
 */
public class DBCloudParamDto extends CloudParamDto {

    private String regionGroup;

    public DBCloudParamDto(String regionId, String regionGroup) {
        super(regionId);
        this.regionGroup = regionGroup;
    }

    public static DBCloudParamDto convertFromString(String paramStr) throws PluginException {
        final Map<String, String> map;
        try {
            map = PluginMapUtils.fromCoreParamString(paramStr);
        } catch (PluginException ex) {
            throw new PluginException("Error when handling cloudParam, please check your request.");
        }

        final String regionId = map.get("regionId");
        if (StringUtils.isEmpty(regionId)) {
            throw new PluginException("Cannot get field: [regionId] through the given cloudParams string");
        }
        final String regionGroup = map.get("regionGroup");
        if (StringUtils.isEmpty(regionId)) {
            throw new PluginException("Cannot get field: [regionGroup] through the given cloudParams string");
        }
        return new DBCloudParamDto(regionId, regionGroup);
    }

    public String getRegionGroup() {
        return regionGroup;
    }

    public void setRegionGroup(String regionGroup) {
        this.regionGroup = regionGroup;
    }

}
