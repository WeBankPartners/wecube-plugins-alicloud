package com.webank.wecube.plugins.alicloud.controller;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import com.webank.wecube.plugins.alicloud.common.PluginException;
import com.webank.wecube.plugins.alicloud.dto.CoreRequestDtoBkp;
import com.webank.wecube.plugins.alicloud.dto.CoreResponseDtoBkp;
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
    public CoreResponseDtoBkp<?> createInstance(@RequestBody CoreRequestDtoBkp<CoreCreateInstanceRequestDto> requestBody) {
        List<CoreCreateInstanceResponseDto> result;
        try {
            result = this.redisService.createInstance(requestBody.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreCreateInstanceResponseDto>().okayWithData(result);
    }

    @PostMapping(path = "/delete")
    @ResponseBody
    public CoreResponseDtoBkp<?> deleteInstance(@RequestBody CoreRequestDtoBkp<CoreDeleteInstanceRequestDto> requestBody) {
        List<CoreDeleteInstanceResponseDto> result;
        try {
            result = this.redisService.deleteInstance(requestBody.getInputs());
        } catch (PluginException ex) {
            return CoreResponseDtoBkp.error(ex.getMessage());
        }
        return new CoreResponseDtoBkp<CoreDeleteInstanceResponseDto>().okayWithData(result);
    }
}
