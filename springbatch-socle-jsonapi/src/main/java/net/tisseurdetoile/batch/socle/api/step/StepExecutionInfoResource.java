package net.tisseurdetoile.batch.socle.api.step;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Getter
@EqualsAndHashCode(callSuper = true)
public class StepExecutionInfoResource extends RepresentationModel<StepExecutionInfoResource> {
    private final Long executionId;

    private final Long jobExecutionId;

    private  String stepType;

    private final String stepName;

    private final BatchStatus status;

    private final int readCount;

    private final int writeCount;

    private final int commitCount;

    private final int rollbackCount;

    private final int readSkipCount;

    private final int processSkipCount;

    private final int writeSkipCount;

    private final String startTime;

    private final String endTime;

    private final String lastUpdated;

    private final ExitStatus exitStatus;

    private final boolean terminateOnly;

    private final int filterCount;

    private final List<Throwable> failureExceptions;

    private final Map<String, Object> executionContext;

    private final Integer version;

    private final TimeZone timeZone;

    public StepExecutionInfoResource(StepExecution stepExecution, TimeZone timeZone) {
        Assert.notNull(stepExecution, "stepExecution must not be null.");

        if(timeZone != null){
            this.timeZone = timeZone;
        }
        else {
            this.timeZone = TimeZone.getTimeZone("UTC");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat durationFormat = new SimpleDateFormat("HH:mm:ss");

        // Duration is always in GMT
        durationFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        // The others can be localized
        dateFormat.setTimeZone(timeZone);

        this.jobExecutionId = stepExecution.getJobExecutionId();
        if(stepExecution.getExecutionContext().containsKey(Step.STEP_TYPE_KEY)) {
            this.stepType = (String) stepExecution.getExecutionContext().get(Step.STEP_TYPE_KEY);
        }

        this.executionId = stepExecution.getId();
        this.stepName = stepExecution.getStepName();
        this.status = stepExecution.getStatus();
        this.readCount = stepExecution.getReadCount();
        this.writeCount = stepExecution.getWriteCount();
        this.commitCount = stepExecution.getCommitCount();
        this.rollbackCount = stepExecution.getRollbackCount();
        this.readSkipCount = stepExecution.getReadSkipCount();
        this.processSkipCount = stepExecution.getProcessSkipCount();
        this.writeSkipCount = stepExecution.getWriteSkipCount();
        this.startTime = dateFormat.format(stepExecution.getStartTime());
        this.lastUpdated = dateFormat.format(stepExecution.getLastUpdated());

        if(stepExecution.getEndTime() != null) {
            this.endTime = dateFormat.format(stepExecution.getEndTime());
        }
        else {
            this.endTime = "N/A";
        }

        HashMap<String, Object> executionContextValues = new HashMap<>();

        for (Map.Entry<String, Object> stringObjectEntry : stepExecution.getExecutionContext().entrySet()) {
            executionContextValues.put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }

        this.executionContext = executionContextValues;
        this.exitStatus = stepExecution.getExitStatus();
        this.terminateOnly = stepExecution.isTerminateOnly();
        this.filterCount = stepExecution.getFilterCount();
        this.failureExceptions = stepExecution.getFailureExceptions();
        this.version = stepExecution.getVersion();
    }
}
