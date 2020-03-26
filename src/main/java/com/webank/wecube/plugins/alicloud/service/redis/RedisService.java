package com.webank.wecube.plugins.alicloud.service.redis;

import com.webank.wecube.plugins.alicloud.dto.redis.CoreCreateInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreCreateInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreDeleteInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreDeleteInstanceResponseDto;

import java.util.List;

/**
 * @author howechen
 */
public interface RedisService {

    List<CoreCreateInstanceResponseDto> createInstance(List<CoreCreateInstanceRequestDto> coreCreateInstanceRequestDtoList);

    List<CoreDeleteInstanceResponseDto> deleteInstance(List<CoreDeleteInstanceRequestDto> coreDeleteInstanceRequestDtoList);
}
