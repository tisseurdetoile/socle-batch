package net.tisseurdetoile.batch.job;


import lombok.extern.log4j.Log4j2;
import net.tisseurdetoile.batch.socle.readerfactory.UnZipBufferedReaderFactory;
import net.tisseurdetoile.batch.socle.tools.item.ConsoleItemWriter;
import net.tisseurdetoile.batch.socle.tools.support.RunUuidIncrementer;
import net.tisseurdetoile.batch.vo.Region;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.BufferedReaderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;

@Log4j2
@Configuration
@EnableBatchProcessing
public class SampleInseeJob {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Value("${insee.region.file}")
    private UrlResource inseeRegionFile;

    public SampleInseeJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    @Bean(name = "regionReader")
    @StepScope
    protected FlatFileItemReader<Region> regionReader () throws MalformedURLException {

        BufferedReaderFactory bufferedReaderFactory = new UnZipBufferedReaderFactory();

        FlatFileItemReader<Region> flatFileItemReader = new FlatFileItemReaderBuilder<Region>()
                .name("regionReader")
                .linesToSkip(1)
                //.resource(readerFile)
                .resource(inseeRegionFile)
                .delimited()
                .delimiter(DelimitedLineTokenizer.DELIMITER_TAB) // NEW DELIMITER
                .names(new String[]{"regionId", "chefLieuCp", "tncc", "ncc", "nccenr"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Region>() {{
                    setTargetType(Region.class);
                }})
                .build();

        flatFileItemReader.setBufferedReaderFactory(bufferedReaderFactory);

        return flatFileItemReader;
    }

    @Bean(name = "regionWriter")
    @StepScope
    protected ItemWriter<Region> regionWriter() {
        ConsoleItemWriter<Region> consoleItemWriter = new ConsoleItemWriter<>();
        consoleItemWriter.setLineSeparator("");
        return consoleItemWriter;
    }

    /**
     * A classic Step with a reader / processor / writer
     * Note: there's a comment of the assignation of another Executor
     *
     * @param reader    reader from list
     * @param writer    writer to Log4j
     * @return a sample step
     */
    @Bean(name = "sInseeStep1")
    public Step step(
            @Qualifier("regionReader") FlatFileItemReader<Region> reader,
            @Qualifier("regionWriter") ItemWriter<Region> writer) {


        return stepBuilderFactory.get("step")
                .<Region, Region>chunk(3)
                .reader(reader)
                .writer(writer)
                //.taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job sampleInseeJobMain(@Qualifier("sInseeStep1") Step step) {
        return jobBuilderFactory.get("SampleInseeJob")
                .incrementer(new RunUuidIncrementer())
                .flow(step)
                .end()
                .build();
    }
}
