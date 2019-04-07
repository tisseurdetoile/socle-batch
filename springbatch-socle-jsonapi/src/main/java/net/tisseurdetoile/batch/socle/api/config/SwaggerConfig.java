package net.tisseurdetoile.batch.socle.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.not;
import static java.util.Collections.emptyList;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Profile("swagger")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired(required = false)
    BuildProperties buildProperties;

    @Bean
    Docket api() {
        return new Docket(SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .paths(not(regex("/error.*")))
                .build().apiInfo(new ApiInfo("Spring Batch Socle API",
                        "REST API for controlling and viewing <a href=\"https://spring.io/projects/spring-batch\">Spring Batch</a> jobs.",
                        buildProperties == null ? null : String.format("%s  -  Build time %s", buildProperties.getVersion(), buildProperties.getTime()),
                        null,
                        new Contact("Github", "https://github.com/tisseurdetoile/...", null),
                        "Apache License 2.0",
                        "https://github.com/tisseurdetoile/.../blob/master/LICENSE", emptyList()));
    }
}
