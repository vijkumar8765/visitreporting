package com.vw.visitreporting.migration.opsplan;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.vw.visitreporting.dao.system.PersonDao;
import com.vw.visitreporting.dao.system.PersonDaoMockImpl;
import com.vw.visitreporting.entity.referencedata.enums.Brand;


/**
 * Connects to the OpsPlan MS Access databases and generates a SQL script that can
 * be run against the Visit Reporting database to insert all the relevant data from OpsPlan.
 * This class can be run as a stand-alone application to generate the SQL script.
 */
public class OpsPlanDataMigration {

	private static final int ID_OFFSET = 1000;	//first ID to use for each entity inserted into the database
	
	private JdbcTemplate jdbcTempl;
	
	
	/**
	 * Entry point for a stand-along application to generate the OpsPlan migration script
	 * using default arguments.
	 */
	public static void main(String[] args) throws Throwable {
		OpsPlanDataMigration app = new OpsPlanDataMigration();
		app.run(new File("C:\\temp\\OPData_VW.mdb"),
				new File("C:\\temp\\OPData_Skoda.mdb"), 
				new File("C:\\temp\\OpsPlan.sql"),
				new PersonDaoMockImpl());
	}
	
	/**
	 * Generate the full OpsPlan migration script.
	 * @param vwOpPlanFile - the MS Access .MDB database file to connect to for VW PC data
	 * @param skodaOpPlanFile - the MS Access .MDB database file to connect to for Skoda data
	 * @param migrationScriptFile - the path to the new SQL script file that will be generated (overwriting any existing file at this location)
	 * @param personDao - a DAO object to lookup person information for a username of email address
	 */
	public void run(File vwOpPlanFile, File skodaOpPlanFile, File migrationScriptFile, PersonDao personDao) throws Throwable {
				
		SqlScriptMigrator migrator = new SqlScriptMigrator(migrationScriptFile, ID_OFFSET, "DBA", new Date());
		OpsPlanMapping mapping = new OpsPlanMapping(migrator, personDao);

		this.doMapping(vwOpPlanFile, Brand.VWPC, mapping, personDao, true);
		this.doMapping(skodaOpPlanFile, Brand.SKODA, mapping, personDao, false);
		
		migrator.close();
	}
	
	/**
	 * Process a single MS Access .MDB file for migration.
	 * @param mdbFile - the MS Access .MDB database file to connect to and process
	 * @param brand - the brand that this Access database refers to
	 * @param mapping - the mapping definition processor
	 * @param personDao - a DAO object to lookup person information for a username of email address
	 * @param migrateUnmappedData - whether or not to call the OpsPlanMapping.unmapped() method - this should only be called once for a full migration script.
	 */
	private void doMapping(File mdbFile, Brand brand, OpsPlanMapping mapping, PersonDao personDao, boolean migrateUnmappedData) throws Throwable {//NOPMD
		
		this.connect(mdbFile);
		
		mapping.setBrand(brand);
		
		//insert necessary data that is not in the Access database but will be required (e.g. business areas)
		if(migrateUnmappedData) {
			mapping.unmapped();		//only insert this data once
		}
		
		//each public method in the OpsPlanMapping corresponds to a table in the OpsPlan database
		//that must be migrated. Iterate through each table to be migrated..
		for(Method method : mapping.getClass().getMethods()) {
			if( ! method.getName().startsWith("tbl_")) {
				continue;
			}
			
			//iterate through all data in this table
			SqlRowSet row = jdbcTempl.queryForRowSet("SELECT * FROM " + method.getName());
			while(row.next()) {
				try {
					
					//call the method in OpsPlanMapping that will map the OpsPlan data to the Visit Reporting database
					method.invoke(mapping, row);
					
				} catch(InvocationTargetException err) {
					
					throw err.getCause();	//allow all errors to bubble through
					
					//LOG.warn("[WARN] table="+method.getName()+" row={"+row.getString(0)+","+row.getString(1)+"} err="+err.getCause().getMessage(), err);
					
				} catch(IllegalAccessException err) {
					throw new RuntimeException("Error invoking method "+method.getName()+": "+err.getMessage(), err);
				}
			}
		}
	}

	/**
	 * Connect to an Access database.
	 * @param mdbFile - the full path of the Access database MDB file to connect to
	 */
	@SuppressWarnings("restriction")
	private void connect(File mdbFile) {
        //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        //String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="+mdbFile+";";
        //conn = DriverManager.getConnection(database, "", "");
		SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriverClass(sun.jdbc.odbc.JdbcOdbcDriver.class);
		ds.setUrl("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="+mdbFile.getPath()+";");
		this.jdbcTempl = new JdbcTemplate(ds);
	}
}
