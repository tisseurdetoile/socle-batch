package net.tisseurdetoile.batch.socle.api.job;

import lombok.Getter;
import lombok.Setter;
import net.tisseurdetoile.batch.socle.api.execution.ExecutionResource;

import java.util.List;

@Getter
@Setter
public class JobResourceDetailExecutions extends JobResource {

    private List<ExecutionResource> executions;

    public JobResourceDetailExecutions(String name, int executionCount) {
        super(name, executionCount);
    }

    public JobResourceDetailExecutions(String name, int executionCount, boolean launchable) {
        super(name, executionCount, launchable);
    }

    public JobResourceDetailExecutions(String name, int executionCount, boolean launchable, boolean incrementable) {
        super(name, executionCount, launchable, incrementable);
    }

    public JobResourceDetailExecutions(String name, int executionCount, Long jobInstanceId, boolean launchable, boolean incrementable) {
        super(name, executionCount, jobInstanceId, launchable, incrementable);
    }

    public JobResourceDetailExecutions (JobResource jobResource) {
        super(jobResource.getName(), jobResource.getExecutionCount(), jobResource.getJobInstanceId(), jobResource.isLaunchable(), jobResource.isIncrementable());
    }
}
