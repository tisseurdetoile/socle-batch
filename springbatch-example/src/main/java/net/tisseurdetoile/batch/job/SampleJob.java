package net.tisseurdetoile.batch.job;

import lombok.extern.log4j.Log4j2;
import net.tisseurdetoile.batch.socle.tools.support.RunUuidIncrementer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
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

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public SampleJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    /**
     * Simple reader that return the content of a list
     *
     * @return the reader
     */
    @Bean(name = "sjReader")
    @StepScope
    public ItemReader<String> reader() {
        log.debug("ItemReader<String> reader()");
        return new ListItemReader<>(Arrays.asList("SampleJob-0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24", "25", "26", "27", "28", "SampleJob-29"));
    }

    /**
     * Wait processor so the job take time to complete
     *
     * @return the processor
     */
    @Bean(name = "sjProcessor")
    @StepScope
    public ItemProcessor<String, String> processor() {
        return item -> {
            TimeUnit.SECONDS.sleep(5);
            return String.format("Processed %s", item);
        };
    }


    /**
     * Writer to log4j
     *
     * @return the log4j writer
     */
    @Bean(name = "sjWriter")
    @StepScope
    public ItemWriter<Object> writer() {
        return items -> log.info("Thread ({}) >{}<", Thread.currentThread().getName(), items);
    }

    /**
     * an example of a minimal tasklet.
     *
     * @return the tasklet
     */
    @Bean(name = "endTasklet")
    @StepScope
    public Tasklet endTasklet() {
        return (stepContribution, chunkContext) -> {
            log.info("EndTaskLet");
            return RepeatStatus.FINISHED;
        };
    }

    /**
     * A classic Step with a reader / processor / writer
     * Note: there's a comment of the assignation of another Executor
     *
     * @param reader    reader from list
     * @param writer    writer to Log4j
     * @param processor processor to wail
     * @return a sample step
     */
    @Bean(name = "sjStep")
    public Step step(
            @Qualifier("sjReader") ItemReader<String> reader,
            @Qualifier("sjWriter") ItemWriter<Object> writer,
            @Qualifier("sjProcessor") ItemProcessor<String, String> processor) {

        return stepBuilderFactory.get("step")
                .<String, String>chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                //.taskExecutor(taskExecutor())
                .build();
    }

    /**
     * A second step.
     *
     * @param endTaskLet the task at the end of the Step
     * @return a taskletStep
     */
    @Bean(name = "endStep")
    public Step endStep(@Qualifier("endTasklet") Tasklet endTaskLet) {
        return this.stepBuilderFactory.get("endStep")
                .tasklet(endTaskLet)
                .build();
    }


    /**
     * Sample executor for parallel Step execution.
     *
     * @return TaskExecutor
     */
    @Bean(name = "customTaskExecutor")
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("spring_batch");

        asyncTaskExecutor.setConcurrencyLimit(5);
        return asyncTaskExecutor;
    }

    /**
     * the Job Sample.
     *
     * @param step    classical step
     * @param endStep tasklet Step
     * @return a sampleJob
     */
    @Bean
    public Job sampleJobMain(@Qualifier("sjStep") Step step,
                             @Qualifier("endStep") Step endStep
    ) {
        return jobBuilderFactory.get("SampleJob")
                .incrementer(new RunUuidIncrementer())
                .flow(step)
                .next(endStep)
                .end()
                .build();
    }

}
