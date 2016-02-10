package com.vw.visitreporting.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.vw.visitreporting.logging.LogConfigManager;


/**
 * Encapsulates the scheduling of all scheduled tasks in this system.
 */
@Component
public class ScheduledTasks {

	@Autowired
	private LogConfigManager logConfigManager;
	

	/**
	 * Scheduled task that checks for updates to the log configuration.
	 * Task should run every 3 seconds.
	 */
	@Scheduled(fixedDelay=3000)
	public void updateLogConfig() {
		
		this.logConfigManager.updateLogConfig();
	}
	

	/**
	 * Scheduled task that deletes draft documents from the system after a configurable
	 * interval when they are deemed to have expired.
	 * Task should run every night at 1:00am.
	 */
	@RunOnce(taskName="deleteExpiredDraftDocs")
	@Scheduled(cron="0 0 1 * * *")
	public void deleteExpiredDraftDocs() {
		
		//run method in the service layer
	}
	

	/**
	 * Scheduled task that sets the status of KPIs to expired if their effective
	 * lifetime has elapsed.
	 * Task should run every night at 1:30am.
	 */
	@RunOnce(taskName="expireKPIs")
	@Scheduled(cron="0 30 1 * * *")
	public void expireKPIs() {
		
		//run method in the service layer
	}

	
	/**
	 * Scheduled task that archives historical data from the database.
	 * Task should run every night at 2:00am.
	 */
	@RunOnce(taskName="archiveHistoricalData")
	@Scheduled(cron="0 0 2 * * *")
	public void archiveHistoricalData() {
		
		//run method in the service layer
	}
}
