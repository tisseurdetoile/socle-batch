package net.tisseurdetoile.batch.socle.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPIConfiguration(@Autowired(required = false) BuildProperties buildProperties) {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Socle Batch REST-API")
                        .version(buildProperties == null ? null : String.format("%s  -  Build time %s", buildProperties.getVersion(), buildProperties.getTime()))
                        .contact(new Contact().email("webmaster@tisseurdetoile.net"))
                        .description("REST API for launching and monitoring <a href=\"https://spring.io/projects/spring-batch\">springbatch</a>")
                        .license(new License().name("GPL 3.0").url("https://github.com/tisseurdetoile/socle-batch/blob/master/LICENCE")));
    }
}
