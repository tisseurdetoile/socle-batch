package net.tisseurdetoile.batch.socle.api.job.exception;

public class JobNotFoundException extends JobException {

    public JobNotFoundException(String jobName) {
        super(jobName);
    }

    public JobNotFoundException(String message, String jobName) {
        super(message, jobName);
    }
}
