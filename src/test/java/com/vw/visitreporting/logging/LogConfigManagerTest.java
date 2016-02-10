package com.vw.visitreporting.logging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static com.vw.visitreporting.CustomMatchers.containsText;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Unit test the LogConfigManager class
 */
public class LogConfigManagerTest {

    private static final Logger LOG = LoggerFactory.getLogger(LogConfigManagerTest.class);

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    
	/**
	 * The logging configuration file will be stored in a location outside of the WAR that
	 * this system is deployed in so that it can be updated and dynamically reloaded.
	 * Check that this file gets created (using the default configuration from resources) if
	 * it doesn't already exist.
	 */
	@Test
	public void testThatLogFileGetsCreatedIfNotExists() throws IOException {
		
		LogConfigManager logManager = new LogConfigManager();
		
		//choose a location for the log configuration file to go and ensure it doesn't already exist
		File logConfigFileLocation = tempFolder.newFile("test_log4j.xml");
		if(logConfigFileLocation.exists()) {
			logConfigFileLocation.delete();
		}
		logConfigFileLocation.deleteOnExit();

		logManager.setSharedLogConfigFileLocation(logConfigFileLocation);

		//this method will be called at fixed time intervals in the production system
		logManager.updateLogConfig();
		
		//check the log file now exists and is not empty
		assertThat(logConfigFileLocation.exists(), is(true));
		assertThat(logConfigFileLocation.length(), is(greaterThan(0L)));

	}
	
	/**
	 * Perform the same test as testThatLogFileGetsCreatedIfNotExists() but in
	 * the scenario where the file location is not specified.
	 */
	@Test
	public void testThatLogFileGetsCreatedIfNotExistsEvenIfLocationNotSpecified() {	
		
		LogConfigManager logManager = new LogConfigManager();

		//test scenario where the file location is not specified
		logManager.setSharedLogConfigFileLocation(null);
		
		logManager.updateLogConfig();

		//check a location has been chosen and that the configuration file has been written there
		File logConfigFileLocation = logManager.getSharedLogConfigFileLocation();
		logConfigFileLocation.deleteOnExit();
		
		assertThat(logConfigFileLocation, is(notNullValue()));
		assertThat(logConfigFileLocation.exists(), is(true));
		assertThat(logConfigFileLocation.length(), is(greaterThan(0L)));

		logConfigFileLocation.delete();
	}
	
