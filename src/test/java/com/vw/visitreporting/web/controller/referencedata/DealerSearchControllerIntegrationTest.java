package com.vw.visitreporting.web.controller.referencedata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import com.vw.visitreporting.common.DbUtils;
import com.vw.visitreporting.entity.referencedata.Address;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.FranchiseGroup;
import com.vw.visitreporting.entity.referencedata.GeographicalArea;
import com.vw.visitreporting.entity.referencedata.GeographicalRegion;
import com.vw.visitreporting.entity.referencedata.GeographicalStructureCategory;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.enums.AddressType;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.security.SecurityUtils;
import com.vw.visitreporting.service.referencedata.DealershipService;
import com.vw.visitreporting.service.referencedata.FranchiseGroupService;
import com.vw.visitreporting.service.referencedata.GeographicalStructureService;
import com.vw.visitreporting.service.referencedata.OrganisationService;
import com.vw.visitreporting.web.ControllerUtil;
import com.vw.visitreporting.web.form.DealershipSearchForm;


/**
 * Performs an integration test using the DealershipController class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath:/application-contexts/applicationContext-core.xml",
	"classpath:/application-contexts/applicationContext-data.xml",
	"classpath:/application-contexts/applicationContext-services.xml",
	"classpath:/application-contexts/applicationContext-web.xml",
	"classpath:/application-contexts/applicationContext-security-authorisation.xml",
	"classpath:/application-contexts/applicationContext-security-authentication.xml"
	/* don't import security-authorisation configuration, to separate functional testing from authorisation rules */
	/* don't import the scheduling configuration, to stop scheduled tasks interfering with tests */
})
@TransactionConfiguration(defaultRollback=true)
@Transactional
public class DealerSearchControllerIntegrationTest {
	
	private final static int DEALER_COUNT = 1000;

	
	@Autowired
	private DealershipService dealershipService;
	
	@Autowired
	private FranchiseGroupService franchiseGroupService;
	
	@Autowired
	private GeographicalStructureService geographyService;

	@Autowired
	private OrganisationService organisationService;
	
	@Autowired
	private DbUtils dbUtils;
	
	@Autowired
	private SecurityUtils securityUtils;
	
	@Autowired
	private ControllerUtil controllerUtil;


	/**
	 * Load standard initial test data set before each test
	 * and ensure that all tests run as the standard test user. 
	 */
	@Before
	public void usualSetUp() {
		dbUtils.insertData(new String[] {
			"/dbUnitDealerData.xml"
		});
		
        securityUtils.loginUser("dealer_user_vwg");
	}

	/**
	 * Clear test data from database after each test and logout current user.
	 */
	@After
	public void usualTearDown() {
		securityUtils.logoutUser();
	}	
	
	
	
	/**
	 * Test that dealer search landing page has necessary data.
	 */
	@Test
    public void testSearchLandingPage() {

		ModelAndView mav = controllerUtil.handle("GET", "/dealership/search");
		
		ModelAndViewAssert.assertViewName(mav, "dealership/search");
		
		DealershipSearchForm formBean = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "command", DealershipSearchForm.class);
		
