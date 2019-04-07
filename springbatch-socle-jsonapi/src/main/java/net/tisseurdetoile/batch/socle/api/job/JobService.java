package net.tisseurdetoile.batch.socle.api.job;

import lombok.extern.log4j.Log4j2;
import net.tisseurdetoile.batch.socle.api.execution.JobExecutionByDateAssending;
import net.tisseurdetoile.batch.socle.api.jobexplorer.JobExplorerService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class JobService implements DisposableBean {
    /***
     * TODO utliser le active execution.
     * @param jobRegistry
     * @param jobLocator
     * Source :
     * https://github.com/spring-projects/spring-batch-admin/blob/9e3ad8bff99b8fad8da62426aa7d2959eb841bcf/spring-batch-admin-manager/src/main/java/org/springframework/batch/admin/web/JobController.java
     * https://github.com/spring-attic/spring-batch-admin/blob/9e3ad8bff99b8fad8da62426aa7d2959eb841bcf/spring-batch-admin-manager/src/main/java/org/springframework/batch/admin/service/SimpleJobService.java
     */

    private JobRegistry jobRegistry;

    private ListableJobLocator jobLocator;

    private JobRepository jobRepository;

    private JobLauncher jobLauncher;

    private JobExplorerService jobExplorerService;

    // 60 seconds
    private static final int DEFAULT_SHUTDOWN_TIMEOUT = 60 * 1000;

    private int shutdownTimeout = DEFAULT_SHUTDOWN_TIMEOUT;

    @Autowired
    JobService(JobRegistry jobRegistry, ListableJobLocator jobLocator, JobRepository jobRepository, JobLauncher jobLauncher, JobExplorerService jobExplorerService) {
        this.jobRegistry = jobRegistry;
        this.jobLocator = jobLocator;
        this.jobRepository = jobRepository;
        this.jobLauncher = jobLauncher;
        this.jobExplorerService = jobExplorerService;
    }

    private Collection<JobExecution> activeExecutions = Collections.synchronizedList(new ArrayList<JobExecution>());

    public List<String> jobsName() {
        return jobRegistry.getJobNames().stream().collect(Collectors.toList());
    }

    public  JobExecution getJobExecution(long jobExecutionId) throws NoSuchJobExecutionException {

        log.debug("cachedJobExecution {}", activeExecutions.size());

        for (JobExecution cachedJobExecution : activeExecutions) {
            if (jobExecutionId == cachedJobExecution.getId()) {
                log.debug("getJobExecution cache hit");
                return cachedJobExecution;
            }
        }

        return jobExplorerService.jobExecution(jobExecutionId);
    }

    public JobExecution stop(Long jobExecutionId) throws NoSuchJobExecutionException, JobExecutionNotRunningException {

        JobExecution jobExecution = getJobExecution(jobExecutionId);
        if (!jobExecution.isRunning()) {
            throw new JobExecutionNotRunningException("JobExecution is not running and therefore cannot be stopped");
        }

        log.info("Stopping job execution: {} ", jobExecution);

        jobExecution.stop();
        jobRepository.update(jobExecution);

        return jobExecution;
    }

    public JobExecution abandon(Long jobExecutionId) throws NoSuchJobExecutionException, JobExecutionAlreadyRunningException {

        JobExecution jobExecution = getJobExecution(jobExecutionId);
        if (jobExecution.getStatus().isLessThan(BatchStatus.STOPPING)) {
            throw new JobExecutionAlreadyRunningException("JobExecution is running or complete and therefore cannot be aborted");
        }

        log.info("Stopping/abandonning job execution: {} ", jobExecution);

        jobExecution.stop();
        jobExecution.setEndTime(new Date());
        jobExecution.upgradeStatus(BatchStatus.ABANDONED);
        jobRepository.update(jobExecution);

        return jobExecution;
    }

    public Set<JobExecution> getJobExecutionForJobName(String jobName, boolean cache_only) {
        Set<JobExecution> res = new TreeSet<>(new JobExecutionByDateAssending());

        this.activeExecutions.stream()
                .filter(jobExecution -> jobName.equalsIgnoreCase(jobExecution.getJobInstance().getJobName()))
                .limit(5)
                .forEach(res::add);

        return res;
    }

    /**
     * Le job est dans liste des job que l'on peu lancer
     * @param jobName
     * @return
     */
    public boolean isLaunchable (String jobName) {
        return this.jobsName().contains(jobName);
    }

    public boolean isRestartable (String jobName) {
        try {
            return jobLocator.getJobNames().contains(jobName) && jobLocator.getJob(jobName).isRestartable();
        } catch (NoSuchJobException e) {
            throw new IllegalStateException("Unexpected non-existent job: " + jobName);
        }
    }

    public boolean isIncrementable(String jobName) {
        try {
            return jobLocator.getJobNames().contains(jobName) && jobLocator.getJob(jobName).getJobParametersIncrementer() != null;
        } catch (NoSuchJobException e) {
            throw new IllegalStateException("Unexpected non-existent job: " + jobName);
        }

    }

    public int countJobs() {
        Collection<String> names = new HashSet<String>(jobLocator.getJobNames());
        names.addAll(this.jobsName());
        return names.size();
    }

    public Job getJob (String jobName) throws NoSuchJobException {
        if(jobLocator.getJobNames().contains(jobName)) {
            return jobLocator.getJob(jobName);
        } else {
            throw new NoSuchJobException(String.format("Unable to find job %s", String.valueOf(jobName)));
        }
    }

    public JobExecution launch(String jobName, JobParameters jobParameters) throws NoSuchJobException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        JobExecution jobExecution;

        if(jobLocator.getJobNames().contains(jobName)) {
            Job job = jobLocator.getJob(jobName);

            JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);
            boolean restart = false;

            if (lastJobExecution != null) {
                BatchStatus status = lastJobExecution.getStatus();
                if (status.isUnsuccessful() && status!=BatchStatus.ABANDONED) {
                    restart = true;
                }
            }

            if (job.getJobParametersIncrementer() != null && !restart) {
                jobParameters = job.getJobParametersIncrementer().getNext(jobParameters);
            }

            jobExecution = jobLauncher.run(job, jobParameters);

            /**
             * A starting job has no StartDate so isRunning is false :(. So we cache StartingJobs
             */
            if (jobExecution.isRunning() || jobExecution.getStatus() == BatchStatus.STARTING) {
                activeExecutions.add(jobExecution);
            }
        } else {
            throw new NoSuchJobException(String.format("Unable to find job %s to launch", String.valueOf(jobName)));
        }

        return jobExecution;
    }


    /**
     * Stop all the active jobs and wait for them (up to a time out) to finish
     * processing.
     *  cf https://github.com/spring-projects/spring-batch-admin/blob/9e3ad8bff99b8fad8da62426aa7d2959eb841bcf/spring-batch-admin-manager/src/main/java/org/springframework/batch/admin/service/SimpleJobService.java
     */
    @Override
    public void destroy() throws Exception {
        log.debug("destroy signal recivied");


        Exception firstException = null;

        for (JobExecution jobExecution : activeExecutions) {
            try {
                if (jobExecution.isRunning()) {
                    stop(jobExecution.getId());
                }
            }
            catch (JobExecutionNotRunningException e) {
                log.info("JobExecution is not running so it cannot be stopped");
            }
            catch (Exception e) {
                log.error("Unexpected exception stopping JobExecution", e);
                if (firstException == null) {
                    firstException = e;
                }
            }
        }

        int count = 0;
        int maxCount = (shutdownTimeout + 1000) / 1000;
        while (!activeExecutions.isEmpty() && ++count < maxCount) {
            log.error("Waiting for {}  active executions to complete", activeExecutions.size());
            removeInactiveExecutions();
            Thread.sleep(1000L);
        }

        if (firstException != null) {
            throw firstException;
        }
    }

    /**
     * Check all the active executions and see if they are still actually
     * running. Remove the ones that have completed.
     */
    @Scheduled(fixedDelay = 60000)
    public void removeInactiveExecutions() {

        for (Iterator<JobExecution> iterator = activeExecutions.iterator(); iterator.hasNext();) {
            JobExecution jobExecution = iterator.next();

            try {
                jobExecution = jobExplorerService.jobExecution(jobExecution.getId());
            } catch (NoSuchJobExecutionException e) {
                log.error("Unexpected exception loading JobExecution", e);
            }

            if (!jobExecution.isRunning()) {
                log.debug("Flush Execution cache - remove JobExecution >%s<", jobExecution);
                iterator.remove();
            }
        }

    }
}
