package net.tisseurdetoile.batch.socle.api.job.exception;

public class JobInvalidParameters extends JobException {

    public JobInvalidParameters(String jobName) {
        super(jobName);
    }

    public JobInvalidParameters(String message, String jobName) {
        super(message, jobName);
    }
}
