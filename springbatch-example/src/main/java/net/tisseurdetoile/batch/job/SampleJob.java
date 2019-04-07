package net.tisseurdetoile.batch.job;

import lombok.extern.log4j.Log4j2;
import net.tisseurdetoile.batch.socle.tools.support.RunUuidIncrementer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Log4j2
@Configuration
@EnableBatchProcessing
public class SampleJob {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public JobRegistry jobRegistry;

    @Bean(name = "sjReader")
    @StepScope
    public ItemReader<String> reader() {
        log.debug("ItemReader<String> reader()");
        return new ListItemReader<>(Arrays.asList("SampleJob-0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24", "25", "26", "27", "28", "SampleJob-29"));
    }

    @Bean(name= "sjProcessor")
    @StepScope
    public ItemProcessor<String, String> processor() {
        return new ItemProcessor<>() {
            @Override
            public String process(String s) throws Exception {
                TimeUnit.SECONDS.sleep(5);
                return String.format("Processed %s", s);
            }
        };
    }

    @Bean(name = "sjWriter")
    @StepScope
    public ItemWriter<? super Object> writer() {
        log.debug("ItemWriter<? super Object> writer()");
        return (ItemWriter<Object>) items -> System.out.println(String.format("Thread (%s) >%s<", Thread.currentThread().getName() ,items));
    }

    @Bean(name = "sjStep")
    public Step step(
            @Qualifier("sjReader") ItemReader reader,
            @Qualifier("sjWriter") ItemWriter writer,
            @Qualifier("sjProcessor") ItemProcessor processor) {

        return stepBuilderFactory.get("step")
                .<String, String> chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                //.taskExecutor(taskExecutor())
                .build();
    }

    @Bean(name="customTaskExecutor")
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor asyncTaskExecutor=new SimpleAsyncTaskExecutor("spring_batch");

        asyncTaskExecutor.setConcurrencyLimit(5);
        return asyncTaskExecutor;
    }

    @Bean
    public Job SampleJobMain(@Qualifier("sjStep") Step step) throws DuplicateJobException {
        Job mainJob = jobBuilderFactory.get("SampleJob")
                .incrementer(new RunUuidIncrementer())
                .flow(step)
                .end()
                .build();


        /**
         * On réference à main je job
         * utiliser le AutomaticJobRegistrar à l'avenir
         */
        //ReferenceJobFactory referenceJobFactory = new ReferenceJobFactory(mainJob);
        //jobRegistry.register(referenceJobFactory);

        return mainJob;
    }

}
