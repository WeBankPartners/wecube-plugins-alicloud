package com.webank.wecube.plugins.alicloud.dto.rds.securityGroup;

import com.aliyuncs.rds.model.v20140815.ModifySecurityGroupConfigurationResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseOutputDto;
import com.webank.wecube.plugins.alicloud.dto.PluginSdkOutputBridge;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * @author howechen
 */
public class CoreModifyDBSecurityGroupResponseDto extends CoreResponseOutputDto implements PluginSdkOutputBridge<CoreModifyDBSecurityGroupResponseDto, ModifySecurityGroupConfigurationResponse> {

    private String requestId;

    @JsonProperty(value = "dBInstanceName")
    private String dBInstanceName;

    private List<ModifySecurityGroupConfigurationResponse.EcsSecurityGroupRelation> items;

    public CoreModifyDBSecurityGroupResponseDto() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @JsonProperty(value = "dBInstanceName")
    public String getdBInstanceName() {
        return dBInstanceName;
    }

    public void setdBInstanceName(String dBInstanceName) {
        this.dBInstanceName = dBInstanceName;
    }

    public List<ModifySecurityGroupConfigurationResponse.EcsSecurityGroupRelation> getItems() {
        return items;
    }

    public void setItems(List<ModifySecurityGroupConfigurationResponse.EcsSecurityGroupRelation> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .appendSuper(super.toString())
                .append("requestId", requestId)
                .append("dBInstanceName", dBInstanceName)
                .append("items", items)
                .toString();
    }
}
