package com.vw.visitreporting.web.controller.referencedata;	//NOPMD

import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.ModelAndViewAssert;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import com.google.common.base.Joiner;
import com.vw.visitreporting.entity.referencedata.UserProfile;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;
import com.vw.visitreporting.service.referencedata.UserProfileService;
import com.vw.visitreporting.web.AbstractControllerIntegrationTest;


/**
 * Performs an integration test using the SystemFunctionsController class.
 */
public class UserProfileControllerIntegrationTest extends AbstractControllerIntegrationTest {
	
	private static final Integer PROFILE_ID = 1;

    @Autowired
    private UserProfileService userProfileService;
	
    
	/**
	 * Test that edit functions landing page has necessary data.
	 */

    public void testPermissionsLandingPage() {

        ModelAndView mav = handle("GET", "/userprofile/permissions/list");
        
        ModelAndViewAssert.assertViewName(mav,"userprofile/permissions/list");
        
        assertContainsAllProfiles(mav);
    }
    
    /**
     * Test that necessary data is available for Permissions update page for "Test Profile" user profile.
     */

    public void testPermissionsPageForSpecificProfile() {

    	UserProfile originalProfileFromDb = userProfileService.getById(1);
    	Set<Function> originalPermissionsFromDb = originalProfileFromDb.getAllowedFunctions();

    	ModelAndView mav = handle("GET", "/userprofile/1/permissions/update");
		
		ModelAndViewAssert.assertViewName(mav,"userprofile/permissions/list");
		
		assertContainsAllProfiles(mav);
		
		ModelAndViewAssert.assertModelAttributeValue(mav, "profileId", PROFILE_ID);
		
		assertPermissionChanges(mav, originalPermissionsFromDb, EnumSet.noneOf(Function.class));
		
		asserModifiedInLast10Seconds(mav, "DBA", toDate("2011-01-01"));
    }

    /**
     * Test that "Test Profile" User Profile can be granted access to functions.
     */
    
    public void testUpdatingPermissionsForProfile() {
    	
    	//add two permissions to "Test Profile" User Profile
    	EnumSet<Function> grantedFunctions = EnumSet.of(Function.ACCESS_DEALER_INFO, Function.ACCESS_MESSAGE_BOARD);
    	
    	String[] grantedFunctionsStr = Joiner.on(",").join(EnumUtil.convertFromEnum(grantedFunctions)).split(",");
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("allowedFunctions", grantedFunctionsStr);
    	
        ModelAndView mav = handle("POST", "/userprofile/"+PROFILE_ID+"/permissions/save", params);
        
		ModelAndViewAssert.assertViewName(mav,"userprofile/permissions/list");
		
		assertContainsAllProfiles(mav);
		
		ModelAndViewAssert.assertModelAttributeValue(mav, "profileId", PROFILE_ID);

		assertPermissionChanges(mav, grantedFunctions, EnumSet.noneOf(Function.class));
		
		asserModifiedInLast10Seconds(mav, "test_user", new Date());

    
    	//remove one and add another
    	grantedFunctions = EnumSet.of(Function.ACCESS_MESSAGE_BOARD, Function.ACCESS_REPORTING);
    	
    	grantedFunctionsStr = Joiner.on(",").join(EnumUtil.convertFromEnum(grantedFunctions)).split(",");
    	
    	params.put("allowedFunctions", grantedFunctionsStr);
    	
        mav = handle("POST", "/userprofile/"+PROFILE_ID+"/permissions/save", params);
        
		ModelAndViewAssert.assertViewName(mav,"userprofile/permissions/list");
		
		assertContainsAllProfiles(mav);
		
		ModelAndViewAssert.assertModelAttributeValue(mav, "profileId", PROFILE_ID);

		assertPermissionChanges(mav, grantedFunctions, EnumSet.of(Function.ACCESS_DEALER_INFO));
		
		asserModifiedInLast10Seconds(mav, "test_user", new Date());
    }
    
    @Test
    public void placeHolder(){
    	
    }
    
    private void assertContainsAllProfiles(ModelAndView mav) {
    	ModelAndViewAssert.assertSortAndCompareListModelAttribute(mav, "userProfiles", userProfileService.list(), new Comparator<UserProfile>() {
			public int compare(UserProfile o1, UserProfile o2) {
				return o1.getId().compareTo(o2.getId());
			}
    	});
    }
    
	private void assertPermissionChanges(ModelAndView mav, Set<Function> grantedFunctions, Set<Function> deniedFunctions) {
    	
		@SuppressWarnings("unchecked")
		Set<Function> currentAllowedFunctions = (Set<Function>)ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "allowedFunctions", Set.class);
		
		assertTrue("all granted functions are present", currentAllowedFunctions.containsAll(grantedFunctions));
		
		assertTrue("no denied functions are present", Collections.disjoint(currentAllowedFunctions, deniedFunctions));
    }
	
	private void asserModifiedInLast10Seconds(ModelAndView mav, String modifiedBy, Date modifiedDate) {
		assertThat(modifiedBy,
				is(equalTo(ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "modifiedBy", String.class)))
			);
		long milliDiff = modifiedDate.getTime() - ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "modifiedDate", Date.class).getTime();
		assertThat(Math.abs(milliDiff), is(lessThan(10000L)));
	}
}
