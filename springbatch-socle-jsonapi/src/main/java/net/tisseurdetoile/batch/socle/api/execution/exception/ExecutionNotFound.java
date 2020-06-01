package net.tisseurdetoile.batch.socle.api.execution.exception;

public class ExecutionNotFound extends ExecutionException {
    public ExecutionNotFound(long id) {
        super(id);
    }

    public ExecutionNotFound(String message, long id) {
        super(message, id);
    }
}
