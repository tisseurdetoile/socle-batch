package net.tisseurdetoile.batch.socle.api.execution;

import org.springframework.batch.core.JobExecution;

import java.util.Comparator;

public class JobExecutionByDateAssending implements Comparator<JobExecution> {
    @Override
    public int compare(JobExecution j1, JobExecution j2) {
        int result;
        if (j1.getEndTime() != null && j2.getEndTime() != null)
            result = j1.getEndTime().compareTo(j2.getEndTime());
        else
            result = j1.getStartTime().compareTo(j2.getStartTime());
        return result * -1;
    }
}
