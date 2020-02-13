package com.webank.wecube.plugins.alicloud;

import com.webank.wecube.plugins.alicloud.common.ApplicationProperties;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@EnableConfigurationProperties({
        ApplicationProperties.class,
})
public abstract class BaseSpringBootTest {
}
