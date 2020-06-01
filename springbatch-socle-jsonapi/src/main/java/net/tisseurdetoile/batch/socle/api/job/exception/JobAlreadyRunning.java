package net.tisseurdetoile.batch.socle.api.job.exception;

public class JobAlreadyRunning extends JobException {

    public JobAlreadyRunning(String jobName) {
        super(jobName);
    }

    public JobAlreadyRunning(String message, String jobName) {
        super(message, jobName);
    }
}
