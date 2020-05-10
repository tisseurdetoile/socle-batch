package net.tisseurdetoile.batch.socle.readerfactory;

import lombok.extern.log4j.Log4j2;
import net.tisseurdetoile.batch.socle.reader.TestReaderBuilder;
import net.tisseurdetoile.batch.socle.vo.Region;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Log4j2
@RunWith(SpringJUnit4ClassRunner.class)
public class BufferedReaderFactoryTest {

    @Value("classpath:reg2018.gz")
    Resource gunZipFile;

    @Value("classpath:reg2018.zip")
    Resource zipFile;

    private Region getRegion () {
        Region region  = new Region();
        region.setRegionId("01");
        region.setChefLieuCp("97105");
        region.setTncc("3");
        region.setNcc("GUADELOUPE");
        region.setNccenr("Guadeloupe");

        return region;
    }

    @Test
    public void test_GUnZipBufferedReaderFactory() throws Exception {
        FlatFileItemReader<Region> regionFlatFileItemReader = TestReaderBuilder.RegionReader(gunZipFile);
        regionFlatFileItemReader.setBufferedReaderFactory(new GUnZipBufferedReaderFactory());

        regionFlatFileItemReader.open(new ExecutionContext());
        Region readRegion = regionFlatFileItemReader.read();

        Assert.assertEquals(getRegion(), readRegion);
    }

    @Test
    public void test_UnZipBufferedReaderFactory() throws Exception {
        FlatFileItemReader<Region> regionFlatFileItemReader = TestReaderBuilder.RegionReader(zipFile);
        regionFlatFileItemReader.setBufferedReaderFactory(new UnZipBufferedReaderFactory());

        regionFlatFileItemReader.open(new ExecutionContext());
        Region readRegion = regionFlatFileItemReader.read();

        Assert.assertEquals(getRegion(), readRegion);
    }
}