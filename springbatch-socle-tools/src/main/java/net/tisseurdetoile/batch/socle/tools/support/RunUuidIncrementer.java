package net.tisseurdetoile.batch.socle.tools.support;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.util.UUID;

/**
 * RunIdIncrementer qui joute un run.uuid = <UUID> si il n'est passé en paramètre
 * permet de base generer un Job avec id junique
 */
public class RunUuidIncrementer implements JobParametersIncrementer {
    private static final String RUN_ID_KEY = "run.uuid";

    private String key;

    public RunUuidIncrementer() {
        this.key = RUN_ID_KEY;
    }

    public RunUuidIncrementer(String key) {
        this.key = key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override public JobParameters getNext(JobParameters jobParameters) {
        JobParameters params = jobParameters == null ? new JobParameters() : jobParameters;
        UUID uuid = UUID.randomUUID();

        if (params.getString(this.key) == null) {
            return (new JobParametersBuilder(params)).addString(this.key, uuid.toString()).toJobParameters();
        }

        return params;
    }
}
