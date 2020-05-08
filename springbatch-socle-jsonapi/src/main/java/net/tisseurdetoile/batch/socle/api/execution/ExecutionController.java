package net.tisseurdetoile.batch.socle.api.execution;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import net.tisseurdetoile.batch.socle.api.job.JobErrorResource;
import net.tisseurdetoile.batch.socle.api.job.JobService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.hateoas.ResourceSupport;
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
     * Recuperer l'execution en parametre
     * @param executionId id de l'execution.
     * @return Ressource associe
     */
    @ApiOperation("View Execution Info")
    @GetMapping("/{executionId}.json")
    public ResourceSupport getExecution(@PathVariable long executionId) {

        try {
            JobExecution jobExecution = jobService.getJobExecution(executionId);
            return new ExecutionResource(jobExecution, timeZone);
        } catch (NoSuchJobExecutionException e) {
            log.error(NO_SUCH_EXECUTION_FOR_ID, executionId);

            return new JobErrorResource("no.such.executions", null, String.format(NO_SUCH_EXECUTION_FOR_ID_SF, executionId));
        }
    }

    @ApiOperation("Stop an Existing Execution")
    @DeleteMapping("/{executionId}.json")
    public ResourceSupport stopExecution(@PathVariable long executionId) {
        return stopOrDeleteExecution(executionId, false);
    }


    @ApiOperation("Stop and abandon an Execution")
    @DeleteMapping(value = "/{executionId}.json", params = {"abandon"})
    public ResourceSupport stopExecution(@PathVariable long executionId, @RequestParam("abandon") Boolean abandon) {
        return stopOrDeleteExecution(executionId, abandon);
    }

    private ResourceSupport stopOrDeleteExecution(long executionId, boolean abandon) {

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
            log.error(NO_SUCH_EXECUTION_FOR_ID, executionId);

            return new JobErrorResource("executions.not.found", null, String.format(NO_SUCH_EXECUTION_FOR_ID_SF, executionId));
        } catch (JobExecutionNotRunningException e) {
            log.error(NO_SUCH_EXECUTION_FOR_ID, executionId);
            return new JobErrorResource("executions.not.running", null, String.format(NO_SUCH_EXECUTION_FOR_ID_SF, executionId));
        } catch (JobExecutionAlreadyRunningException e) {
            log.error(NO_SUCH_EXECUTION_FOR_ID, executionId);
            return new JobErrorResource("executions.not.abandonable", null, String.format(NO_SUCH_EXECUTION_FOR_ID_SF, executionId));
        }
    }

    private static final String NO_SUCH_EXECUTION_FOR_ID = "No such execution for id : {}";
    private static final String NO_SUCH_EXECUTION_FOR_ID_SF = "No such execution for id : %s";
}
