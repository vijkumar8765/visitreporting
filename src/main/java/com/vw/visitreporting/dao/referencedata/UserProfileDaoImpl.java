package com.vw.visitreporting.dao.referencedata;

import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.entity.referencedata.UserProfile;
import com.vw.visitreporting.entity.referencedata.enums.Function;


/**
 * Data access object for the UserProfile entity.
 * Provides a type-safe implementation of AbstractVRDao and adds additional search functionality.
 */
@Repository
public class UserProfileDaoImpl extends AbstractVRDao<UserProfile> implements UserProfileDao{


	/**
	 * Sets the collection of system functions that users asigned to the given user profile have access to.
	 * Access to Function.EDIT_USER_PROFILE required to perform this operation.
	 * @return the updated instance of UserProfile that has been persisted to the database
	 */
	@PreAuthorize("hasPermission(#userProfile, 'EDIT_USER_PROFILE')")
	public UserProfile updatePermissions(UserProfile userProfile, Set<Function> permissions) {
		userProfile.setAllowedFunctions(permissions);
		return super.save(userProfile);
	}

	
	/**
	 * Get a user profile from the database that matches the given name.
	 * @param name - the name of the user profile
	 */
	public UserProfile findByName(String name) {
		Criteria crit = createBaseCriteria();
		crit.add(Restrictions.eq("name", name));
		return super.findOne(crit);
	}

	/**
	 * Access to Function.EDIT_USER_PROFILE required to perform this operation.
	 */
	@PreAuthorize("hasPermission(#userProfile, 'EDIT_USER_PROFILE')")
	public UserProfile save(UserProfile userProfile) {
		return super.save(userProfile);
	}
}
