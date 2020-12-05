package net.tisseurdetoile.batch.socle.item.file;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;


import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;

/**
 * Tests for {@link DirectoryItemReader}.
 */
public class DirectoryItemReaderTest {

    private final DirectoryItemReader reader = new DirectoryItemReader();
    private final ExecutionContext executionContext = new ExecutionContext();

    @Before
    public void setUp() {
        reader.setFolder(new File("./src/test/resources/dirtest"));
    }

    @Test
    public void testDirectoryItemReaderSimple() throws Exception {
        //WITH
        //filename is uniq.uni
        FileFilter uniqFileFilter = pathname -> "uniq.uni".equalsIgnoreCase(pathname.getName());
        reader.setFileFilter(uniqFileFilter);
        reader.setStrict(true);

        reader.open(executionContext);

        File appProperties = reader.read();
        File noMoreFiles = reader.read();

        assertNull("no more file", noMoreFiles);
        assertNotNull("application.properties", appProperties);
    }

    @Test
    public void testDirectoryItemReaderRestartable() throws Exception {
        File readFile;
        //WITH
        //filename is *.txt
        FileFilter txtFileFilter = pathname -> pathname.getName().endsWith(".txt");
        reader.setFileFilter(txtFileFilter);
        reader.setComparator(Comparator.comparingLong(File::lastModified).reversed());

        reader.open(executionContext);

        readFile = reader.read();
        assertNotNull("First File",readFile);

        reader.update(executionContext);
        reader.close();

        reader.open(executionContext);

        readFile = reader.read();
        assertNotNull("Second File", readFile);
        readFile = reader.read();
        assertNotNull("Third File", readFile);
        readFile = reader.read();
        assertNull("No more file", readFile);
    }
}