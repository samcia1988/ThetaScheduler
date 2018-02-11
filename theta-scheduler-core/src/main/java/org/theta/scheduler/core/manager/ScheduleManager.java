package org.theta.scheduler.core.manager;

import org.theta.scheduler.core.job.ThetaJob;

/**
 * Schedule manager interface.
 * 
 * @author ranger
 *
 */
public interface ScheduleManager {

	public boolean start();

	public boolean stop();

	public int addJob(ThetaJob job);

	public int runJob(String jobKey);

	public int removeJob(String jobKey);

	public int updateJob(ThetaJob job);

}
