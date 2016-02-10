package com.vw.visitreporting.dao.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.vw.visitreporting.dto.PersonDTO;


/**
 * Provides a mock implementation of PersonDao that obtains person information from
 * the users.tsv text file in the resources.
 * The PersonDao should be implemented using a live data source for person data, such as LDAP.
 */
@Repository
public class PersonDaoMockImpl implements PersonDao {

	private static final String USERS_FILE = "/person.tsv";
	private static final String USERNAME_MAPPING_FILE = "/personUsernameMap.tsv";
	
	private final Map<String, PersonDTO> usernameToPerson;
	private final Map<String, PersonDTO> emailToPerson;
	private final Map<String, String> usernameToFoxId;
	
	
	/**
	 * Constructor. Loads the users.tsv file from resources and creates maps to PersonDTO
	 * instances, indexed by username and by email address.
	 */
	public PersonDaoMockImpl() {
		this.usernameToPerson = new HashMap<String, PersonDTO>();
		this.emailToPerson = new HashMap<String, PersonDTO>();
		this.usernameToFoxId = new HashMap<String, String>();
		BufferedReader reader = null;
		String line = null;
		String[] row = null;
		try {
			reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(USERNAME_MAPPING_FILE)));
			
			while( (line = reader.readLine()) != null) {
				row = line.split("\t", -1);
				usernameToFoxId.put(row[1].toLowerCase(), row[0].toLowerCase());
			}
			reader.close();

			
			reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(USERS_FILE)));
			
			line = reader.readLine();	//header
			row = line.split("\t", -1);
			Map<String, Integer> headings = new HashMap<String, Integer>();
			for(int i=0; i<row.length; i++) {
				headings.put(row[i], i);
			}
			
			while( (line = reader.readLine()) != null) {
				
				row = line.split("\t", -1);

				PersonDTO person = this.personFromRowInFile(row, headings);
				usernameToPerson.put(person.getUsername().toLowerCase(), person);
				emailToPerson.put(person.getEmail().toLowerCase(), person);
			}
			
		} catch(IOException err) {
			throw new RuntimeException("Error parsing users.tsv file: "+err.getMessage(), err);
		} finally {
			try {
				reader.close();
			} catch(Exception err) {
				reader = null;
			}
		}
	}
	
	
	/**
	 * Lookup information for person by their windows username.
	 */
	public PersonDTO getPersonByUsername(String username) {
		if(username == null) {
			return null;
		}
		username = username.toLowerCase();
		if(usernameToPerson.containsKey(username)) {
			return usernameToPerson.get(username);
		} else {
			if(usernameToFoxId.containsKey(username)) {
				return usernameToPerson.get(usernameToFoxId.get(username));
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Lookup information for person by their email address.
	 */
	public PersonDTO getPersonByEmailAddress(String email) {
		if(email == null) {
			return null;
		}
		return emailToPerson.get(email.toLowerCase());
	}


	/**
	 * Reads person data from a row of the users.tsv file in a new PersonDTO instance.
	 */
	private PersonDTO personFromRowInFile(String[] row, Map<String, Integer> headings) {
		
		//ensure that fox id is used where possible
		String userId = row[headings.get("Username")].toLowerCase();
		if(usernameToFoxId.containsKey(userId)) {
			userId = usernameToFoxId.get(userId);
		}
		
		PersonDTO person = new PersonDTO();
		person.setUsername(userId);
		person.setFirstname(row[headings.get("First")]);
		person.setSurname(row[headings.get("Surname")]);
		person.setEmail(row[headings.get("Email")]);
		person.setLandline(row[headings.get("Landline")]);
		person.setMobile(row[headings.get("Mobile")]);
		person.setOrganisation(row[headings.get("Organisation")]);
		person.setBrand(row[headings.get("Brand")]);
		person.setBusinessArea(row[headings.get("BusinessArea")]);
		return person;
	}
}
