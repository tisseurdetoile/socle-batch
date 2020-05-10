package net.tisseurdetoile.batch.socle.reader;

import net.tisseurdetoile.batch.socle.vo.Region;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;

public class TestReaderBuilder  {
    public static FlatFileItemReader<Region> RegionReader (Resource resource) {
        return new FlatFileItemReaderBuilder<Region>()
                .name("regionReader")
                .linesToSkip(1)
                .resource(resource)
                .delimited()
                .delimiter(DelimitedLineTokenizer.DELIMITER_TAB)
                .names(new String[]{"regionId", "chefLieuCp", "tncc", "ncc", "nccenr"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Region>() {{
                    setTargetType(Region.class);
                }})
                .build();
    }
}
