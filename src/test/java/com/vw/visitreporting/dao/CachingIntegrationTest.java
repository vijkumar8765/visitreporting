package com.vw.visitreporting.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import javax.persistence.Table;
import net.sf.ehcache.CacheManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.vw.visitreporting.AbstractIntegrationTest;
import com.vw.visitreporting.entity.referencedata.MeetingType;
import com.vw.visitreporting.service.referencedata.MeetingTypeService;


/**
 * Test that 2nd-level caching is correctly configured in the data access layer.
 */
public class CachingIntegrationTest extends AbstractIntegrationTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream outStream = new PrintStream(outContent);
	private PrintStream prevOut;
	
	@Autowired
	private MeetingTypeService meetingTypeService;
	
	@Autowired
	private CacheManager cacheManager;
	
	
	/**
	 * Redirect console output to memory buffer before each test
	 * so that selects to the DB can be monitored.
	 */
	@Before
	public void setUpStreamsAndClearCache() {
		prevOut = System.out;
		outStream.flush();
		outContent.reset();
		System.setOut(outStream);
		
		cacheManager.clearAll();
	}
	
	/**
	 * Restore console output after each test.
	 * @throws IOException 
	 */
	@After
	public void cleanUpStreams() throws IOException {
		System.out.flush();
		System.setOut(prevOut);
	}
	
	
	/**
	 * Test that Cached entities are being cached appropriately.
	 */
	@Test
	public void testThat2ndLevelCachingIsApplied() {
		
		String tableName = MeetingType.class.getSimpleName();
		Table table = MeetingType.class.getAnnotation(Table.class);
		if(table != null) {
			tableName = ((table.schema().length() > 0) ? table.schema()+"." : "") + table.name();
		}
		
		MeetingType type1 = meetingTypeService.getById(0);

		assertDatabaseSelect(tableName, true);
	
		MeetingType type2 = meetingTypeService.getById(0);

		assertDatabaseSelect(tableName, false);

		assertThat(type1, is(equalTo(type2)));
	}

	
	/**
	 * Assert that a select operation on a given table has been run on the database.
	 * This is checked by looking for a select statement in the console output.
	 * @param entityName - the name of the entity table (case-sensitive)
	 * @param happened - if true this function asserts that the select DID happen, if false, this function asserts that it DID NOT happen.
	 */
	private void assertDatabaseSelect(String entityName, boolean happened) {
		this.outStream.flush();
		String[] lines = this.outContent.toString().split("\n");
		this.outContent.reset();
		for(String line : lines) {
			if(line.startsWith("Hibernate: select") && line.contains("from "+entityName)) {
				if(happened) {
					return;
				} else {
					fail("Select statement for "+entityName+" was found in console output");
				}
			}
		}
		if(happened) {
			fail("Select statement for "+entityName+" not found in console output");
		} else {
			return;
		}
	}
}
