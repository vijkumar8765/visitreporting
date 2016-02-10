package com.vw.visitreporting.web.controller.referencedata;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.ModelAndViewAssert;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import com.google.common.base.Joiner;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;
import com.vw.visitreporting.service.referencedata.OrganisationService;
import com.vw.visitreporting.web.AbstractControllerIntegrationTest;
import com.vw.visitreporting.web.form.OrganisationBrand;


/**
 * Performs an integration test using the SystemFunctionsController class.
 */
public class OrganisationControllerIntegrationTest extends AbstractControllerIntegrationTest {//NOPMD

    @Autowired
    private OrganisationService organisationService;
	
    
	/**
	 * Test that edit functions landing page has necessary data.
	 */
	@Test
    public void testPermissionsLandingPage() {

        ModelAndView mav = handle("GET", "/organisation/permissions/list");
        
        ModelAndViewAssert.assertViewName(mav,"organisation/permissions/list");
        
        assertContainsAllOrgBrands(mav);
    }
    
    /**
     * Test that necessary data is available for Permissions update page for various organisations and brands.
     */
    @Test
    public void testPermissionsPageForVariousOrgBrands() {//NOPMD
    	runPermissionsPageFor("Dealerships", Brand.AUDI);
    	runPermissionsPageFor("VWG-UK", Brand.AUDI);
    	runPermissionsPageFor("VW-FS", null);
    	runPermissionsPageFor("Mondial", Brand.AUDI);
    }

    private void runPermissionsPageFor(String orgName, Brand brand) {
    	Organisation org = organisationService.findByName(orgName);
    	OrganisationBrand orgBrand = new OrganisationBrand(org, brand);
    	
    	Set<Function> grantedFuncsFromDb = organisationService.getPermissions(org.getId(), brand);
    	
		ModelAndView mav = handle("GET", "/organisation/"+orgBrand.getId()+"/permissions/update");
		
		ModelAndViewAssert.assertViewName(mav,"organisation/permissions/list");
		
		assertContainsAllOrgBrands(mav);
		
    	OrganisationBrand mavOrgBrand = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "organisationBrand", OrganisationBrand.class);
    	assertThat(orgName, is(equalTo(mavOrgBrand.getOrganisationName())));
    	assertThat(brand, is(equalTo(mavOrgBrand.getBrand())));
				
    	assertPermissionChanges(mav, grantedFuncsFromDb, EnumSet.noneOf(Function.class));

