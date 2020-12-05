package net.tisseurdetoile.batch.socle.readerfactory;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.file.BufferedReaderFactory;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipInputStream;

@Log4j2
public class UnZipBufferedReaderFactory implements BufferedReaderFactory {

    /** Default value for gzip suffixes. */
    private List<String> suffixes = Collections.singletonList(".zip");

    /**
     * Créer un Bufferedreader pour les fichier zip
     * Nota : un zip ne contient plusieur fichier on positionne le reader sur la premiere entrée
     *
     * @param resource resource representant le fichier
     * @param encoding encodage des fichier
     * @return un reader sur le fichier
     * @throws UnsupportedEncodingException Encodage incorrect
     * @throws IOException problème rencontrés pendant le décodage
     */
    @Override
    public BufferedReader create(Resource resource, String encoding)  throws IOException {
        log.debug("Resource {} encoding {}", resource.getFilename(), encoding);
        for (String suffix : suffixes) {
            boolean endWithSuffix = Objects.requireNonNull(resource.getFilename()).endsWith(suffix);
            if (endWithSuffix  || resource.getDescription().endsWith(suffix)) {
                log.debug("suffix {} OK {}", suffix, "ZipInputStream");
                ZipInputStream zipInputStream = new ZipInputStream(resource.getInputStream());
                zipInputStream.getNextEntry();
                return new BufferedReader(new InputStreamReader(zipInputStream, encoding));
            }
        }
        return new BufferedReader(new InputStreamReader(resource.getInputStream(), encoding));
    }

    public List<String> getSuffixes() {
        return suffixes;
    }

    public void setSuffixes(List<String> suffixes) {
        this.suffixes = suffixes;
    }
}
