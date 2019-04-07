package net.tisseurdetoile.batch.socle.api.config;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Verifier la pertinence de tout les element
 * cf. dans les logs
 * o.s.b.c.l.support.SimpleJobLauncher      : No TaskExecutor has been set, defaulting to synchronous executor.
 */
@Configuration
@EnableBatchProcessing
public class CustomBatchConfig extends DefaultBatchConfigurer {

    @Autowired
    private JobRepository jobRepository;

    //@Autowired
    //private JobRegistry jobRegistry;

    @Value("${spring.batch.num-thread:3}")
    private int threadSize;


    @Bean
    public TaskExecutor taskExecutor() {
        //SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        //taskExecutor.setConcurrencyLimit(4);
        return new ConcurrentTaskExecutor(getexecutorService());
    }

    @Bean
    public ExecutorService getexecutorService() {
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        return executorService;
    }

    @Bean
    public JobLauncher getJobLauncher() {
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
