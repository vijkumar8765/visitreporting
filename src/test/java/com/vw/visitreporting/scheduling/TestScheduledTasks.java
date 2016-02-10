package com.vw.visitreporting.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * Defines some scheduled tasks for testing purposes.
 * NOTE: scheduling is not enabled in the test Spring Application Context
 */
@Component
public class TestScheduledTasks {

	private volatile int scheduledTaskMethod_1_InvokationCount = 0;	//NOPMD
	private volatile int scheduledTaskMethod_2_InvokationCount = 0;	//NOPMD
	
	/**
	 * Define a test scheduled task, annotated with the RunOnce and Scheduled annotation.
	 */
	@RunOnce(taskName="scheduledTaskMethod1")
	@Scheduled(cron="0 25 * * * *")				//invalid cron schedule: daily at 25:00
	public synchronized void scheduledTaskMethod1() {	//NOPMD
				
		//count the number of time this method has been called
		scheduledTaskMethod_1_InvokationCount++;
	}
	

	/**
	 * Define a test task, annotated with the RunOnce annotation only.
	 */
	@RunOnce(taskName="scheduledTaskMethod2")
	public synchronized void scheduledTaskMethod2() {	//NOPMD
		
		//count the number of time this method has been called
		scheduledTaskMethod_2_InvokationCount++;
	}

	
	/**
	 * Define a test scheduled task that throws an Exception
	 */
	@RunOnce(taskName="scheduledTaskMethod3")
	@Scheduled(cron="0 25 * * * *")				//invalid cron schedule: daily at 25:00
	public synchronized void scheduledTaskMethod3(String errMsg) {	//NOPMD
		
		throw new RuntimeException("Error in scheduledTaskMethod3(): "+errMsg);
	}	
	

	public int getScheduledTaskMethod_1_InvokationCount() {	//NOPMD
		return scheduledTaskMethod_1_InvokationCount;
	}
	public void setScheduledTaskMethod_1_InvokationCount(int scheduledTaskMethod_1_InvokationCount) {	//NOPMD
		this.scheduledTaskMethod_1_InvokationCount = scheduledTaskMethod_1_InvokationCount;
	}
	public int getScheduledTaskMethod_2_InvokationCount() {	//NOPMD
		return scheduledTaskMethod_2_InvokationCount;
	}
	public void setScheduledTaskMethod_2_InvokationCount(int scheduledTaskMethod_2_InvokationCount) {	//NOPMD
		this.scheduledTaskMethod_2_InvokationCount = scheduledTaskMethod_2_InvokationCount;
	}
}
