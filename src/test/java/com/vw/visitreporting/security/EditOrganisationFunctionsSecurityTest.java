package com.vw.visitreporting.security;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Function;


/**
 * Test access to SystemFunction.EDIT_ORGANISATION_FUNCTIONS
 */
public class EditOrganisationFunctionsSecurityTest{ //extends AbstractSecurityTest {

	/**
	 * Requirements 6.1.6.1.1, 6.1.6.1.2 state:
	 * only VWG-UK / VW-FS org type + user profile + brands match
	 * (We assume that "The User belongs to the same Organisation and Brand" in 6.1.6.1.2
	 *  should be reduced to "brands match" because this would not allow Dealership/3rdParty
	 *  permissions to be modified - see query log) 
	 */
	@Test
	public void testNothing() {
		
	}	
	//hasPermission(brand, 'EDIT_ORGANISATION_FUNCTIONS')  doesn't work yet
	
	/*
	public void testAccesses() {

		final Brand targetBrand = Brand.AUDI;
		
		//grant  access to profile1 to edit user profiles
		super.grantSystemFunctionToProfile("profile1", Function.EDIT_ORGANISATION_FUNCTIONS);		

		//work out whether or not each user should have access to do the work below
		Map<User, Boolean> shouldBeAuthorised = new HashMap<User, Boolean>();
		for(User user : super.testUsers) {
			
			//user only has access if they have user profile "profile1" and they belong to the VWG UK or VW-FS orgnisations and their brand matches the target brand
			boolean hasAccess = (userHasProfile(user, "profile1") && 
					             ("VWG-UK".equals(user.getOrganisation().toString()) || "VW-FS".equals(user.getOrganisation().toString())) &&
					             ! user.getBrands().contains(targetBrand));
				
			shouldBeAuthorised.put(user, hasAccess);
		}
		
		//try performing a restricted operation as every user (rolling back each time)
		super.runForEachTestUser(shouldBeAuthorised, new Runnable() {
			public void run() {

				Organisation org = organisationService.findByName("Dealerships");
				organisationService.updateOrganisationPermissions(org.getId(), targetBrand, EnumSet.of(Function.ACCESS_REPORTING));
			}
		});
	}
	*/
}
