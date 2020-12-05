package net.tisseurdetoile.batch.socle.item.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Restartable {@link org.springframework.batch.item.ItemReader} that reads files (File) from a Directory (File)
 *
 * @author TisseurDeToile
 */
public class DirectoryItemReader extends AbstractItemCountingItemStreamItemReader<File> implements  InitializingBean {
    private File folder;
    private List<File> files;
    private FileFilter fileFilter;
    private boolean noInput = false;
    private boolean strict = true;
    private Comparator<File> comparator = null;
    private int readCount = 0;

    private static final Log logger = LogFactory.getLog(DirectoryItemReader.class);

    public DirectoryItemReader() {
        setName(ClassUtils.getShortName(DirectoryItemReader.class));
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public void setFileFilter(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    public void setComparator(Comparator<File> comparator) {
        this.comparator = comparator;
    }

    /**
     *
     * @param folder the folder (File) in which we read le content
     */
    public void setFolder(File folder) {
        this.folder = folder;
    }

    @Override
    protected File doRead() {
        if (noInput) {
            return null;
        }

        return readFile();
    }

    private File readFile() {
        if (!files.isEmpty()) {
            readCount++;
            return files.remove(0);
        }
        return null;
    }

    @Override
    protected void doOpen() {
        Assert.notNull(folder, "folder must be set");

        if (!folder.exists()) {
            if (strict) {
                throw new IllegalStateException("Input Folder must exist (reader is in 'strict' mode): " + folder);
            }
            logger.warn("Folder does not exist " + folder.getAbsolutePath());
            return;
        }

        if (!folder.canRead()) {
            if (strict) {
                throw new IllegalStateException("Input resource must be readable (reader is in 'strict' mode): "
                        + folder);
            }
            logger.warn("Folder is not readable " + folder.getAbsolutePath());
            return;
        }

        File[] arFiles;

        if (this.fileFilter != null) {
            arFiles = this.folder.listFiles(this.fileFilter);
        } else {
            arFiles = this.folder.listFiles();
        }

        if (arFiles == null || arFiles.length == 0) {
            noInput = false;
        } else {
            if (comparator == null) {
                Arrays.sort(arFiles);
            } else {
                Arrays.sort(arFiles, comparator);
            }

            files = new ArrayList<>(Arrays.asList(arFiles));
        }
    }

    @Override
    protected void doClose() {
        readCount = 0;
    }

    @Override
    protected void jumpToItem(int itemIndex) {
        for (int i = 0; i < itemIndex; i++) {
            readFile();
        }
    }

    @Override
    public void afterPropertiesSet(){
        // only for override Use
    }
}