		assertThat(formBean.getDealershipNumber(), is(nullValue()));
		assertThat(formBean.getDealershipName(), is(nullValue()));
		assertThat(formBean.getFranchiseGroupName(), is(nullValue()));
		assertThat(formBean.getSearchResults(), is(nullValue()));
		assertThat(formBean.getPaging(), is(not(nullValue())));
	}
	
    /**
     * Test that a search with no criteria returns all dealerships.
     */
    @Test
    public void testSearchWithNoCriteria() {
    	
    	//test the search page with no criteria
    	DealershipSearchForm inForm = new DealershipSearchForm();
    	inForm.setDealershipNumber("");
    	inForm.setDealershipName("");
    	inForm.setFranchiseGroupName("");
    	
    	ModelAndView mav = handleRequest("POST", "/dealership/search", inForm);
    	
    	
		//check results
		ModelAndViewAssert.assertViewName(mav, "dealership/search");
		
		DealershipSearchForm outForm = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "command", DealershipSearchForm.class);
		
		assertThat(outForm.getDealershipNumber(), is(equalTo(inForm.getDealershipNumber())));
		assertThat(outForm.getDealershipName(), is(equalTo(inForm.getDealershipName())));
		assertThat(outForm.getFranchiseGroupName(), is(equalTo(inForm.getFranchiseGroupName())));
		
		assertThat(outForm.getSearchResults(), is(not(nullValue())));
		assertThat(outForm.getSearchResults().size(), is(greaterThanOrEqualTo(DEALER_COUNT)));
		
		//assert that all test dealerships were returned in search results
		Set<String> searchResultsdealerNames = dealerNamesFrom(outForm.getSearchResults());
		for(int i=1; i<=DEALER_COUNT; i++) {
			assertThat(searchResultsdealerNames, hasItem("Dealer"+i));
		}
    }
    

    /**
     * Test that a search with invalid dealer number criteria
     */
    @Test
    public void testSearchWithInvalidDealerNumberCriteria() {
    	
    	//test the search page with no criteria
    	DealershipSearchForm inForm = new DealershipSearchForm();
    	inForm.setDealershipNumber("abc");
    	inForm.setDealershipName("");
    	inForm.setFranchiseGroupName("");
    	
    	ModelAndView mav = handleRequest("POST", "/dealership/search", inForm);
    	
    	
		//check results
		ModelAndViewAssert.assertViewName(mav, "dealership/search");
		
		DealershipSearchForm outForm = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "command", DealershipSearchForm.class);
		
		assertThat(outForm.getDealershipNumber(), is(equalTo(inForm.getDealershipNumber())));
		assertThat(outForm.getDealershipName(), is(equalTo(inForm.getDealershipName())));
		assertThat(outForm.getFranchiseGroupName(), is(equalTo(inForm.getFranchiseGroupName())));
		
		BindingResult bindingResult = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "org.springframework.validation.BindingResult.command", BindingResult.class);
		assertThat(bindingResult.hasFieldErrors("dealershipNumber"), is(equalTo(true)));
    }

    
    /**
     * Test that a search with dealer number that is not in DB criteria
     */
    @Test
    public void testSearchWithNonExistingDealerNumberCriteria() {
    	
    	//test the search page with no criteria
    	DealershipSearchForm inForm = new DealershipSearchForm();
    	inForm.setDealershipNumber("99999");	//this dealer does not exist in the database
    	inForm.setDealershipName("");
    	inForm.setFranchiseGroupName("");
    	
    	ModelAndView mav = handleRequest("POST", "/dealership/search", inForm);
    	
    	
		//check results
		ModelAndViewAssert.assertViewName(mav, "dealership/search");
		
		DealershipSearchForm outForm = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "command", DealershipSearchForm.class);
		
		assertThat(outForm.getDealershipNumber(), is(equalTo(inForm.getDealershipNumber())));
		assertThat(outForm.getDealershipName(), is(equalTo(inForm.getDealershipName())));
		assertThat(outForm.getFranchiseGroupName(), is(equalTo(inForm.getFranchiseGroupName())));
		
		assertThat(outForm.getSearchResults(), is(not(nullValue())));
		assertThat(outForm.getSearchResults().size(), is(equalTo(0)));
    }
    
    
    /**
     * Test that a search with dealer number that does exist in DB criteria
     */
    @Test
    public void testSearchWithExistingDealerNumberCriteria() {
    	
    	//test the search page with no criteria
    	DealershipSearchForm inForm = new DealershipSearchForm();
    	inForm.setDealershipNumber("00001");	//this dealer does exist in the database
    	inForm.setDealershipName("");
    	inForm.setFranchiseGroupName("");
    	
    	ModelAndView mav = handleRequest("POST", "/dealership/search", inForm);
    	
    	
		//check results
		ModelAndViewAssert.assertViewName(mav, "dealership/search");
		
		DealershipSearchForm outForm = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "command", DealershipSearchForm.class);
		
		assertThat(outForm.getDealershipNumber(), is(equalTo(inForm.getDealershipNumber())));
		assertThat(outForm.getDealershipName(), is(equalTo(inForm.getDealershipName())));
		assertThat(outForm.getFranchiseGroupName(), is(equalTo(inForm.getFranchiseGroupName())));
		
		assertThat(outForm.getSearchResults(), is(not(nullValue())));
		Set<String> searchResultsdealerNames = dealerNamesFrom(outForm.getSearchResults());
		assertThat(searchResultsdealerNames, hasItem("Dealer1"));
    }    

    
    /**
     * Test that a search with dealer name that is not in DB criteria
     */
    @Test
    public void testSearchWithNonExistingDealerNameCriteria() {
    	
    	//test the search page with no criteria
    	DealershipSearchForm inForm = new DealershipSearchForm();
    	inForm.setDealershipNumber("");
    	inForm.setDealershipName("this doesnt exist");	//this dealer does not exist in the database
    	inForm.setFranchiseGroupName("");
    	
    	ModelAndView mav = handleRequest("POST", "/dealership/search", inForm);
    	
    	
		//check results
		ModelAndViewAssert.assertViewName(mav, "dealership/search");
		
		DealershipSearchForm outForm = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "command", DealershipSearchForm.class);
		
		assertThat(outForm.getDealershipNumber(), is(equalTo(inForm.getDealershipNumber())));
		assertThat(outForm.getDealershipName(), is(equalTo(inForm.getDealershipName())));
		assertThat(outForm.getFranchiseGroupName(), is(equalTo(inForm.getFranchiseGroupName())));
		
		assertThat(outForm.getSearchResults(), is(not(nullValue())));
		assertThat(outForm.getSearchResults().size(), is(equalTo(0)));
    }

    
    /**
     * Test that a search with dealer name that does exist in DB criteria
     */
    @Test
    public void testSearchWithExistingDealerNameCriteria() {
    	
    	//test the search page with no criteria
    	DealershipSearchForm inForm = new DealershipSearchForm();
    	inForm.setDealershipNumber("");
    	inForm.setDealershipName("Dealer1");	//this dealer does exist in the database
    	inForm.setFranchiseGroupName("");
    	
    	ModelAndView mav = handleRequest("POST", "/dealership/search", inForm);
    	
    	
		//check results
		ModelAndViewAssert.assertViewName(mav, "dealership/search");
		
		DealershipSearchForm outForm = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "command", DealershipSearchForm.class);
		
		assertThat(outForm.getDealershipNumber(), is(equalTo(inForm.getDealershipNumber())));
		assertThat(outForm.getDealershipName(), is(equalTo(inForm.getDealershipName())));
		assertThat(outForm.getFranchiseGroupName(), is(equalTo(inForm.getFranchiseGroupName())));
		
		assertThat(outForm.getSearchResults(), is(not(nullValue())));
		assertThat(outForm.getSearchResults().size(), is(equalTo(1)));
		
		Dealership result = outForm.getSearchResults().get(0);
		assertThat(result.getDealerName(), is(equalTo("Dealer1")));
    }    

    
    /**
     * Test that a search with wildcard dealer name criteria
     */
    @Test
    public void testSearchWithWildcardDealerNameCriteria() {
    	
    	//test the search page with no criteria
    	DealershipSearchForm inForm = new DealershipSearchForm();
    	inForm.setDealershipNumber("");
    	inForm.setDealershipName("D*ler1*");
    	inForm.setFranchiseGroupName("");
    	
    	ModelAndView mav = handleRequest("POST", "/dealership/search", inForm);
    	
    	
		//check results
		ModelAndViewAssert.assertViewName(mav, "dealership/search");
		
		DealershipSearchForm outForm = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "command", DealershipSearchForm.class);
		
		assertThat(outForm.getDealershipNumber(), is(equalTo(inForm.getDealershipNumber())));
		assertThat(outForm.getDealershipName(), is(equalTo(inForm.getDealershipName())));
		assertThat(outForm.getFranchiseGroupName(), is(equalTo(inForm.getFranchiseGroupName())));
		
		assertThat(outForm.getSearchResults(), is(not(nullValue())));
		assertThat(outForm.getSearchResults().size(), is(equalTo(112)));
		
		//assert that all test dealerships with number starting with 1 were returned in search results
		Set<String> searchResultsdealerNames = dealerNamesFrom(outForm.getSearchResults());
		
		assertThat(searchResultsdealerNames, hasItem("Dealer1"));		//dealer 1
		for(int i=10; i<=19; i++) {
			assertThat(searchResultsdealerNames, hasItem("Dealer"+i));	//dealer 10-19
		}
		for(int i=100; i<=199; i++) {
			assertThat(searchResultsdealerNames, hasItem("Dealer"+i));	//dealer 100-199
		}
		assertThat(searchResultsdealerNames, hasItem("Dealer1000"));		//dealer 1000
    }    

    
    /**
     * Test that a search with franchise group name that is not in DB criteria
     */
    @Test
    public void testSearchWithNonExistingFranchiseGroupNameCriteria() {
    	
    	//test the search page with no criteria
    	DealershipSearchForm inForm = new DealershipSearchForm();
    	inForm.setDealershipNumber("");
    	inForm.setDealershipName("");
    	inForm.setFranchiseGroupName("this doesnt exist");	//this franchise group does not exist in the database
    	
    	ModelAndView mav = handleRequest("POST", "/dealership/search", inForm);
    	
    	
		//check results
		ModelAndViewAssert.assertViewName(mav, "dealership/search");
		
		DealershipSearchForm outForm = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "command", DealershipSearchForm.class);
		
		assertThat(outForm.getDealershipNumber(), is(equalTo(inForm.getDealershipNumber())));
		assertThat(outForm.getDealershipName(), is(equalTo(inForm.getDealershipName())));
		assertThat(outForm.getFranchiseGroupName(), is(equalTo(inForm.getFranchiseGroupName())));
		
		assertThat(outForm.getSearchResults(), is(not(nullValue())));
		assertThat(outForm.getSearchResults().size(), is(equalTo(0)));
    }

    
    /**
     * Test that a search with franchise group name that does exist in DB criteria
     */
    @Test
    public void testSearchWithExistingFranchiseGroupNameCriteria() {
    	
    	//test the search page with no criteria
    	DealershipSearchForm inForm = new DealershipSearchForm();
    	inForm.setDealershipNumber("");
    	inForm.setDealershipName("");
    	inForm.setFranchiseGroupName("Group1");	//this franchise group does exist in the database
    	
    	ModelAndView mav = handleRequest("POST", "/dealership/search", inForm);
    	
    	
		//check results
		ModelAndViewAssert.assertViewName(mav, "dealership/search");
		
		DealershipSearchForm outForm = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "command", DealershipSearchForm.class);
		
		assertThat(outForm.getDealershipNumber(), is(equalTo(inForm.getDealershipNumber())));
		assertThat(outForm.getDealershipName(), is(equalTo(inForm.getDealershipName())));
		assertThat(outForm.getFranchiseGroupName(), is(equalTo(inForm.getFranchiseGroupName())));
		
		assertThat(outForm.getSearchResults(), is(not(nullValue())));
		assertThat(outForm.getSearchResults().size(), is(equalTo(1)));
		
		Dealership result = outForm.getSearchResults().get(0);
		assertThat(result.getFranchiseGroup().getName(), is(equalTo("Group1")));
    }    

    
    /**
     * Test that a search with wildcard franchise group name criteria
     */
    @Test
    public void testSearchWithWildcardFranchiseGroupNameCriteria() {
    	
    	//test the search page with no criteria
    	DealershipSearchForm inForm = new DealershipSearchForm();
    	inForm.setDealershipNumber("");
    	inForm.setDealershipName("");
    	inForm.setFranchiseGroupName("Gr*p1*");
    	
    	ModelAndView mav = handleRequest("POST", "/dealership/search", inForm);
    	
    	
		//check results
		ModelAndViewAssert.assertViewName(mav, "dealership/search");
		
		DealershipSearchForm outForm = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "command", DealershipSearchForm.class);
		
		assertThat(outForm.getDealershipNumber(), is(equalTo(inForm.getDealershipNumber())));
		assertThat(outForm.getDealershipName(), is(equalTo(inForm.getDealershipName())));
		assertThat(outForm.getFranchiseGroupName(), is(equalTo(inForm.getFranchiseGroupName())));
		
		assertThat(outForm.getSearchResults(), is(not(nullValue())));
		assertThat(outForm.getSearchResults().size(), is(equalTo(112)));
		
		//assert that all test dealerships where group name starts with 1 were returned in search results
		Set<String> searchResultsdealerNames = dealerNamesFrom(outForm.getSearchResults());
		
		assertThat(searchResultsdealerNames, hasItem("Dealer1"));		//dealer 1
		for(int i=10; i<=19; i++) {
			assertThat(searchResultsdealerNames, hasItem("Dealer"+i));	//dealer 10-19
		}
		for(int i=100; i<=199; i++) {
			assertThat(searchResultsdealerNames, hasItem("Dealer"+i));	//dealer 100-199
		}
		assertThat(searchResultsdealerNames, hasItem("Dealer1000"));	//dealer 1000
    }    

    
    /**
     * Test that a search with multiple search criteria
     */
    @Test
    public void testSearchWithMultipleCriteria() {
    	
    	//test the search page with no criteria
    	DealershipSearchForm inForm = new DealershipSearchForm();
    	inForm.setDealershipNumber("1");
    	inForm.setDealershipName("Dealer1");
    	inForm.setFranchiseGroupName("Gr*p1*");
    	
    	ModelAndView mav = handleRequest("POST", "/dealership/search", inForm);
    	
    	
		//check results
		ModelAndViewAssert.assertViewName(mav, "dealership/search");
		
		DealershipSearchForm outForm = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "command", DealershipSearchForm.class);
		
		assertThat(outForm.getDealershipNumber(), is(equalTo(inForm.getDealershipNumber())));
		assertThat(outForm.getDealershipName(), is(equalTo(inForm.getDealershipName())));
		assertThat(outForm.getFranchiseGroupName(), is(equalTo(inForm.getFranchiseGroupName())));
		
		assertThat(outForm.getSearchResults(), is(not(nullValue())));
		assertThat(outForm.getSearchResults().size(), is(equalTo(1)));
		
		Dealership result = outForm.getSearchResults().get(0);
		assertThat(result.getDealerName(), is(equalTo("Dealer1")));
    }    

    
    
    /**
     * Helper method to simulate a request to the DealershipController and verify that it
     * returns within the time specified by the non-functional requirements (5 secs).
     * @param method - the HTTP method of the request (GET | POST)
     * @param url - the url of the request to simulate
     * @param inForm - the form bean to include in the request
     * @return the ModelAndView object created by Spring MVC as it performed the request
     */
    private ModelAndView handleRequest(String method, String url, DealershipSearchForm inForm) {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("dealershipNumber", inForm.getDealershipNumber());
    	params.put("dealershipName", inForm.getDealershipName());
    	params.put("franchiseGroupName", inForm.getFranchiseGroupName());

    	//time the request
    	Date startTime = new Date();

    	ModelAndView mav = controllerUtil.handle(method, url, params);
    	
    	//check response time is less than 5 secs
    	long responseTimeInMillis = (new Date().getTime() - startTime.getTime());
    	assertThat(responseTimeInMillis, is(lessThan(5000L)));
    	
    	return mav;
    }
    
    /**
     * Helper method to get a set of names of dealers from search results for easier assertions.
     */
    private Set<String> dealerNamesFrom(List<Dealership> searchResults) {
    	HashSet<String> dealerNames = new HashSet<String>();
    	for(Dealership dealer : searchResults) {
    		dealerNames.add(dealer.getDealerName());
    	}
    	return dealerNames;
    }
    
    /**
     * Create the test data for this test and output it to a DBUnit xml file.
     * @return the collection of Dealership entities that were created.
     */
	private Dealership[] setupTestData() {

    	Map<String, GeographicalArea> areaMap = setupGeographicalStructure();
    	Dealership[] dealers = new Dealership[DEALER_COUNT];
    	for(int i=0; i<dealers.length; i++) {
    		dealers[i] = insertDealership(i+1, areaMap);
    	}

    	dbUtils.doDatabaseDump(new File("c:\\temp\\dbUnit.xml"));
    	
    	return dealers;
	}    
    
    /**
     * Inserts a dealer into the database with all EAGER properties set to realistic values.
     * @param index - the dealer number of the dealer to create
     * @param areaMap - the geographical structure hierarchy with which dealers will be associated
     * @return the Dealership entity that was created and persisted by this method
     */
    private Dealership insertDealership(int number, Map<String, GeographicalArea> areaMap) {

    	FranchiseGroup group = new FranchiseGroup();
    	group.setName("Group"+number);
    	group.setBrands(EnumSet.of(Brand.AUDI));
    	group.setWebsiteLink("http://max/");
    	group.setWebsiteLabel("MAX");
    	
    	group = franchiseGroupService.save(group);    	
    	
    	Dealership dealer = new Dealership();
    	dealer.setDealerNumber(number);
    	dealer.setDealerName("Dealer"+number);
    	dealer.setBrand(Brand.AUDI);
    	dealer.setFranchiseGroup(group);
    	dealer.setMainlineTelephone("123");
    	dealer.setFaxNumber("456");
    	dealer.setWebsiteLabel("MAX");
    	dealer.setWebsiteLink("http://max/");
    	
    	Address addr1 = new Address(
    			AddressType.DELIVERY,
    			1, null, "high street", null, "Milton Keynes", null, "Bucks", "postCode", "UK"
    		);
    	Address addr2 = new Address(
    			AddressType.REGISTERED_OFFICE,
    			2, null, "high street", null, "Milton Keynes", null, "Bucks", "postCode", "UK"
    		);
    	Address addr3 = new Address(
    			AddressType.MAIN_TRADING,
    			3, null, "high street", null, "Milton Keynes", null, "Bucks", "postCode", "UK"
    		);
    	HashSet<Address> addrs = new HashSet<Address>();
    	addrs.addAll(Arrays.asList(addr1, addr2, addr3));
    	dealer.setAddresses(addrs);
    	dealer.setTradingAddress(addr3);

    	//choose a geographic area from each category to associate to this dealer
    	int regionIndex = (int)Math.floor((double)number / 5.0) % 5;
    	int areaIndex = number % 5;
    	HashSet<GeographicalArea> areas = new HashSet<GeographicalArea>();
    	for(int i=0; i<4; i++) {
    		areas.add( areaMap.get(i+"_"+regionIndex+"_"+areaIndex) );
    	}
    	dealer.setGeographicalAreas(areas);

    	dealer = dealershipService.save(dealer);

    	group.setDealerships(Collections.singleton(dealer));
    	
    	return dealer;
    }
    
    /**
     * Generates a geographic structure hierarchy consisting of 4 geographic categories, each
     * with 5 regions, which each have 5 areas.
     * @return the geographic categories that were created and persisted in this method.
     */
    private Map<String, GeographicalArea> setupGeographicalStructure() {
    	
    	Organisation org = organisationService.getOrganisation(0);
    	
    	Map<String, GeographicalArea> areaMap = new HashMap<String, GeographicalArea>();
    	
    	for(int cat=0; cat<4; cat++) {
    		
    		GeographicalStructureCategory geogCategory = new GeographicalStructureCategory();
    		geogCategory.setName("geogCat"+cat);
    		geogCategory.setIsDefault(cat == 0);
    		geogCategory.setOrganisation(org);
    		geogCategory.setBrand(Brand.AUDI);
    		
    		geogCategory = geographyService.saveCategory(geogCategory);
	
    		GeographicalRegion[] regions = new GeographicalRegion[5];
			for(int reg=0; reg<regions.length; reg++) {
				
				regions[reg] = new GeographicalRegion();
				regions[reg].setDescription("region"+reg);
				regions[reg].setGeographicalStructureCategory(geogCategory);

				regions[reg] = geographyService.saveRegion(regions[reg]);

				GeographicalArea[] areas = new GeographicalArea[5];
				for(int area=0; area<areas.length; area++) {
					
					areas[area] = new GeographicalArea();
					areas[area].setDescription("area"+reg+"_"+area);
					areas[area].setGeographicalRegion(regions[reg]);

					areas[area] = geographyService.saveArea(areas[area]);
					
					areaMap.put(cat+"_"+reg+"_"+area, areas[area]);
				}
				HashSet<GeographicalArea> areaSet = new HashSet<GeographicalArea>();
				areaSet.addAll(Arrays.asList(areas));
				regions[reg].setGeographicalAreas(areaSet);
			}
			HashSet<GeographicalRegion> regionSet = new HashSet<GeographicalRegion>();
			regionSet.addAll(Arrays.asList(regions));
			geogCategory.setGeographicalRegions(regionSet);
    	}
    	return areaMap;
    }
}
