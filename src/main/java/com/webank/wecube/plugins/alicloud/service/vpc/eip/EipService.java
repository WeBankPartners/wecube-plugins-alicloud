package com.webank.wecube.plugins.alicloud.service.vpc.eip;

import com.webank.wecube.plugins.alicloud.dto.vpc.eip.*;

import java.util.List;

public interface EipService {
    List<CoreAllocateEipResponseDto> allocateEipAddress(List<CoreAllocateEipRequestDto> requestDtoList);

    List<CoreReleaseEipResponseDto> releaseEipAddress(List<CoreReleaseEipRequestDto> requestDtoList);

    List<CoreAssociateEipResponseDto> associateEipAddress(List<CoreAssociateEipRequestDto> requestDtoList);

    List<CoreUnAssociateEipResponseDto> unAssociateEipAddress(List<CoreUnAssociateEipRequestDto> requestDtoList);
}
