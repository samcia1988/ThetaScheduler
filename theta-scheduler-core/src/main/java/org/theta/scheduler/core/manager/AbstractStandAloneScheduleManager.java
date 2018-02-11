package org.theta.scheduler.core.manager;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.theta.scheduler.core.job.ThetaJob;

/**
 * 
 * @author ranger
 *
 */
public abstract class AbstractStandAloneScheduleManager implements ScheduleManager {

	private static final Logger logger = LoggerFactory.getLogger(AbstractStandAloneScheduleManager.class);

	private Scheduler scheduler = null;

	public synchronized boolean start() {
		SchedulerFactory factory = new StdSchedulerFactory();
		try {
			this.scheduler = factory.getScheduler();
			if (this.scheduler.isStarted()) {
				logger.warn("This scheduler has been started,notice your command orders.");
				return false;
			}
			this.scheduler.start();
		} catch (SchedulerException e) {
			logger.error("Exception happened while creating scheduler.", e);
			return false;
		}
		return true;
	}

	public synchronized boolean stop() {
		try {
			if (!this.scheduler.isShutdown())
				this.scheduler.shutdown();
			else {
				logger.warn("This scheduler has been shut down,notice your command orders.");
			}
		} catch (SchedulerException e) {
			logger.error("Exception happened while shutting down scheduler.", e);
			return false;
		}
		return true;
	}

	public synchronized int addJob(ThetaJob job) {
		try {
			this.scheduler.scheduleJob(job.getJob(), job.getTrigger());
		} catch (SchedulerException e) {
			logger.error("Adding job failed.", e);
			return 0;
		}
		return 1;
	};

	public synchronized int removeJob(String jobKey) {
		try {
			this.scheduler.deleteJob(JobKey.jobKey(jobKey));
		} catch (SchedulerException e) {
			logger.error("Removing job failed.", e);
			return 0;
		}
		return 1;
	};

	public int runJob(String jobKey) {
		try {
			if (this.scheduler.checkExists(JobKey.jobKey(jobKey))) {
				this.scheduler.triggerJob(JobKey.jobKey(jobKey));
			} else {
				logger.warn("No such job key:" + jobKey);
				return 0;
			}
		} catch (SchedulerException e) {
			logger.error("Unkown exception.", e);
		}

		return 1;
	}

	public synchronized int updateJob(ThetaJob job) {
		try {
			this.scheduler.deleteJob(job.getJob().getKey());
			this.scheduler.scheduleJob(job.getJob(), job.getTrigger());
		} catch (SchedulerException e) {
			try {
				boolean deleted = !this.scheduler.checkExists(job.getJob().getKey());
				String delMsg = deleted ? "has" : "hasn't";
				logger.error("Updating job failed.This job " + delMsg + " been deleted.", e);

				return 0;
			} catch (SchedulerException e1) {
				logger.error("Unkown exception while checking job existence.", e1);
			}
		}
		return 1;
	};

}
