package net.tisseurdetoile.batch.socle.api;

import net.tisseurdetoile.batch.socle.api.config.CustomBatchConfig;
import net.tisseurdetoile.batch.socle.api.config.OpenApiConfig;
import net.tisseurdetoile.batch.socle.api.execution.ExecutionController;
import net.tisseurdetoile.batch.socle.api.job.JobController;
import net.tisseurdetoile.batch.socle.api.jobexplorer.JobExplorerService;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@ComponentScan(basePackages = { "net.tisseurdetoile.batch.job" }, basePackageClasses = { ExecutionController.class,
        JobController.class, JobExplorerService.class, CustomBatchConfig.class, OpenApiConfig.class })
public class SpringBatchSocleApiConfiguration {

    @Autowired(required = false)
    BuildProperties buildProperties;

}
