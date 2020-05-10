package net.tisseurdetoile.batch.socle.readerfactory;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.file.BufferedReaderFactory;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

@Log4j2
public class GUnZipBufferedReaderFactory implements BufferedReaderFactory {

    /** Default value for gzip suffixes. */
    private List<String> suffixes = Arrays.asList(".gz", ".gzip");

    /**
     * Créer un Bufferedreader pour les fichier gzip
     * Nota : un gzip ne contient qu'un fichier on renvoie directement le BufferedReader de l'inputStream
     *
     * @param resource resource representant le fichier
     * @param encoding encodage des fichier
     * @return un reader sur le fichier
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    @Override
    public BufferedReader create(Resource resource, String encoding) throws IOException {
        log.debug("Resource {} encoding {}", resource.getFilename(), encoding);
        for (String suffix : suffixes) {
            boolean endWithSuffix = Objects.requireNonNull(resource.getFilename()).endsWith(suffix);
            if (endWithSuffix  || resource.getDescription().endsWith(suffix)) {
                log.debug("suffix {} OK {}", suffix, "GZIPInputStream");
                return new BufferedReader(new InputStreamReader(new GZIPInputStream(resource.getInputStream()), encoding));
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