	/**
	 * Test that when the logging configuration file is modified the logging of the system
	 * gets re-configured to use the new configuration settings.
	 */
	@Test
	public void testThatLoggingIsReconfiguredWhenConfigFileModified() throws IOException {
		
		//modify log configuration file to WARN level
		File logConfigFile = tempFolder.newFile("test_log4j.xml");
		File logFile = tempFolder.newFile("test_log.txt");
		logConfigFile.deleteOnExit();
		logFile.deleteOnExit();

		LogConfigManager logManager = new LogConfigManager();
		logManager.setSharedLogConfigFileLocation(logConfigFile);

		LogConfigManagerTest.copyTestLogConfigFileWithParams(logConfigFile, logFile, "warn");
		logManager.updateLogConfig();
		
		//log an INFO message and check that it has not been written to the log file
		String errMsg1 = "--TEST ERR MESSAGE--" + new Date().getTime() + "--";
		
		LOG.info(errMsg1);
		
		assertThat(logFile,not(containsText(errMsg1)));
		
		
		//must sleep for >1 sec because Linux file timestamp precision is one second
		try {
			Thread.sleep(1500L);
		} catch(InterruptedException err) {
			fail("Sleep thread interupted");
		}
		
		
		//modify log configuration file to TRACE level
		LogConfigManagerTest.copyTestLogConfigFileWithParams(logConfigFile, logFile, "trace");
		logManager.updateLogConfig();
		
		//log an INFO message and check that it has not been written to the log file
		String errMsg2 = "--TEST ERR MESSAGE--" + new Date().getTime() + "--";
		
		LOG.info(errMsg2);
		
		assertThat(logFile,containsText(errMsg2));
	}
	
	
	/**
	 * Test that an invalid log configuration file will not stop the system.
	 */
	@Test
	public void testThatInvalidLogFileDoesNotStopSystem() throws IOException {
		
		LogConfigManager logManager = new LogConfigManager();
		
		//create invalid log configuration file
		File logConfigFileLocation = tempFolder.newFile("test_log4j.xml");
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(logConfigFileLocation));
			writer.println("Hello World");
			writer.close();
		} catch(Exception err) {
			fail("error creating log configuration file: "+err.getMessage());
		}
		logConfigFileLocation.deleteOnExit();
		
		//redirect console output temporarily as error messages will be output to console
		PrintStream prevErrOut = System.err;
		ByteArrayOutputStream outErrContent = new ByteArrayOutputStream();
		System.setErr(new PrintStream(outErrContent));
		
		//update log configuration with these settings
		logManager.setSharedLogConfigFileLocation(logConfigFileLocation);
		try {
			logManager.updateLogConfig();
			
			assertTrue("Log config updated successfully", true);	//NOPMD
			
		} catch(Exception err) {
			fail("invalid log configuration caused system failure");
			
		} finally {
			//restore console output
			System.err.flush();
			System.setErr(prevErrOut);
			outErrContent.close();
		}
	}
	
	
	/**
	 * Test that an invalid log configuration file location will not stop the system.
	 *
	@Test
	public void testInvalidLogConfigFileLocationdoesNotStopSystem() {

		File logConfigFileLocation = new File("////::Hello World");
		
		LogConfigManager logManager = new LogConfigManager();
		logManager.setSharedLogConfigFileLocation(logConfigFileLocation);
		try {
			logManager.updateLogConfig();
			
			assertTrue(true);
			
		} catch(Exception err) {
			fail("invalid log configuration file location caused system failure");
		}
	}
	*/
	
	/**
	 * Writes the log configuration file in the test resources (called log4j_test.xml) to a
	 * local location and replaces --FILENAME-- and --LEVEL-- tokens with the arguments specified.
	 * @param logConfigFile - the file location to save the log configuration to
	 * @param logFile - the location of the log file to write to
	 * @param logLevel - the level of logging, i.e. error/warn/info/debug/trace
	 */
	public static void copyTestLogConfigFileWithParams(File logConfigFile, File logFile, String logLevel) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(LogConfigManagerTest.class.getResourceAsStream("/log4j_test.xml")));
			PrintWriter writer = new PrintWriter(new FileOutputStream(logConfigFile));
			
			String line = null;
			while( (line = reader.readLine()) != null) {
				
				line = line.replace("--FILENAME--", logFile.getPath().replaceAll("\\\\", "/")).replace("--LEVEL--", logLevel);

				writer.println(line);
			}
			
			writer.close();
			reader.close();
				
		} catch(Exception err) {
			fail("Could not copy test log file configuration: "+err.getMessage());
		}
	}
	
	/**
	 * Utility method for other unit tests to allow quick configuration of logging.
	 * @param level - the desired level of logging
	 * @param folder - the folder that the log file should be written to
	 * @return the file details of the log file that logs will be written to 
	 */
	public static File setLogLevelTo(String level, TemporaryFolder folder) {
		try {
			File logConfigFile = folder.newFile("test_log4j.xml");
			File logFile = folder.newFile("test_log.txt");
			logConfigFile.deleteOnExit();
			logFile.deleteOnExit();
			
			LogConfigManagerTest.copyTestLogConfigFileWithParams(logConfigFile, logFile, level);
			
			LogConfigManager logManager = new LogConfigManager();
			logManager.setSharedLogConfigFileLocation(logConfigFile);
			logManager.updateLogConfig();
			
			return logFile;
			
		} catch(Exception err) {
			throw new RuntimeException("Could not re-configure logging: "+err.getMessage(), err);
		}
	}
}
