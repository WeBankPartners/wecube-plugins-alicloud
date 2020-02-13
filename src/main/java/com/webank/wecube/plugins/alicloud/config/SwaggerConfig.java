package com.webank.wecube.plugins.alicloud.config;

import com.webank.wecube.plugins.alicloud.common.ApplicationConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author howechen
 */
@Configuration
@EnableSwagger2
@Profile(value = {ApplicationConstants.Profile.DEV, ApplicationConstants.Profile.TEST})
public class SwaggerConfig {

    public static final String SWAGGER_BASE_PACKAGE = ApplicationConstants.ApiInfo.BASE_PACKAGE;
    public static final String SWAGGER_VERSION = ApplicationConstants.ApiInfo.V1;
    public static final String SWAGGER_TITLE = ApplicationConstants.ApiInfo.TITLE;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_BASE_PACKAGE)).paths(PathSelectors.any()).build();
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(SWAGGER_TITLE)
                .version(SWAGGER_VERSION)
                .build();
    }
}
