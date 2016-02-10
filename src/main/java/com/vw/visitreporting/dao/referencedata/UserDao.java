package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.User;


/**
 * Contract for a type-safe implementation of AbstractVRDao that adds additional search functionality.
 */
public interface UserDao extends VRDao<User> {

	User getById(String userId);


	/**
	 * Gets the contacts for a given dealership.
	 * @param dealer - the dealership to find contacts for.
	 * @return a list of users of this system who are contacts for this dealership
	 */
	List<User> getDealershipContacts(Dealership dealer);

	/**
	 * Gets the contacts for a given organisation.
	 * @param organisation - the organisation to find contacts for.
	 * @return a list of users of this system who are contacts for this organisation
	 */
	List<User> getOrganisationContacts(Organisation organisation);
	
	
	/**
	 * @param organisation
	 * @param dealershipNumber
	 * @param businessArea
	 * @param jobRole
	 * @param geographicalArea
	 * @param level
	 * @return
	 */
	List<User> getOrganisationContacts(Organisation organisation, Integer dealershipNumber, String businessArea, String jobRole, String geographicalArea, Integer level);
}
