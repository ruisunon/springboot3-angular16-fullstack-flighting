package io.rxs.flights.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;
import static org.springdoc.core.utils.Constants.ALL_PATTERN;
@Configuration
public class SwaggerConfig {
    
    @Value("${app.version}")
    private String appVersion;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                   .components(new Components()
                                   .addSecuritySchemes("bearer-key",
                                       new SecurityScheme()
                                           .type(HTTP)
                                           .scheme("bearer")
                                           .bearerFormat("JWT")))
                   .info(new Info()
                             .title("REST API for Flight Advisor Service.")
                             .version(this.appVersion)
                             .license(new License()
                                          .name("MIT License")
                                          .url("https://springdoc.org")));
    }

//    @Bean
//    @Profile("!prod")
//    public GroupedOpenApi actuatorApi(OpenApiCustomizer actuatorOpenApiCustomiser,
//                                      //OperationCustomizer actuatorCustomizer,
//                                      WebEndpointProperties endpointProperties,
//                                      @Value("${app.version}") String appVersion) {
//        return GroupedOpenApi.builder()
//                .group("Actuator")
//                .pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
//                .addOpenApiCustomizer(actuatorOpenApiCustomiser)
//                .addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Actuator API").version(appVersion)))
//                //.addOperationCustomizer(actuatorCustomizer)
//                .pathsToExclude("/health/*")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi usersGroup(@Value("${app.version}") String appVersion) {
//        return GroupedOpenApi.builder().group("users")
//                .addOperationCustomizer((operation, handlerMethod) -> {
//                    operation.addSecurityItem(new SecurityRequirement().addList("basicScheme"));
//                    return operation;
//                })
//                .addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Users API").version(appVersion)))
//                .packagesToScan("org.springdoc.demo.app2")
//                .build();
//    }
    
}
