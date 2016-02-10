package com.vw.visitreporting.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vw.visitreporting.common.AppInfo;


/**
 * Manages the configuration of the system logger.
 * Re-configures the logger at runtime with an external configuration file if it has changed.
 */
public class LogConfigManager {

    private static final Logger LOG = LoggerFactory.getLogger(LogConfigManager.class);

    private static final String DEFAULT_LOG_CONFIG = "/log4j.xml";

    private File sharedLogConfigFileLocation;
    private long sharedLogConfigFileLastModified;


    /**
     * Constructor. Set last modified time of log configuration file to 0L so that
     * logger is re-configured when system starts.
     */
    public LogConfigManager() {
    	this.sharedLogConfigFileLocation = null;
    	this.sharedLogConfigFileLastModified = 0L;
    }

    /**
     * Gets the location of the Log4J XML configuration file for the logger.
     */
	public File getSharedLogConfigFileLocation() {
		return this.sharedLogConfigFileLocation;
	}

    /**
     * Sets the location of the Log4J XML configuration file for the logger.
     */
	public void setSharedLogConfigFileLocation(File sharedLogConfigFileLocation) {
		this.sharedLogConfigFileLocation = sharedLogConfigFileLocation;
	}


	/**
	 * To be called at timed intervals.
	 * Check if log configuration needs to be updated and update if necessary.
	 */
	public void updateLogConfig() {
		try {

			//if location of configuration has not been specified then copy default config file to a temporary location
			if(this.sharedLogConfigFileLocation == null) {
				this.sharedLogConfigFileLocation = new File(System.getProperty("java.io.tmpDir"), AppInfo.getArtifactName()+"_log4j.xml");
			}

			//If a file does not exist at the shared log configuration file location then write
			//the default log configuration file in the resources to that location.
			if( ! sharedLogConfigFileLocation.exists()) {
				writeDefaultLogFileTo(sharedLogConfigFileLocation);
			}

			//if log config file has changed since last time logger was configured
			//or if log has not yet been configured (sharedLogConfigFileLastModified = 0 when system starts)
			//then configure the logger
			if(sharedLogConfigFileLastModified < sharedLogConfigFileLocation.lastModified()) {

				reloadLogConfigFile();
			}

		} catch(Exception err) {

			//any exceptions should not cause application to fail so just log them
			LOG.error("Error updating log configuration: "+err.getMessage(), err);
		}
	}


	/**
	 * Configure the logger with the shared configuration file.
	 */
	private void reloadLogConfigFile() {

		LOG.info("Update log configuration using file: "+sharedLogConfigFileLocation);

		LogManager.resetConfiguration();
		DOMConfigurator.configure(sharedLogConfigFileLocation.getPath());

		sharedLogConfigFileLastModified = sharedLogConfigFileLocation.lastModified();
	}

	/**
	 * Copy the default log configuration file in the application resources to a given location.
	 * @param destination - the location to copy the file contents to
	 * @throws IOException if any IO errors occur
	 */
	private void writeDefaultLogFileTo(File destination) throws IOException {

		InputStream in = null;
		OutputStream out = null;
		try {
			in = this.getClass().getResourceAsStream(DEFAULT_LOG_CONFIG);
			out = new FileOutputStream(destination);

			int len = 0;
			byte[] buffer = new byte[8096];
			while( (len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}

		} finally {
			try {
				in.close();
			} catch(Exception err) {
				in = null;
			}
			try {
				out.close();
			} catch(Exception err) {
				out = null;
			}
		}
	}
}
