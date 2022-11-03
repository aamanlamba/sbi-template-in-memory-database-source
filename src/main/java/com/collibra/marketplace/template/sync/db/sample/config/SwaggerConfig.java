/*
 * (c) 2021 Collibra Inc. This software is protected under international copyright law. You may only
 * install and use this software subject to the license agreement available at
 * https://marketplace.collibra.com/binary-code-license-agreement/. If such an agreement is not in
 * place, you may not use the software.
 */

package com.collibra.marketplace.template.sync.db.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.collibra"))
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("File to Collibra Integration API")
            .description("Read from an XML/CSV file and import into Collibra")
            .build();
    }
}
