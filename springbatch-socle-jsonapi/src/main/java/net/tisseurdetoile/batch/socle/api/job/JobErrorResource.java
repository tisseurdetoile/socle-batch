package net.tisseurdetoile.batch.socle.api.job;

import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.util.HashSet;
import java.util.Set;

@Getter
public class JobErrorResource extends ResourceSupport {
    private String error;

    private JobResource job;

    private String description;

    private Set<Exception> nestedExceptions;

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
