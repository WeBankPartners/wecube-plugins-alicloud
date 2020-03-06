package com.webank.wecube.plugins.alicloud.service.disk;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.disk.*;

import java.util.List;

/**
 * @author howechen
 */
public interface DiskService {

    List<CoreCreateDiskResponseDto> createDisk(List<CoreCreateDiskRequestDto> coreCreateLoadBalancerRequestDtoList) throws PluginException;

    List<CoreDeleteDiskResponseDto> deleteDisk(List<CoreDeleteDiskRequestDto> coreDeleteLoadBalancerRequestDtoList) throws PluginException;

    List<CoreAttachDiskResponseDto> attachDisk(List<CoreAttachDiskRequestDto> coreAttachDiskRequestDtoList) throws PluginException;

    List<CoreDetachDiskResponseDto> detachDisk(List<CoreDetachDiskRequestDto> coreDetachDiskRequestDtoList) throws PluginException;

    DescribeDisksResponse retrieveDisk(IAcsClient client, String regionId, String diskId);
}
