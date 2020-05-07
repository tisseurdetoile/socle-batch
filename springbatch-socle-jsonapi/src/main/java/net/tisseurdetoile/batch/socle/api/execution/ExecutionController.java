package net.tisseurdetoile.batch.socle.api.execution;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import net.tisseurdetoile.batch.socle.api.job.JobErrorResource;
import net.tisseurdetoile.batch.socle.api.job.JobService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.*;

import java.util.TimeZone;


@RestController
@Log4j2
@RequestMapping(value = "/executions", produces = "application/hal+json")
public class ExecutionController {

    @Autowired
    private JobService jobService;

    private TimeZone timeZone = TimeZone.getDefault();


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
            log.error(String.format(String.format("No such execution for id : %s", executionId)));

            // TODO correct error
            JobErrorResource jobErrorResource = new JobErrorResource("no.such.executions", null, String.format("No such execution for id : %s", executionId));
            return jobErrorResource;
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
            if (!abandon) {
                jobExecution = jobService.getJobExecution(executionId);
                stoppedJobExecution = jobService.stop(executionId);
            } else {
                jobExecution = jobService.getJobExecution(executionId);
                stoppedJobExecution = jobService.abandon(executionId);
            }

            return new ExecutionResource(stoppedJobExecution, timeZone);

        } catch (NoSuchJobExecutionException e) {
            log.error("No such execution for id : {}", executionId);

            // TODO correct error
            JobErrorResource jobErrorResource = new JobErrorResource("executions.not.found", null, String.format("No such execution for id : %s", executionId));
            return jobErrorResource;
        } catch (JobExecutionNotRunningException e) {
            log.error("No such execution for id : {}", executionId);
            JobErrorResource jobErrorResource = new JobErrorResource("executions.not.running", null, String.format("No such execution for id : %s", executionId));
            return jobErrorResource;
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("No such execution for id : {}", executionId);
            JobErrorResource jobErrorResource = new JobErrorResource("executions.not.abandonable", null, String.format("No such execution for id : %s", executionId));
            return jobErrorResource;
        }
    }
}
