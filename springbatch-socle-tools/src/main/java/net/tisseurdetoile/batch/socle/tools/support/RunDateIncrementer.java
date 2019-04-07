package net.tisseurdetoile.batch.socle.tools.support;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * RunIdIncrementer qui joute un run.dateId = yyyyMMJJ si il n'est passé en paramètre
 * permet de base generer un Job avec id journalier
 */
public class RunDateIncrementer implements JobParametersIncrementer {
    private static final String RUN_ID_KEY = "run.dateId";
    private static final String DATE_FORMAT = "yyyyMMdd";

    private String key;
    private String dateFormat;

    public RunDateIncrementer() {
        this.key = RUN_ID_KEY;
        this.dateFormat = DATE_FORMAT;
    }

    public RunDateIncrementer(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public RunDateIncrementer(String dateFormat, String key) {
        this.dateFormat = dateFormat;
        this.key = key;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override public JobParameters getNext(JobParameters jobParameters) {
        JobParameters params = jobParameters == null ? new JobParameters() : jobParameters;

        if (params.getString(this.key) == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(this.dateFormat);
            LocalDate today = LocalDate.now();
            return (new JobParametersBuilder(params)).addString(this.key, today.format(formatter)).toJobParameters();
        }

        return params;
    }
}
