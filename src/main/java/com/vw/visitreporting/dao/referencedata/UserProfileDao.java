package com.vw.visitreporting.dao.referencedata;

import java.util.Set;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.referencedata.UserProfile;
import com.vw.visitreporting.entity.referencedata.enums.Function;


/**
 * Contract for a type-safe implementation of AbstractVRDao that adds additional search functionality.
 */
public interface UserProfileDao extends VRDao<UserProfile>{

	/**
	 * Sets the collection of system functions that users asigned to the given user profile have access to.
	 * Access to Function.EDIT_USER_PROFILE required to perform this operation.
	 * @return the updated instance of UserProfile that has been persisted to the database
	 */
	UserProfile updatePermissions(UserProfile userProfile, Set<Function> permissions);

	/**
	 * Get a user profile from the database that matches the given name.
	 * @param name - the name of the user profile
	 */
	UserProfile findByName(String name);
}
