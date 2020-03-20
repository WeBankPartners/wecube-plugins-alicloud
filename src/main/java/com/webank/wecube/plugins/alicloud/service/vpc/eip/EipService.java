package com.webank.wecube.plugins.alicloud.service.vpc.eip;

import com.aliyuncs.IAcsClient;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.vpc.eip.*;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;

import java.util.List;

/**
 * @author howechen
 */
public interface EipService {
    List<CoreAllocateEipResponseDto> allocateEipAddress(List<CoreAllocateEipRequestDto> requestDtoList);


    List<CoreReleaseEipResponseDto> releaseEipAddress(List<CoreReleaseEipRequestDto> requestDtoList);

    void releaseEipAddress(IAcsClient client, String regionId, List<String> eipAllocationId) throws PluginException, AliCloudException;

    void unAssociateEipAddress(IAcsClient client, String regionId, List<String> eipAllocationId, String instanceId, String instanceType) throws PluginException, AliCloudException;

    List<CoreAssociateEipResponseDto> associateEipAddress(List<CoreAssociateEipRequestDto> requestDtoList);

    List<CoreUnAssociateEipResponseDto> unAssociateEipAddress(List<CoreUnAssociateEipRequestDto> requestDtoList);

    boolean ifEipIsAvailable(IAcsClient client, String regionId, String associatedInstanceType, String associatedInstanceId) throws PluginException, AliCloudException;
}
