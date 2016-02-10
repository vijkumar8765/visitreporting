package com.vw.visitreporting.security;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.UserProfile;
import com.vw.visitreporting.entity.referencedata.enums.Function;


/**
 * Test access to SystemFunction.EDIT_USER_PROFILE
 */
public class EditUserProfileSecurityTest { //extends AbstractSecurityTest {

	/**
	 * Requirements 6.1.6.2.1, 6.1.6.2.2 state:
	 * only VWG UK/VWFS org type + user profile
	 * (We assume that "The User belongs to the same Organisation and Brand as the Profile" in 6.1.6.2.2
	 *  is not necessary because user profiles are not associated to org/brands - see query log) 
	 */
	//@Test
	//commented this as we need to follow a different logic for access check
	/*
	public void testAccesses() {
		
		//grant access to profile1 to edit user profiles
		super.grantSystemFunctionToProfile("profile1", Function.EDIT_USER_PROFILE);		

		//work out whether or not each user should have access to do the work below
		Map<User, Boolean> shouldBeAuthorised = new HashMap<User, Boolean>();
		for(User user : super.testUsers) {
			
			//user only has access if they have user profile "profile1" and they belong to the VWG UK or VW-FS orgnisations
			boolean hasAccess = (userHasProfile(user, "profile1") && ("VWG-UK".equals(user.getOrganisation().toString()) || "VW-FS".equals(user.getOrganisation().toString())));
				
			shouldBeAuthorised.put(user, hasAccess);
		}
		
		//try performing a restricted operation as every user (rolling back each time)
		super.runForEachTestUser(shouldBeAuthorised, new Runnable() {
			public void run() {

				UserProfile newProfile = new UserProfile();
				newProfile.setName("test_profile");
				newProfile = userProfileService.save(newProfile);
				
				Set<Function> allowedFuncs = EnumSet.of(Function.ACCESS_REPORTING);
				userProfileService.updatePermissions(newProfile.getId(), allowedFuncs);
			}
		});
	}
	*/
	
	@Test
	public void placeholder(){
		
	}
}
