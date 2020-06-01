package net.tisseurdetoile.batch.socle.api.execution.exception;

public class ExecutionNotAbandonable extends ExecutionException {
    public ExecutionNotAbandonable(long id) {
        super(id);
    }

    public ExecutionNotAbandonable(String message, long id) {
        super(message, id);
    }
}
