package net.tisseurdetoile.batch.socle.api.job;

import net.tisseurdetoile.batch.socle.api.job.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class JobExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JobAlreadyComplete.class)
    protected ResponseEntity<Object> handleJobAlreadyComplete(JobException ex, WebRequest request) {
        JobErrorResource error =  new JobErrorResource("job.already.complete", new JobResource(ex.getJobName(), 0), String.format("A job with this name : %s and parameters already completed successfully.", ex.getJobName()) );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JobAlreadyRunning.class)
    protected ResponseEntity<Object> handleJobAlreadyRunning(JobException ex, WebRequest request) {
        JobErrorResource error =  new JobErrorResource("job.already.running", new JobResource(ex.getJobName(), 0), String.format("A job with this name  %s and parameters is already running.", ex.getJobName()) );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JobCouldNotRestart.class)
    protected ResponseEntity<Object> handleJobCouldNotRestart(JobException ex, WebRequest request) {
        JobErrorResource error =  new JobErrorResource("job.parameters.invalid", new JobResource(ex.getJobName(), 0), String.format("The job %s was not able to restart.", ex.getJobName()) );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JobInvalidParameters.class)
    protected ResponseEntity<Object> handleInvalidParameters(JobException ex, WebRequest request) {
        JobErrorResource error =  new JobErrorResource("job.could.not.restart", new JobResource(ex.getJobName(), 0), String.format("The job parameters for %s are invalid according to the configuration", ex.getJobName()) );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JobNotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(JobException ex, WebRequest request) {
        JobErrorResource error =  new JobErrorResource("no.such.job", new JobResource(ex.getJobName(), 0), String.format( "No such job for name: %s", ex.getJobName()) );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JobException.class)
    protected ResponseEntity<Object> handleOther(JobException ex, WebRequest request) {
        JobErrorResource error =  new JobErrorResource("job.error", new JobResource(ex.getJobName(), 0), String.format( "Error in job name: %s", ex.getJobName()) );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
