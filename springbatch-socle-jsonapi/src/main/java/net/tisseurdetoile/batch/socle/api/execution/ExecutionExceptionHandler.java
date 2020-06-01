package net.tisseurdetoile.batch.socle.api.execution;

import net.tisseurdetoile.batch.socle.api.execution.exception.ExecutionAlreadyRunning;
import net.tisseurdetoile.batch.socle.api.execution.exception.ExecutionException;
import net.tisseurdetoile.batch.socle.api.execution.exception.ExecutionNotFound;
import net.tisseurdetoile.batch.socle.api.execution.exception.ExecutionNotRunning;
import net.tisseurdetoile.batch.socle.api.job.JobErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExecutionExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ExecutionNotRunning.class)
    protected ResponseEntity<Object> handleJobAlreadyComplete(ExecutionException ex, WebRequest request) {
        JobErrorResource error =  new JobErrorResource("executions.not.running", null, String.format("No runninf execution running for id : %s", ex.getId()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExecutionNotFound.class)
    protected ResponseEntity<Object> handleJobAlreadyRunning(ExecutionException ex, WebRequest request) {
        JobErrorResource error =  new JobErrorResource("executions.not.found", null, String.format("No such execution for id : %s", ex.getId()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExecutionAlreadyRunning.class)
    protected ResponseEntity<Object> handleJobCouldNotRestart(ExecutionException ex, WebRequest request) {
        JobErrorResource error =  new JobErrorResource("executions.not.abandonable", null, String.format("No abandonable such execution for id : %s", ex.getId()) );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
