package net.tisseurdetoile.batch.socle.api.job;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
public class JobErrorResource extends RepresentationModel<JobErrorResource> {
    private final String error;

    private final JobResource job;

    private final String description;

    private final Set<Exception> nestedExceptions;

    public JobErrorResource(String error, JobResource job, String description) {
        this.error = error;
        this.job = job;
        this.description = description;
        this.nestedExceptions = new HashSet<>();
    }

    public void addExceptions(Exception exceptions) {
            this.nestedExceptions.add(exceptions);
    }
}
