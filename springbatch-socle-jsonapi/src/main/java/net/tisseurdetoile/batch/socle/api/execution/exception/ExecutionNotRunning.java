package net.tisseurdetoile.batch.socle.api.execution.exception;

public class ExecutionNotRunning extends ExecutionException {
    public ExecutionNotRunning(long id) {
        super(id);
    }

    public ExecutionNotRunning(String message, long id) {
        super(message, id);
    }
}