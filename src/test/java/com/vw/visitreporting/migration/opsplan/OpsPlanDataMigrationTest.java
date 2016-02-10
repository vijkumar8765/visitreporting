package com.vw.visitreporting.migration.opsplan;

import static org.junit.Assert.assertTrue;
import java.io.File;
import org.hibernate.SessionFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import com.vw.visitreporting.dao.system.PersonDaoMockImpl;


/**
 * Generate the migration script for OpsPlan and attempt to run it against the database
 * to ensure that all data will migrate without error.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath:/application-contexts/applicationContext-data.xml"
})
@TransactionConfiguration(defaultRollback=true)
@Transactional
public class OpsPlanDataMigrationTest {
	
	private static final boolean SKIP_TEST = true;	//enable this test when there is a complete Visit Reporting database to test against
	private static final File VW_OPPLAN_MDB_FILENAME = new File("C:\\temp\\OPData_VW.mdb"); 
	private static final File SKODA_OPPLAN_MDB_FILENAME = new File("C:\\temp\\OPData_Skoda.mdb"); 
	

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    @Autowired
    private SessionFactory sessionFactory;
	
    
	/**
	 * Generate the OpsPlan migration script to a temporary location, run it
	 * against the database and roll back afterwards.
	 */
	@Test
	public void generateAndRunMigrationScript() throws Throwable {
		
		//skip test if OPPlan.mdb file is not available
		if( ! VW_OPPLAN_MDB_FILENAME.exists() || ! SKODA_OPPLAN_MDB_FILENAME.exists() || SKIP_TEST) {
			return;
		}
		
		//generate migration script to temporary location
		File opsPlanMigrationScript = tempFolder.newFile("opsPlan.sql");
		
		OpsPlanDataMigration migration = new OpsPlanDataMigration();
		migration.run(VW_OPPLAN_MDB_FILENAME, SKODA_OPPLAN_MDB_FILENAME, opsPlanMigrationScript, new PersonDaoMockImpl());
		
		
		//run against database
		boolean success = false;
		SimpleJdbcTemplate jdbcTemplate = new SimpleJdbcTemplate(SessionFactoryUtils.getDataSource(sessionFactory));
		SimpleJdbcTestUtils.executeSqlScript(jdbcTemplate, new FileSystemResourceLoader(), opsPlanMigrationScript.getPath(), false);
		success = true;
		
		assertTrue("Migration script executed successfully", success);
	}
}
