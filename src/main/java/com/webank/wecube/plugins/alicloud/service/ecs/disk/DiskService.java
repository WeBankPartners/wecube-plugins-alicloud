package com.webank.wecube.plugins.alicloud.service.ecs.disk;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.ecs.disk.*;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;

import java.util.List;

/**
 * @author howechen
 */
public interface DiskService {

    List<CoreCreateDiskResponseDto> createDisk(List<CoreCreateDiskRequestDto> coreCreateLoadBalancerRequestDtoList);

    List<CoreDeleteDiskResponseDto> deleteDisk(List<CoreDeleteDiskRequestDto> coreDeleteLoadBalancerRequestDtoList);

    List<CoreAttachDiskResponseDto> attachDisk(List<CoreAttachDiskRequestDto> coreAttachDiskRequestDtoList);

    List<CoreDetachDiskResponseDto> detachDisk(List<CoreDetachDiskRequestDto> coreDetachDiskRequestDtoList);

    DescribeDisksResponse retrieveDisk(IAcsClient client, String regionId, String diskId) throws PluginException, AliCloudException;
}
