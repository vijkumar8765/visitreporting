package com.vw.visitreporting.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import javax.sql.DataSource;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.xml.sax.InputSource;
import com.vw.visitreporting.common.exception.ExceptionCode;
import com.vw.visitreporting.common.exception.VisitReportingException;


/**
 * Provides utility functions to work with the DBUnit library.
 * Instances of this class must be instantiated within a Spring application context.
 */
public class DbUtils implements InitializingBean {
	
    private static final Logger LOG = LoggerFactory.getLogger(DbUtils.class);

    @Autowired
	private SessionFactory sessionFactory;

	private String[] setupScripts;
	
	public void setSetupScripts(String setupScripts) {
		this.setupScripts = setupScripts.split(";");
	}
	
	
	/**
	 * Run any SQL setup scripts that have been provided as soon as the application context has been setup
	 */
	public void afterPropertiesSet() throws Exception {//NOPMD
		if(setupScripts != null && setupScripts.length > 0 && ! this.isDatabaseInitialised()) {
			this.insertInitialData();
		}
	}
	
	
	/**
	 * Gets a new or existing Hibernate session for the current transaction.
	 */
	public Session getSession() {
		return SessionFactoryUtils.getSession(sessionFactory, true);
	}
	
	
	/**
	 * Returns true if the database has been initialised with some initial data.
	 * This check is performed by checking whether or not the USER table has any rows.
	 */
	public boolean isDatabaseInitialised() {
		SimpleJdbcTemplate jdbcTemplate = new SimpleJdbcTemplate(SessionFactoryUtils.getDataSource(sessionFactory));
		int userCount = jdbcTemplate.queryForInt("select count(*) from user");
		return (userCount > 0);
	}
	

	/**
	 * Imports the standard test data sets from resources into the database using DBUnit.
	 */
	private void insertInitialData() {	//NOPMD
		insertData(setupScripts);
    }
	

	/**
	 * Imports test data sets from resources into the database using DBUnit.
	 * @param dbUnitDataFiles - a collection of resource locations to insert
	 */
	public void insertData(String[] dbUnitDataFiles) {
		if(dbUnitDataFiles == null) {
			return;
		}
        String currentScript = null;
        try {
        	@SuppressWarnings("deprecation")
			Connection conn = SessionFactoryUtils.getSession(sessionFactory, true).connection();
        	
			for(String setupScript : dbUnitDataFiles) {
				currentScript = setupScript;
				InputStream in = this.getClass().getResourceAsStream(setupScript);
				if(in == null) {
					LOG.warn("DBUnit script: "+setupScript+" could not be found in resources so was not loaded");
				} else {
					FlatXmlProducer flatXml = new FlatXmlProducer(new InputSource(in));
					flatXml.setColumnSensing(true);
					CachedDataSet initDataset = new CachedDataSet(flatXml);
			        
					DatabaseOperation.INSERT.execute(new DatabaseConnection(conn), initDataset);
					LOG.info("DBUnit script: "+setupScript+" successfully loaded");
				}
			}			
        } catch(Exception err) {
        	throw new VisitReportingException(ExceptionCode.GENERAL, "Error inserting initial data from script "+currentScript+": "+err.getMessage(), err);
		}
	}
	
	
	/**
	 * Take a snapshot export of a database, with connections details supplied by the current application context.
	 * Note that the XML output will not take entity dependencies into account so will probably need to be manually
	 * altered to ensure rows are added in this appropriate order. 
	 * @param file - full path of the XML filename to output the database contents to.
	 */
    public void doDatabaseDump(File dataFile) {
    	
    	//first flush the session to ensure the database is in intended state
    	sessionFactory.getCurrentSession().flush();

    	//connect to database
    	DataSource ds = SessionFactoryUtils.getDataSource(sessionFactory);
    	IDatabaseConnection dbunitConn = null;
    	try {
    		dbunitConn = new DatabaseConnection(ds.getConnection());
    		
    		//write DTD file for database schema
            //FlatDtdDataSet.write(dbunitConn.createDataSet(), new FileOutputStream(dtdFile));

        	//write contents of database to DBUnit xml file
	        IDataSet fullDataSet = dbunitConn.createDataSet();
	        FlatXmlDataSet.write(fullDataSet, new FileOutputStream(dataFile));
	    	
    	} catch(Exception err) {
    		throw new RuntimeException("Could not get dump of database for DBUnit", err);
    	} finally {
    		try {
    			dbunitConn.close();
    		} catch(Exception err) {
    			dbunitConn = null;
    		}
    	}
    }
}
