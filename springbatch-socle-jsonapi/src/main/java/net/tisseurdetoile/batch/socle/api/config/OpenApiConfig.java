package net.tisseurdetoile.batch.socle.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * Open Api Configuration
     * @param buildProperties propriété valorisé par spring-boot-maven-plugin / build-info
     * @return OpenApi configuration
     */
    @Bean
    public OpenAPI openAPIConfiguration(@Autowired(required = false) BuildProperties buildProperties, GitProperties gitProperties) {
        Contact contact = new Contact()
                .email("webmaster@tisseurdetoile.net")
                .url("https://blog.tisseurdetoile.net/");


        String description = "REST API for launching and monitoring <a href=\"https://spring.io/projects/spring-batch\">spring-batch</a>";
        String title = "Socle Batch REST-API";
        License license = new License().name("GPL 3.0").url("https://github.com/tisseurdetoile/socle-batch/blob/master/LICENCE");
        String version = buildProperties == null ? null : String.format("%s  -  %s commit:%s", buildProperties.getVersion(), buildProperties.getTime(), gitProperties.getShortCommitId());

        Info info = new Info().title(title).version(version).contact(contact).description(description).license(license);

        ExternalDocumentation externalDocumentation = new ExternalDocumentation().description("Github repo").url("https://github.com/tisseurdetoile/socle-batch");

        return new OpenAPI()
                .components(new Components())
                .externalDocs(externalDocumentation)
                .info(info);
    }
}
