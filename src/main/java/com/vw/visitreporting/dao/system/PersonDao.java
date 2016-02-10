package com.vw.visitreporting.dao.system;

import com.vw.visitreporting.dto.PersonDTO;


/**
 * Specifies the behaviour that a person lookup service must implement.
 * This DAO will lookup information about an employee of the Volkswagen Group, who
 * is not necessarily a member of this system.
 *
 */
public interface PersonDao {

	/**
	 * Lookup information for person by their windows username.
	 */
	PersonDTO getPersonByUsername(String username);
	
	/**
	 * Lookup information for person by their email address.
	 */
	PersonDTO getPersonByEmailAddress(String email);
}
