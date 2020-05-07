package com.webank.wecube.plugins.alicloud.dto;

import com.webank.wecube.plugins.alicloud.dto.redis.CoreCreateInstanceRequestDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class RedisFieldAdaptionTest {
    private final CoreCreateInstanceRequestDto requestDto = new CoreCreateInstanceRequestDto();

    @Test
    void givenSingleZoneId_adaptToAliCloud_ShouldAdaptSucceed() {
        requestDto.setZoneId("ap-southeast-1maz1-b");
        requestDto.adaptToAliCloud();
        assert StringUtils.equals("ap-southeast-1b", requestDto.getZoneId());
    }

    @Test
    void givenSingleValidZoneId_adaptToAliCloud_ShouldAdaptSucceed() {
        requestDto.setZoneId("ap-southeast-1a");
        requestDto.adaptToAliCloud();
        assert StringUtils.equals("ap-southeast-1a", requestDto.getZoneId());
    }

    @Test
    void givenHAZoneId_adaptToAliCloud_ShouldAdaptSucceed() {
        requestDto.setZoneId("[ap-southeast-1MAZ2-a, ap-southeast-1MAZ2-b]");
        requestDto.adaptToAliCloud();
        assert StringUtils.equals("ap-southeast-1MAZ2(a,b)", requestDto.getZoneId());
    }

    @Test
    void givenHAValidZoneId_adaptToAliCloud_ShouldAdaptSucceed() {
        requestDto.setZoneId("ap-southeast-1MAZ2(a,b)");
        requestDto.adaptToAliCloud();
        assert StringUtils.equals("ap-southeast-1MAZ2(a,b)", requestDto.getZoneId());
    }


}
