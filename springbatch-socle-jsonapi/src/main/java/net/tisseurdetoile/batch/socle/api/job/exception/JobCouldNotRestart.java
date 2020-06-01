package net.tisseurdetoile.batch.socle.api.job.exception;

public class JobCouldNotRestart extends JobException {

    public JobCouldNotRestart(String jobName) {
        super(jobName);
    }

    public JobCouldNotRestart(String message, String jobName) {
        super(message, jobName);
    }
}
