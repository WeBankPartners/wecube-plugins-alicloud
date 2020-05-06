package com.webank.wecube.plugins.alicloud.dto;

import com.webank.wecube.plugins.alicloud.dto.rds.db.CoreCreateDBInstanceRequestDto;
import com.webank.wecube.plugins.alicloud.service.rds.RDSCategory;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class RDSFieldAdaptionTest {
    private final CoreCreateDBInstanceRequestDto requestDto = new CoreCreateDBInstanceRequestDto();

    @Test
    void givenBasicCategory_handleZoneId_ShouldAdaptSucceed() {
        requestDto.setZoneId("ap-southeast-1maz1-b");
        requestDto.setCategory(RDSCategory.Basic.toString());
        requestDto.adaptToAliCloud();
        assert StringUtils.equals("ap-southeast-1b", requestDto.getZoneId());
    }

    @Test
    void givenBasicCategoryAndValidZoneId_adaptToAliCloud_ShouldAdaptSucceed() {
        requestDto.setCategory(RDSCategory.Basic.toString());
        requestDto.setZoneId("ap-southeast-1a");
        requestDto.adaptToAliCloud();
        assert StringUtils.equals("ap-southeast-1a", requestDto.getZoneId());
    }

    @Test
    void givenOtherCategoryAndListStringZoneId_adaptToAliCloud_ShouldAdaptSucceed() {
        requestDto.setCategory(RDSCategory.HighAvailability.toString());
        requestDto.setZoneId("[ap-southeast-1MAZ2-a, ap-southeast-1MAZ2-b]");
        requestDto.adaptToAliCloud();
        assert StringUtils.equals("ap-southeast-1MAZ2(a,b)", requestDto.getZoneId());
    }

    @Test
    void givenOtherCategoryAndValidStringZoneId_adaptToAliCloud_ShouldAdaptSucceed() {
        requestDto.setCategory(RDSCategory.HighAvailability.toString());
        requestDto.setZoneId("ap-southeast-1MAZ2(a,b)");
        requestDto.adaptToAliCloud();
        assert StringUtils.equals("ap-southeast-1MAZ2(a,b)", requestDto.getZoneId());
    }


}
