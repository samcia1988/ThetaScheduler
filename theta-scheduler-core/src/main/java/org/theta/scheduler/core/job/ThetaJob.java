package org.theta.scheduler.core.job;

import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 * Common job class.
 * 
 * @author ranger
 *
 */
public abstract class ThetaJob {

	private JobDetail job;
	private Trigger trigger;

	public ThetaJob() {
	}

	public ThetaJob(JobDetail job, Trigger trigger) {
		this.job = job;
		this.trigger = trigger;
	}

	public JobDetail getJob() {
		return this.job;
	}

	public Trigger getTrigger() {
		return this.trigger;
	}

}
