package net.tisseurdetoile.batch.socle.api.job.exception;

public class JobAlreadyComplete extends JobException {

    public JobAlreadyComplete(String jobName) {
        super(jobName);
    }

    public JobAlreadyComplete(String message, String jobName) {
        super(message, jobName);
    }
}
