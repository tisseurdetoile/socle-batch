package net.tisseurdetoile.batch.socle.api.execution.exception;

public class ExecutionAlreadyRunning extends ExecutionException {
    public ExecutionAlreadyRunning(long id) {
        super(id);
    }

    public ExecutionAlreadyRunning(String message, long id) {
        super(message, id);
    }
}
