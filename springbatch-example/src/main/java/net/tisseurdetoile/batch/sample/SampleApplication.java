package net.tisseurdetoile.batch.sample;

import net.tisseurdetoile.batch.socle.api.EnableSpringBatchSocleApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSpringBatchSocleApi
public class SampleApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleApplication.class, args);
    }
}
