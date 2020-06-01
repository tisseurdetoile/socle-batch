package net.tisseurdetoile.batch.socle.api.execution.exception;

public class ExecutionException extends RuntimeException {
    private final long id;

    public ExecutionException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public ExecutionException(String message, long id) {
        super(message);
        this.id = id;
    }

    public ExecutionException(String message, Throwable cause, long id) {
        super(message, cause);
        this.id = id;
    }

    public ExecutionException(Throwable cause, long id) {
        super(cause);
        this.id = id;
    }

    public ExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, long id) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.id = id;
    }
}
