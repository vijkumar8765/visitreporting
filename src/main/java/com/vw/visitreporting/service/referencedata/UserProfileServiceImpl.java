package com.vw.visitreporting.service.referencedata;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.UserProfileDao;
import com.vw.visitreporting.entity.referencedata.UserProfile;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.service.AbstractVRService;


@Service
public class UserProfileServiceImpl extends AbstractVRService<UserProfile> implements UserProfileService{

	@Autowired
	private UserProfileDao userProfileDao;


	/**
	 * Get a user profile from the database that matches the given name.
	 * @param name - the name of the user profile
	 */
	public UserProfile findByName(String name) {
		return userProfileDao.findByName(name);
	}
	
	/**
	 * Sets the collection of system functions that users asigned to the given user profile have access to.
	 * @return the updated instance of UserProfile that has been persisted to the database
	 */
	public UserProfile updatePermissions(Integer userProfileId, Set<Function> permissions) {
		UserProfile userProfile = userProfileDao.getById(userProfileId);
		return userProfileDao.updatePermissions(userProfile, permissions);
	}
	
	
	public VRDao<UserProfile> getTDao() {
		return userProfileDao;
	}
}
