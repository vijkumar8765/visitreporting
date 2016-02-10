package com.vw.visitreporting.dao;

import static org.junit.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import com.vw.visitreporting.dao.system.PersonDao;
import com.vw.visitreporting.dao.system.PersonDaoMockImpl;
import com.vw.visitreporting.dto.PersonDTO;

/**
 * Unit tests implementations of the PersonDao interface.
 */
public class PersonDaoTest {
	
	private final PersonDao personDao;
	
	public PersonDaoTest() {
		personDao = new PersonDaoMockImpl();	//create instance of PersonDao implementation to test.
	}
	

	/**
	 * Test that the PersonDao implementation can lookup an existing person in Volkswagen Group
	 * by their username.
	 */
	@Test
	public void testCanLookupExistingUserByUsername() {
		
		PersonDTO person = personDao.getPersonByUsername("fIX7ma6");
		assertPersonDetailsMatchAndy(person);

		person = personDao.getPersonByUsername("mcKAya");
		assertPersonDetailsMatchAndy(person);
	}

	/**
	 * Test that the PersonDao implementation can lookup an existing person in Volkswagen Group
	 * by their email address.
	 */
	@Test
	public void testCanLookupExistingUserByEmailAddress() {
		
		PersonDTO person = personDao.getPersonByEmailAddress("AndREW.McKay@VolksWAGEN.co.uk");
		
		assertPersonDetailsMatchAndy(person);
	}
	
	/**
	 * Assert that a PersonDTO instance contains the correct details for Cheryl Manning
	 */
	private static void assertPersonDetailsMatchAndy(PersonDTO person) {
		assertNotNull("check person instance was found", person);
		assertThat(person.getUsername(), is(equalToIgnoringCase("fix7ma6")));
		assertThat(person.getFirstname(), is(equalToIgnoringCase("Andrew")));
		assertThat(person.getSurname(), is(equalToIgnoringCase("Mckay")));
		assertThat(person.getEmail(), is(equalToIgnoringCase("Andrew.McKay@Volkswagen.co.uk")));
		assertThat(person.getLandline(), is(equalToIgnoringCase("+44 (1908) 601601")));
		assertThat(person.getMobile(), is(equalToIgnoringCase("")));
		assertThat(person.getOrganisation(), is(equalToIgnoringCase("VWG UK")));
		assertThat(person.getBrand(), is(equalToIgnoringCase("All")));
		assertThat(person.getBusinessArea(), is(equalToIgnoringCase("Admin")));
	}
}
