package com.webank.wecube.plugins.alicloud.integration;

import com.webank.wecube.plugins.alicloud.BaseSpringBootTest;
import com.webank.wecube.plugins.alicloud.dto.CloudParamDto;
import org.junit.Test;

public class IntegrationTest extends BaseSpringBootTest {
    // constants
    private static final Integer REQUEST_TIME = 900;
    private static final Boolean CREATE_EXPENSIVE_RESOUCE = false;
    private static final Boolean ONLY_PRINT_IMAGES = false;

    // environment variables
    private String regionId;
    private String accessKeyId;
    private String secret;

    private CloudParamDto getCloudProviderParam() {
        CloudParamDto cloudParam = new CloudParamDto(regionId);
        return cloudParam;
    }
    @Test
    public void sampleTest() {
        System.out.println("test");
    }


}
