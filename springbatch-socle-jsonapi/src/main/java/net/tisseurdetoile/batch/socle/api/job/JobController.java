package net.tisseurdetoile.batch.socle.api.job;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import net.tisseurdetoile.batch.socle.api.execution.ExecutionController;
import net.tisseurdetoile.batch.socle.api.execution.ExecutionResource;
import net.tisseurdetoile.batch.socle.api.job.exception.*;
import net.tisseurdetoile.batch.socle.api.jobexplorer.JobExplorerService;
import net.tisseurdetoile.batch.socle.api.support.JobParametersExtractor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@Log4j2
@RequestMapping(value = "/jobs", produces = "application/hal+json")
@EnableBatchProcessing
public class JobController {

    private final JobService jobService;
    private final JobExplorerService jobExplorerService;

    private final TimeZone timeZone = TimeZone.getDefault();
    private final JobParametersExtractor jobParametersExtractor = new JobParametersExtractor();

    public JobController(JobService jobService, JobExplorerService jobExplorerService) {
        this.jobService = jobService;
        this.jobExplorerService = jobExplorerService;
    }

    @GetMapping(value = "/name")
    public List<String> jobName() {
        return jobService.jobsName();
    }


    @GetMapping(value = "/runned")
    public List<String> allRunned() {

        List<String> allJobs = jobExplorerService.getJobName();
        log.debug("jobExplorerService.getJobName() = {}",allJobs.toString());

        return allJobs;
    }

    ExecutionResource getExectionResource (JobExecution jobExecution) {
        ExecutionResource executionResource = new ExecutionResource(jobExecution, timeZone);
        executionResource.add(linkTo(ExecutionController.class).slash(String.format(END_EXTENTION, jobExecution.getId())).withSelfRel());

        return executionResource;
    }

    @Operation(summary = "Display Job Information",
    responses = { @ApiResponse(
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = JobResourceDetailExecutions.class)))
    })

    @GetMapping("/{jobName}.json")
    public JobResourceDetailExecutions getJob(@PathVariable String jobName) {
        JobResourceDetailExecutions jobResourceDetailExecutions;

        try {
            Job job = jobService.getJob(jobName);
            jobResourceDetailExecutions = new JobResourceDetailExecutions(job.getName(),
                     jobExplorerService.getJobInstanceCount(jobName),
                     null,
                     jobService.isLaunchable(jobName),
                     jobService.isIncrementable(jobName));

            List<ExecutionResource> executions;
            executions = jobService.getJobExecutionForJobName(jobName)
                    .stream()
                    .map(this::getExectionResource)
                    .collect(Collectors.toList());

            jobResourceDetailExecutions.setExecutions(executions);

            jobResourceDetailExecutions.add(linkTo(JobController.class).slash(String.format(END_EXTENTION, jobName)).withSelfRel());

             return  jobResourceDetailExecutions;

        } catch (NoSuchJobException e) {
            log.error("No such job for name : {}", jobName);
            throw new JobNotFoundException(jobName);
        }
    }

    @Operation(summary = "Launch Job")
    @PostMapping("/{jobName}.json")
    public ExecutionResource postJob(@PathVariable String jobName,
                                   @RequestParam(required = false) String jobParameters)  {
        log.debug("url parameters = {}", jobParameters);

        JobParameters urlJobParameters = jobParametersExtractor.fromString(jobParameters);
        JobExecution jobExecution;

        try {
            jobExecution = jobService.launch(jobName, urlJobParameters);
        } catch (NoSuchJobException e) {
            log.error("No such job for name : {}", jobName);
            throw new JobNotFoundException(jobName);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("A job with this name : {} and parameters already completed successfully.", jobName);
            throw new JobAlreadyComplete(jobName);
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("A job with this name {} and parameters is already running.", jobName);
            throw new JobAlreadyRunning(jobName);
        } catch (JobParametersInvalidException e) {
            log.error("The job parameters for {} are invalid according to the configuration.", jobName);
            throw new JobInvalidParameters(jobName);
        } catch (JobRestartException e) {
            log.error("The job {} was not able to restart.", jobName);
            throw new JobCouldNotRestart(jobName);
        }

        ExecutionResource jobExecutionResource = new ExecutionResource(jobExecution, this.timeZone);
        jobExecutionResource.add(linkTo(ExecutionController.class).slash(String.format(END_EXTENTION, jobExecution.getId())).withSelfRel());
        jobExecutionResource.add(linkTo(ExecutionController.class).slash(String.format(END_EXTENTION, jobExecution.getId())).withRel("execution"));

        return jobExecutionResource;
    }

    @Operation(summary = "Get all Spring Batch jobs")
    @GetMapping
    public List<JobResource> all () {

        return jobService.jobsName().stream().map(this::getJobResource).collect(Collectors.toList());
    }

    private JobResource getJobResource(String jobName) {
        JobResource jobResource = new JobResource(jobName,
                jobExplorerService.getJobInstanceCount(jobName),
                null,
                jobService.isLaunchable(jobName),
                jobService.isIncrementable(jobName));

        jobResource.add(linkTo(JobController.class).slash(String.format(END_EXTENTION, jobName)).withSelfRel());

        return jobResource;
    }

    private static final String END_EXTENTION = "%s.json";
}
