package com.webank.wecube.plugins.alicloud.service.ecs.disk;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.ecs.disk.*;
import com.webank.wecube.plugins.alicloud.support.AliCloudException;

import java.util.List;

/**
 * @author howechen
 */
public interface DiskService {

    List<CoreCreateAttachDiskResponseDto> createAttachDisk(List<CoreCreateAttachDiskRequestDto> coreCreateAttachDiskRequestDtoList);

    List<CoreDetachDeleteDiskResponseDto> detachDeleteDisk(List<CoreDetachDeleteDiskRequestDto> coreDetachDeleteDiskRequestDtoList);

    DescribeDisksResponse retrieveDisk(IAcsClient client, String regionId, String diskId) throws PluginException, AliCloudException;

    Boolean ifDiskInStatus(IAcsClient client, String regionId, String diskId, DiskStatus status) throws PluginException, AliCloudException;
}
