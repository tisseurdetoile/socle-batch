package net.tisseurdetoile.batch.socle.api.support;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.support.PropertiesConverter;

import java.util.Properties;

/**
 * Helper class for extracting a String representation of {@link JobParameters}
 * for rendering.
 *
 * @author Dave Syer
 *
 */
public class JobParametersExtractor {

    private JobParametersConverter converter = new DefaultJobParametersConverter();

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * @param oldParameters the latest job parameters
     * @return a String representation for rendering the job parameters from the
     * last instance
     */
    public String fromJobParameters(JobParameters oldParameters) {

        String properties = PropertiesConverter.propertiesToString(converter.getProperties(oldParameters));
        if (properties.startsWith("#")) {
            properties = properties.substring(properties.indexOf(LINE_SEPARATOR) + LINE_SEPARATOR.length());
        }
        properties = properties.replace("\\:", ":");
        return properties;

    }

    public JobParameters fromString(String params) {
        Properties properties = PropertiesConverter.stringToProperties(params);
        return converter.getJobParameters(properties);
    }


}
