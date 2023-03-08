package com.alibou.security.swagger;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(securityContext())
                .securitySchemes(List.of(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }
    private List<SecurityContext> securityContext() {
        return Collections.singletonList(SecurityContext.builder().securityReferences(sf()).build());
    }
    private List<SecurityReference> sf(){
        AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");
        return List.of(new SecurityReference("JWT", new AuthorizationScope[]{scope}));
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Crm App")
                .description("Crm App with Spring Boot 3")
                .build();
    }
}
