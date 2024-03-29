package net.tisseurdetoile.batch.socle.api.config;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Custom configuration for the springbatch json API
 */
@Configuration
@EnableBatchProcessing
public class CustomBatchConfig extends DefaultBatchConfigurer {

    private final JobRepository jobRepository;

    @Value("${spring.batch.jsonapi.poolsize:3}")
    private int poolSize;

    @Value("${spring.batch.jsonapi.maxpoolsize:6}")
    private int maxPoolSize;

    @Value("${spring.batch.jsonapi.queuesize:2}")
    private int queueSize;

    CustomBatchConfig (JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueSize);
        executor.setThreadNamePrefix("jsonApiPoolExecutor-");
        executor.initialize();

        return executor;
    }

    /**
     * NOTA :
     * If you set the table prefix on the job repository,
     * don’t forget to set it on the job explorer as well.
     * END NOTA
     *
     * @param jobExplorer {@link org.springframework.batch.core.explore.JobExplorer}
     * @param jobRepository {@link org.springframework.batch.core.repository.JobRepository}
     * @param jobRegistry {@link org.springframework.batch.core.configuration.JobRegistry}
     * @param jobLauncher {@link org.springframework.batch.core.launch.JobLauncher}
     * @return {@link org.springframework.batch.core.explore.JobExplorer}
     */
    @Bean
    public SimpleJobOperator jobOperator(JobExplorer jobExplorer,
                                         JobRepository jobRepository,
                                         JobRegistry jobRegistry,
                                         JobLauncher jobLauncher) {

        SimpleJobOperator jobOperator = new SimpleJobOperator();

        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.setJobLauncher(jobLauncher);

        return jobOperator;
    }

    @Override
    protected JobLauncher createJobLauncher() {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(taskExecutor());

        return jobLauncher;
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }
}
