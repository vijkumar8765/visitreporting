package com.vw.visitreporting;

import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.vw.visitreporting.common.DbUtils;
import com.vw.visitreporting.security.SecurityUtils;


/**
 * Provides common functionality for all integration tests.
 * This ensures that all integration tests are tested in a common environment
 * that resembles the production environment.
 * This also provides a single entry point for changing the way integration
 * tests interact with the database (e.g. whether or not to drop the DB each test)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath:/application-contexts/applicationContext-core.xml",
	"classpath:/application-contexts/applicationContext-data.xml",
	"classpath:/application-contexts/applicationContext-services.xml",
	"classpath:/application-contexts/applicationContext-web.xml",
	"classpath:/application-contexts/applicationContext-security-authorisation.xml",
	"classpath:/application-contexts/applicationContext-security-authentication.xml"
	/* don't import security-authorisation configuration, to separate functional testing from authorisation rules */
	/* don't import the scheduling configuration, to stop scheduled tasks interfering with tests */
})
@TransactionConfiguration(defaultRollback=true)
@Transactional
public abstract class AbstractIntegrationTest {

	protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

	
	@Autowired
	protected SecurityUtils securityUtils;

    @Autowired
    protected DbUtils dbUtils;
    
	
	/**
	 * Load standard initial test data set before each test
	 * and ensure that all tests run as the standard test user. 
	 */
	@Before
	public void usualSetUp() {
        securityUtils.loginUser("test_user");
	}

	/**
	 * Clear test data from database after each test and logout current user.
	 */
	@After
	public void usualTearDown() {
		securityUtils.logoutUser();
	}
	
	
	/**
	 * Helper method for sub-classes. Converters a string date in format yyyy-MM-dd to a Date object.
	 */
	protected Date toDate(String dateString) {
		synchronized(DATE_FORMAT) {
			try {
				return DATE_FORMAT.parse(dateString);
			} catch(ParseException err) {
				fail("Could not parse date "+dateString+": "+err.getMessage());
				return null;
			}
		}
	}
}
