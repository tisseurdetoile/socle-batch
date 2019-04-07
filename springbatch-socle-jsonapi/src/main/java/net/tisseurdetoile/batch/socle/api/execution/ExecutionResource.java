package net.tisseurdetoile.batch.socle.api.execution;

import lombok.Getter;
import net.tisseurdetoile.batch.socle.api.step.StepExecutionInfoResource;
import net.tisseurdetoile.batch.socle.api.support.JobParametersExtractor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.hateoas.ResourceSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Getter
public class ExecutionResource extends ResourceSupport {

    private Long executionId;

    private int stepExecutionCount;

    private Long jobId;

    private String jobName;

    private String startDate = "";

    private String startTime = "";

    private String duration = "";

    private BatchStatus status;

    private ExitStatus exitStatus;

    private String jobParameters;

    private Collection<StepExecutionInfoResource> stepExecutions;

    private boolean restartable = false;

    private boolean abandonable = false;

    private String exitDescription;

    private boolean stoppable = false;

    private TimeZone timeZone;

    public ExecutionResource(JobExecution jobExecution, TimeZone timeZone) {

        this.executionId = jobExecution.getId();
        this.jobId = jobExecution.getJobId();
        this.stepExecutionCount = jobExecution.getStepExecutions().size();
        this.jobParameters = new JobParametersExtractor().fromJobParameters(jobExecution.getJobParameters());
        this.status = jobExecution.getStatus();
        this.exitStatus = jobExecution.getExitStatus();

        this.timeZone = timeZone;

        if (jobExecution.getExitStatus() != null) {
            this.exitDescription = jobExecution.getExitStatus().getExitDescription();
        }

        JobInstance jobInstance = jobExecution.getJobInstance();
        if (jobInstance != null) {
            this.jobName = jobInstance.getJobName();
            BatchStatus status = jobExecution.getStatus();
            this.restartable = status.isGreaterThan(BatchStatus.STOPPING) && status.isLessThan(BatchStatus.ABANDONED);
            this.abandonable = status.isGreaterThan(BatchStatus.STARTED) && status!=BatchStatus.ABANDONED;
            this.stoppable  = status.isLessThan(BatchStatus.STOPPING);
        }
        else {
            this.jobName = "?";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat durationFormat = new SimpleDateFormat("HH:mm:ss");

        this.stepExecutions = new ArrayList<>();

        this.stepExecutions = jobExecution.getStepExecutions().stream()
                .map(stepExecution -> new StepExecutionInfoResource(stepExecution, this.timeZone))
                .collect(Collectors.toSet());

        // Duration is always in GMT
        durationFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        // The others can be localized
        timeFormat.setTimeZone(timeZone);
        dateFormat.setTimeZone(timeZone);
        if (jobExecution.getStartTime() != null) {
            this.startDate = dateFormat.format(jobExecution.getStartTime());
            this.startTime = timeFormat.format(jobExecution.getStartTime());
            Date endTime = jobExecution.getEndTime() != null ? jobExecution.getEndTime() : new Date();
            this.duration = durationFormat.format(new Date(endTime.getTime() - jobExecution.getStartTime().getTime()));
        }
    }
}