		assertModifiedInLast10Seconds(mav, "DBA", toDate("2011-01-01"));
    }


    /**
     * Test that various organisations and brands can be granted access to functions.
     */
    @Test
    public void testUpdatingPermissionsForVariousOrgBrands() { //NOPMD
    	runUpdatePermissionsPageFor("Dealerships", Brand.AUDI);
    	runUpdatePermissionsPageFor("VWG-UK", Brand.AUDI);
    	runUpdatePermissionsPageFor("VW-FS", null);
    	runUpdatePermissionsPageFor("Mondial", Brand.AUDI);
    }
    
    private void runUpdatePermissionsPageFor(String orgName, Brand brand) {
    	Organisation org = organisationService.findByName(orgName);
    	OrganisationBrand orgBrand = new OrganisationBrand(org, brand);
    	
    	//add two permissions to "Test Profile" User Profile
    	EnumSet<Function> grantedFunctions = EnumSet.of(Function.ACCESS_DEALER_INFO, Function.ACCESS_MESSAGE_BOARD);
    	
    	String[] grantedFunctionsStr = Joiner.on(",").join(EnumUtil.convertFromEnum(grantedFunctions)).split(",");
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("allowedFunctions", grantedFunctionsStr);
    	
        ModelAndView mav = handle("POST", "/organisation/"+orgBrand.getId()+"/permissions/save", params);
		
		ModelAndViewAssert.assertViewName(mav,"organisation/permissions/list");
		
		assertContainsAllOrgBrands(mav);
		
    	OrganisationBrand mavOrgBrand = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "organisationBrand", OrganisationBrand.class);
    	assertThat(orgName, is(equalTo(mavOrgBrand.getOrganisationName())));
    	assertThat(brand, is(equalTo(mavOrgBrand.getBrand())));
				
    	assertPermissionChanges(mav, grantedFunctions, EnumSet.noneOf(Function.class));

		assertModifiedInLast10Seconds(mav, "test_user", new Date());

    
    	//remove one and add another
    	grantedFunctions = EnumSet.of(Function.ACCESS_MESSAGE_BOARD, Function.ACCESS_REPORTING);
    	
    	grantedFunctionsStr = Joiner.on(",").join(EnumUtil.convertFromEnum(grantedFunctions)).split(",");
    	
    	params.put("allowedFunctions", grantedFunctionsStr);
    	
        mav = handle("POST", "/organisation/"+orgBrand.getId()+"/permissions/save", params);
		
		ModelAndViewAssert.assertViewName(mav,"organisation/permissions/list");
		
		assertContainsAllOrgBrands(mav);
		
    	mavOrgBrand = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "organisationBrand", OrganisationBrand.class);
    	assertThat(orgName, is(equalTo(mavOrgBrand.getOrganisationName())));
    	assertThat(brand, is(equalTo(mavOrgBrand.getBrand())));
				
    	assertPermissionChanges(mav, grantedFunctions, EnumSet.of(Function.ACCESS_DEALER_INFO));
		
		assertModifiedInLast10Seconds(mav, "test_user", new Date());
    }
    

	/**
	 * Test that sharing rules landing page has necessary data.
	 */
	@Test
    public void testSharingRulesLandingPage() {

        ModelAndView mav = handle("GET", "/organisation/sharingrules/list");
        
        ModelAndViewAssert.assertViewName(mav,"organisation/sharingrules/list");
        
        assertContainsAllOrgBrands(mav);
    }
    
	
    /**
     * Test that necessary data is available for Sharing Rules update page for various organisations and brands.
     */
    @Test
    public void testSharingRulesPageForVariousOrgBrands() {//NOPMD
    	runSharingRulesPageFor("Dealerships", Brand.AUDI);
    	runSharingRulesPageFor("VWG-UK", Brand.AUDI);
    	runSharingRulesPageFor("VW-FS", null);
    	runSharingRulesPageFor("Mondial", Brand.AUDI);
    }

    private void runSharingRulesPageFor(String orgName, Brand brand) {
    	Organisation org = organisationService.findByName(orgName);
    	OrganisationBrand orgBrand = new OrganisationBrand(org, brand);
    	
		ModelAndView mav = handle("GET", "/organisation/"+orgBrand.getId()+"/sharingrules/update");
		
		ModelAndViewAssert.assertViewName(mav,"organisation/sharingrules/list");
		
		assertContainsAllOrgBrands(mav);
		
    	OrganisationBrand mavOrgBrand = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "rootOrgBrand", OrganisationBrand.class);
    	assertThat(orgName, is(equalTo(mavOrgBrand.getOrganisationName())));
    	assertThat(brand, is(equalTo(mavOrgBrand.getBrand())));
		
    	assertSharingRuleChanges(mav, new HashSet<OrganisationBrand>());
		
    	assertModifiedInLast10Seconds(mav, "DBA", toDate("2011-01-01"));
    }	
	
	
    /**
     * Test that various organisations and brands can be granted access to information.
     */
    @Test
    public void testUpdatingSharingRulesForVariousOrgBrands() { //NOPMD
    	OrganisationBrand audiDealer = new OrganisationBrand(organisationService.findByName("Dealerships"), Brand.AUDI);
    	OrganisationBrand audiBrand = new OrganisationBrand(organisationService.findByName("VWG-UK"), Brand.AUDI);
    	OrganisationBrand fs = new OrganisationBrand(organisationService.findByName("VW-FS"), null);
    	OrganisationBrand mondial = new OrganisationBrand(organisationService.findByName("Mondial"), Brand.AUDI);
    	
    	runUpdateSharingRulesPageFor(audiDealer,
    			new OrganisationBrand[] {audiBrand, fs},
    			new OrganisationBrand[] {fs, mondial}
    		);
    	runUpdateSharingRulesPageFor(audiBrand,
    			new OrganisationBrand[] {audiDealer, fs},
    			new OrganisationBrand[] {fs, mondial}
    		);
    	runUpdateSharingRulesPageFor(fs,
    			new OrganisationBrand[] {audiDealer, audiBrand},
    			new OrganisationBrand[] {audiBrand, mondial}
    		);
    	runUpdateSharingRulesPageFor(mondial,
    			new OrganisationBrand[] {audiDealer, audiBrand},
    			new OrganisationBrand[] {audiBrand, fs}
    		);
    }
    
    private void runUpdateSharingRulesPageFor(OrganisationBrand rootOrgBrand, OrganisationBrand[] targetOrgBrands1, OrganisationBrand[] targetOrgBrands2) {
    	
    	String[] targetOrgBrandsStr = new String[targetOrgBrands1.length];
    	for(int i=0; i<targetOrgBrands1.length; i++) {
    		targetOrgBrandsStr[i] = targetOrgBrands1[i].getId();
    	}
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("targetOrgBrands", targetOrgBrandsStr);
    	
    	ModelAndView mav = handle("POST", "/organisation/"+rootOrgBrand.getId()+"/sharingrules/save", params);

		ModelAndViewAssert.assertViewName(mav,"organisation/sharingrules/list");
		
		assertContainsAllOrgBrands(mav);

		HashSet<OrganisationBrand> setOfTargetOrgBrands1 = new HashSet<OrganisationBrand>();
		setOfTargetOrgBrands1.addAll(Arrays.asList(targetOrgBrands1));

    	OrganisationBrand mavOrgBrand = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "rootOrgBrand", OrganisationBrand.class);
    	assertThat(rootOrgBrand.getOrganisationName(), is(equalTo(mavOrgBrand.getOrganisationName())));
    	assertThat(rootOrgBrand.getBrand(), is(equalTo(mavOrgBrand.getBrand())));

    	assertSharingRuleChanges(mav, setOfTargetOrgBrands1);

    	assertModifiedInLast10Seconds(mav, "test_user", new Date());

    
    	//remove one and add another
    	targetOrgBrandsStr = new String[targetOrgBrands2.length];
    	for(int i=0; i<targetOrgBrands2.length; i++) {
    		targetOrgBrandsStr[i] = targetOrgBrands2[i].getId();
    	}
    	
    	params.put("targetOrgBrands", targetOrgBrandsStr);
    	
        mav = handle("POST", "/organisation/"+rootOrgBrand.getId()+"/sharingrules/save", params);
        
		ModelAndViewAssert.assertViewName(mav,"organisation/sharingrules/list");
		
		assertContainsAllOrgBrands(mav);

		HashSet<OrganisationBrand> setOfTargetOrgBrands2 = new HashSet<OrganisationBrand>();
		setOfTargetOrgBrands2.addAll(Arrays.asList(targetOrgBrands2));

    	mavOrgBrand = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "rootOrgBrand", OrganisationBrand.class);
    	assertThat(rootOrgBrand.getOrganisationName(), is(equalTo(mavOrgBrand.getOrganisationName())));
    	assertThat(rootOrgBrand.getBrand(), is(equalTo(mavOrgBrand.getBrand())));

    	assertSharingRuleChanges(mav, setOfTargetOrgBrands2);

    	assertModifiedInLast10Seconds(mav, "test_user", new Date());
    }
    
    
    
    /**
	 * Test that organisational levels landing page has necessary data.
	 */
	@Test
	@SuppressWarnings("unchecked")
    public void testLevelsLandingPage() {

        ModelAndView mav = handle("GET", "/organisation/levels/list");
        
        ModelAndViewAssert.assertViewName(mav,"organisation/levels/list");
        
        //check list of orgs is present
        ModelAndViewAssert.assertSortAndCompareListModelAttribute(mav, "organisations", organisationService.list(), new Comparator<Organisation>() {
			public int compare(Organisation o1, Organisation o2) {
				return o1.getId().compareTo(o2.getId());
			}
    	});
        
        //check orgs have access levels
    	List<Organisation> orgs = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "organisations", List.class);
    	for(Organisation org : orgs) {
    		assertFalse("lazy levels property not initialised", org.getLevels().isEmpty());
    	}
    }
    
    
	
	
    private void assertContainsAllOrgBrands(ModelAndView mav) {
    	@SuppressWarnings("unchecked")
		List<OrganisationBrand> orgBrands = (List<OrganisationBrand>)ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "organisationBrands", List.class);
    	
    	//sort orgBrands by organisation name and then by brand short name
    	Collections.sort(orgBrands, new Comparator<OrganisationBrand>() {
			public int compare(OrganisationBrand o1, OrganisationBrand o2) {
				int result = o1.getOrganisationName().compareTo(o2.getOrganisationName());
				if(result == 0) {
					return o1.getBrand().getShortName().compareTo(o2.getBrand().getShortName());
				} else {
					return result;
				}
			}
    	});
    	assertThat(orgBrands.size(), is(equalTo(12)));
    	assertThat(orgBrands.get(0).toString(), is(equalTo("Audi Dealerships")));
    	assertThat(orgBrands.get(1).toString(), is(equalTo("SEAT Dealerships")));
    	assertThat(orgBrands.get(2).toString(), is(equalTo("Skoda Dealerships")));
    	assertThat(orgBrands.get(3).toString(), is(equalTo("VW CV Dealerships")));
    	assertThat(orgBrands.get(4).toString(), is(equalTo("VW PC Dealerships")));
    	assertThat(orgBrands.get(5).toString(), is(equalTo("Mondial (Audi)")));
    	assertThat(orgBrands.get(6).toString(), is(equalTo("VW-FS")));
    	assertThat(orgBrands.get(7).toString(), is(equalTo("Audi VWG-UK")));
    	assertThat(orgBrands.get(8).toString(), is(equalTo("SEAT VWG-UK")));
    	assertThat(orgBrands.get(9).toString(), is(equalTo("Skoda VWG-UK")));
    	assertThat(orgBrands.get(10).toString(),is(equalTo("VW CV VWG-UK")));
    	assertThat(orgBrands.get(11).toString(),is(equalTo("VW PC VWG-UK")));
    }
    
	private void assertPermissionChanges(ModelAndView mav, Set<Function> grantedFunctions, Set<Function> deniedFunctions) {
    	
		@SuppressWarnings("unchecked")
		Set<Function> currentAllowedFunctions = (Set<Function>)ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "allowedFunctions", Set.class);
		
		assertTrue("all granted functions are present", currentAllowedFunctions.containsAll(grantedFunctions));
		
		assertTrue("no denied functions are present", Collections.disjoint(currentAllowedFunctions, deniedFunctions));
    }
    
    @SuppressWarnings("rawtypes")
	private void assertSharingRuleChanges(ModelAndView mav, Set<OrganisationBrand> targetOrgBrands) {
    	
		assertThat((Set)targetOrgBrands,
			is(equalTo(ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "targetOrgBrands", Set.class)))
		);
    }
    
	private void assertModifiedInLast10Seconds(ModelAndView mav, String modifiedBy, Date modifiedDate) {
		assertThat(modifiedBy,
			is(equalTo(ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "modifiedBy", String.class)))
		);
		long milliDiff = modifiedDate.getTime() - ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "modifiedDate", Date.class).getTime();
		assertThat(Math.abs(milliDiff), is(lessThan(1000L)));
    }
}
