package com.vw.visitreporting.service.referencedata;

import java.util.Set;
import com.vw.visitreporting.entity.referencedata.UserProfile;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.service.VRService;


public interface UserProfileService extends VRService<UserProfile>{

	/**
	 * Get a user profile from the database that matches the given name.
	 * @param name - the name of the user profile
	 */
	UserProfile findByName(String name);

	/**
	 * Sets the collection of system functions that users asigned to the given user profile have access to.
	 * @return the updated instance of UserProfile that has been persisted to the database
	 */
	UserProfile updatePermissions(Integer userProfileId, Set<Function> permissions);
}
