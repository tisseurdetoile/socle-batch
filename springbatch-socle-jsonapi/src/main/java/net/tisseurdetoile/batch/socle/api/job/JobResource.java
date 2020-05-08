package net.tisseurdetoile.batch.socle.api.job;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

@Getter
@EqualsAndHashCode(callSuper = true)
public class JobResource extends ResourceSupport {

    private final String name;

    private final int executionCount;

    private final boolean launchable;

    private final boolean incrementable;

    private final Long jobInstanceId;

    public JobResource(String name, int executionCount) {
        this(name, executionCount, false);
    }

    public JobResource(String name, int executionCount, boolean launchable) {
        this(name, executionCount, null, launchable, false);
    }

    public JobResource(String name, int executionCount, boolean launchable, boolean incrementable) {
        this(name, executionCount, null, launchable, incrementable);
    }

    public JobResource(String name, int executionCount, Long jobInstanceId, boolean launchable, boolean incrementable) {
        super();
        this.name = name;
        this.executionCount = executionCount;
        this.jobInstanceId = jobInstanceId;
        this.launchable = launchable;
        this.incrementable = incrementable;
    }

    @Override public String toString() {
        return String.format("%s", name);
    }
}