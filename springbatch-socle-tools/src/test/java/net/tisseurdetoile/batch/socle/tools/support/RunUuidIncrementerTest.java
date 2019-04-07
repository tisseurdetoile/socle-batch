package net.tisseurdetoile.batch.socle.tools.support;

import net.tisseurdetoile.batch.socle.tools.support.RunUuidIncrementer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.JobParameters;


public class RunUuidIncrementerTest {


    @Test
    public void valid_uuid_parameter() {
        RunUuidIncrementer runUuidIncrementer = new RunUuidIncrementer();

        JobParameters parameters = new JobParameters();

        JobParameters updatedParameters = runUuidIncrementer.getNext(parameters);

        String uuid = updatedParameters.getString("run.uuid");

        Assert.assertTrue(uuid.matches(regExUuidPattern));
    }

    public static final String regExUuidPattern = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";

}