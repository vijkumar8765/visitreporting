package com.vw.visitreporting.scheduling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static com.vw.visitreporting.CustomMatchers.containsText;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import com.vw.visitreporting.AbstractIntegrationTest;
import com.vw.visitreporting.logging.LogConfigManagerTest;


/**
 * Unit test the AppInfo class
 */
public class RunOnceAspectIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private TestScheduledTasks testScheduledTasks;

	@Autowired
	private SessionFactory sessionFactory;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
	
	/**
	 * Test that annotating a method with RunOnce will ensure that is only run on one machine in a cluster.
	 */
    @Test
	public void testRunOnceAnnotationEnsuresOnlyRunOnOneInstance() {
		
		//reset invocation counter for method scheduledTaskMethod1()
		testScheduledTasks.setScheduledTaskMethod_1_InvokationCount(0);

		//start multiple threads to run the method scheduledTaskMethod1()
		runTaskOnMultipleThreads(new Runnable() {
			public void run() {
				testScheduledTasks.scheduledTaskMethod1();
			}
		});
		
		//check that it only ran once
 		assertThat(testScheduledTasks.getScheduledTaskMethod_1_InvokationCount(), is(equalTo(1)));
	}
	
	/**
	 * Test that RunOnce annotation has no effect unless Scheduled annotation is also supplied
	 */
	@Test
	public void testThatNoScheduledAnnotationHasNoEffect() {
		
		//reset invocation counter for method scheduledTaskMethod2()
		testScheduledTasks.setScheduledTaskMethod_2_InvokationCount(0);

		//start multiple threads to run the method scheduledTaskMethod1()
		int threadCount = runTaskOnMultipleThreads(new Runnable() {
			public void run() {
				testScheduledTasks.scheduledTaskMethod2();
			}
		});
		
		//check that it only ran once
 		assertThat(testScheduledTasks.getScheduledTaskMethod_2_InvokationCount(), is(equalTo(threadCount)));
	}
	
	
	/**
	 * Test that if a scheduled task throws an Exception, the error is logged but
	 * does not halt execution of the system.
	 */
	@Test
	public void testThatTaskErrorsAreNotFatalAndAreLogged() throws IOException {
		
		//configure logging to known log file location and error level
		File logFile = LogConfigManagerTest.setLogLevelTo("error", tempFolder);
		
		//run a task that will through an exception
		String errMsg = "--test error message "+new Date().getTime()+"--";
		testScheduledTasks.scheduledTaskMethod3(errMsg);
		
		//check that an entry has been written to the log file
		assertThat(logFile,containsText(errMsg));
	}
	

	/**
	 * The RunOnceAspect code has to persist so rollback will not clear the DB.
	 * This method clears the TaskExecution table in the database after each test.
	 */
	@After
	public void clearTaskExecutionTable() throws Exception {	//NOPMD
 		JdbcTemplate deleteStmt = new JdbcTemplate(SessionFactoryUtils.getDataSource(sessionFactory));
 		deleteStmt.update("DELETE from TASKEXECUTION");
	}
	
	/**
	 * Runs a task multiple times on different threads.
	 * @return the number of times the task was executed
	 */
	private int runTaskOnMultipleThreads(Runnable task) {

		int threadCount = 3;
		long executionTimeoutInMillis = 3000;	//five seconds
		
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(threadCount);
		for(int i=0; i<threadCount; i++) {
			executor.execute(task);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(executionTimeoutInMillis, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		}
		return threadCount;
	}
}
