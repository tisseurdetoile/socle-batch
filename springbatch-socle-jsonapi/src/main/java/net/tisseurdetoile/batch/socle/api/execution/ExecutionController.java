package net.tisseurdetoile.batch.socle.api.execution;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import net.tisseurdetoile.batch.socle.api.execution.exception.ExecutionAlreadyRunning;
import net.tisseurdetoile.batch.socle.api.execution.exception.ExecutionNotFound;
import net.tisseurdetoile.batch.socle.api.execution.exception.ExecutionNotRunning;
import net.tisseurdetoile.batch.socle.api.job.JobService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.web.bind.annotation.*;

import java.util.TimeZone;

@RestController
@Log4j2
@RequestMapping(value = "/executions", produces = "application/hal+json")
public class ExecutionController {

    public ExecutionController(JobService jobService) {
        this.jobService = jobService;
    }

    private final JobService jobService;

    private final TimeZone timeZone = TimeZone.getDefault();


    /**
     * Récupérer l'execution en paramètre
     * @param executionId id de l'execution.
     * @return Ressource associe
     */
    @Operation(summary = "View Execution Info")
    @GetMapping("/{executionId}.json")
    public ExecutionResource getExecution(@PathVariable long executionId) {

        try {
            JobExecution jobExecution = jobService.getJobExecution(executionId);
            return new ExecutionResource(jobExecution, timeZone);
        } catch (NoSuchJobExecutionException e) {
            log.error("No such execution for id : {}", executionId);
            throw new ExecutionNotFound(executionId);
        }
    }

    @Operation(summary = "Stop an Existing Execution")
    @DeleteMapping("/{executionId}.json")
    public ExecutionResource stopExecution(@PathVariable long executionId) {
        return stopOrDeleteExecution(executionId, false);
    }

    @Operation(summary = "Stop and abandon an Execution")
    @DeleteMapping(value = "/{executionId}.json", params = {"abandon"})
    public ExecutionResource stopExecution(@PathVariable long executionId, @RequestParam("abandon") Boolean abandon) {
        return stopOrDeleteExecution(executionId, abandon);
    }

    private ExecutionResource stopOrDeleteExecution(long executionId, boolean abandon) {

        JobExecution jobExecution;
        JobExecution stoppedJobExecution;

        try {
            jobExecution = jobService.getJobExecution(executionId);
            log.debug("JobExecution.isRunning = {}", jobExecution.isRunning());

            if (!abandon) {
                stoppedJobExecution = jobService.stop(executionId);
            } else {
                stoppedJobExecution = jobService.abandon(executionId);
            }

            return new ExecutionResource(stoppedJobExecution, timeZone);

        } catch (NoSuchJobExecutionException e) {
            log.error("No such execution for id : {}", executionId);
            throw new ExecutionNotFound(executionId);
        } catch (JobExecutionNotRunningException e) {
            log.error("Not running execution for id : {}", executionId);
            throw new ExecutionNotRunning(executionId);
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("Execution AlreadyRunning for id {}", executionId);
            throw new ExecutionAlreadyRunning(executionId);
        }
    }
}
