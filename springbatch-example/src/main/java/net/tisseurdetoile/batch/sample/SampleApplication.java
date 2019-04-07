package net.tisseurdetoile.batch.sample;

import net.tisseurdetoile.batch.sample.config.H2WebClient;
import net.tisseurdetoile.batch.socle.api.EnableSpringBatchSocleApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableSpringBatchSocleApi
@ComponentScan(basePackageClasses={H2WebClient.class})
public class SampleApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleApplication.class, args);
    }
}
