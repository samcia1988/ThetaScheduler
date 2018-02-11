package org.theta.scheduler.core;

import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.TriggerBuilder;
import org.theta.scheduler.core.job.ThetaJob;
import org.theta.scheduler.core.manager.DefaultStandAloneScheduleManager;
import org.theta.scheduler.core.manager.ScheduleManager;

/**
 * 
 * @author ranger
 *
 */
public class ProcessTester {

	@Test
	public void testSchedulerManager() throws InterruptedException {
		ScheduleManager sm = new DefaultStandAloneScheduleManager();
		sm.start();
		JobDetail job = JobBuilder.newJob(TestJob.class).withIdentity("Key1").build();
		CronTrigger trigger = TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule("0/59 * * * * ? *")).build();
		ThetaJob tj = new ThetaJob(job, trigger) {
		};
		sm.addJob(tj);
		sm.runJob("Key1");
		Thread.sleep(5000l);
		sm.stop();
	}

}
