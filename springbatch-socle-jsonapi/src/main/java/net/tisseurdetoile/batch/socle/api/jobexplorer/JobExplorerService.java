package net.tisseurdetoile.batch.socle.api.jobexplorer;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
/**
 * Liste les Job qui on tournée
 */
public class JobExplorerService {

    private JobExplorer jobExplorer;

    @Autowired
    public JobExplorerService(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }


    public JobExecution jobExecution(long executionId) throws NoSuchJobExecutionException {
        JobExecution jobExecution = jobExplorer.getJobExecution(executionId);

        if (jobExecution == null) {
            throw new NoSuchJobExecutionException("Could not find job execution with ID " + executionId);
        }


        return jobExecution;
    }

    public List<String> getJobName() {
        return jobExplorer.getJobNames();
    }

    public int getJobInstanceCount(String jobName) {
        try {
            return jobExplorer.getJobInstanceCount(jobName);
        } catch (NoSuchJobException e) {
            log.error(String.format("Unexpected non-existent job: %s", jobName));
            return 0;
        }
    }

    public List<JobInstance> lastTen (String jobName) {
        return jobExplorer.findJobInstancesByJobName(jobName, 0, 10);
    }

    public List<JobExecution>  LastExecution (String jobName) {

        List<JobExecution> jobExecutions = new ArrayList<>();

        for (JobInstance jobInstance: lastTen(jobName)){
            jobExecutions.addAll(jobExplorer.getJobExecutions(jobInstance));
        }

        return jobExecutions;
    }
}
