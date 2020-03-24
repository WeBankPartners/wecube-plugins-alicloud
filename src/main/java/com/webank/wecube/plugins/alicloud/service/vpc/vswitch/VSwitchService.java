package com.webank.wecube.plugins.alicloud.service.vpc.vswitch;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.vpc.model.v20160428.DescribeVSwitchesResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreCreateVSwitchRequestDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreCreateVSwitchResponseDto;
import com.webank.wecube.plugins.alicloud.dto.vpc.vswitch.CoreDeleteVSwitchRequestDto;

import java.util.List;

/**
 * @author howechen
 */
public interface VSwitchService {

    List<CoreCreateVSwitchResponseDto> createVSwitch(List<CoreCreateVSwitchRequestDto> coreCreateVSwitchRequestDtoList) throws PluginException;

    DescribeVSwitchesResponse retrieveVSwtich(IAcsClient client, String regionId, String vSwitchId) throws PluginException;

    void deleteVSwitch(List<CoreDeleteVSwitchRequestDto> deleteVSwitchRequestList) throws PluginException;
}