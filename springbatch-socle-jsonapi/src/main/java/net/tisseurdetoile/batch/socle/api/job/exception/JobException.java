package net.tisseurdetoile.batch.socle.api.job.exception;

public class JobException extends  RuntimeException{

    private final String jobName;

    public JobException(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public JobException(String message, String jobName) {
        super(message);
        this.jobName = jobName;
    }

    public JobException(String message, Throwable cause, String jobName) {
        super(message, cause);
        this.jobName = jobName;
    }

    public JobException(Throwable cause, String jobName) {
        super(cause);
        this.jobName = jobName;
    }

    public JobException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String jobName) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.jobName = jobName;
    }
}
