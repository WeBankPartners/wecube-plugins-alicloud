package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDto;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreCreateInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreCreateInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreDeleteInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.dto.redis.CoreDeleteInstanceResponseDto;
import com.webank.wecube.plugins.alicloud.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author howechen
 */
@RestController
@RequestMapping(ApplicationConstants.ApiInfo.URL_PREFIX + "/redis")
public class RedisController {

    private RedisService redisService;

    @Autowired
    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping(path = "/create")
    @ResponseBody
    public CoreResponseDto<CoreCreateInstanceResponseDto> createInstance(@RequestBody CoreRequestDto<CoreCreateInstanceRequestDto> request) {
        List<CoreCreateInstanceResponseDto> result = this.redisService.createInstance(request.getInputs());
        return new CoreResponseDto<CoreCreateInstanceResponseDto>().withErrorCheck(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDto<CoreDeleteInstanceResponseDto> deleteInstance(@RequestBody CoreRequestDto<CoreDeleteInstanceRequestDto> request) {
        List<CoreDeleteInstanceResponseDto> result = this.redisService.deleteInstance(request.getInputs());
        return new CoreResponseDto<CoreDeleteInstanceResponseDto>().withErrorCheck(result);
    }
}
