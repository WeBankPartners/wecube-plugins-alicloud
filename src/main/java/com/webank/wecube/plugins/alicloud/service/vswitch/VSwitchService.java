package com.webank.wecube.plugins.alicloud.service.vswitch;

import com.aliyuncs.vpc.model.v20160428.DeleteVSwitchRequest;
import com.aliyuncs.vpc.model.v20160428.DescribeVSwitchesResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vswitch.CoreCreateVSwitchResponseDto;

import java.util.List;

/**
 * @author howechen
 */
public interface VSwitchService {

    List<CoreCreateVSwitchResponseDto> createVSwitch(List<CoreCreateVSwitchRequestDto> coreCreateVSwitchRequestDtoList) throws PluginException;

    DescribeVSwitchesResponse retrieveVSwtich(String regionId, String vSwitchId) throws PluginException;

    void deleteVSwitch(List<DeleteVSwitchRequest> deleteVSwitchRequestList) throws PluginException;
}
