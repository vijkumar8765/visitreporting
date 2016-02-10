package com.vw.visitreporting.security;//NOPMD

import static org.junit.Assert.fail;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import com.google.common.base.Joiner;
import com.vw.visitreporting.common.DbUtils;
import com.vw.visitreporting.entity.referencedata.BusinessArea;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.FranchiseGroup;
import com.vw.visitreporting.entity.referencedata.GeographicalArea;
import com.vw.visitreporting.entity.referencedata.GeographicalRegion;
import com.vw.visitreporting.entity.referencedata.GeographicalStructureCategory;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.Positions;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.UserProfile;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.referencedata.enums.Level;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;
import com.vw.visitreporting.service.referencedata.BusinessAreaService;
import com.vw.visitreporting.service.referencedata.DealershipService;
import com.vw.visitreporting.service.referencedata.FranchiseGroupService;
import com.vw.visitreporting.service.referencedata.OrganisationService;
import com.vw.visitreporting.service.referencedata.UserProfileService;


/**
 * Provides common functionality for all authorisation integration tests.
 * This ensures that all integration tests are tested in a common environment
 * that resembles the production environment and provides common routines.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath:/application-contexts/applicationContext-core.xml",
	"classpath:/application-contexts/applicationContext-data.xml",
	"classpath:/application-contexts/applicationContext-security-authentication.xml",
	"classpath:/application-contexts/applicationContext-security-authorisation.xml"
	/* don't import scheduling,services,web configuration */
})
@TransactionConfiguration(defaultRollback=true)
@Transactional
public abstract class AbstractSecurityTest {//NOPMD

	//collection of entites that will be associated to each user to dictate the user's permissions
	protected final Map<String, Organisation> organisations = new HashMap<String, Organisation>();
	protected final Map<String, EnumSet<Brand>> brandSets = new HashMap<String, EnumSet<Brand>>();
	protected final Map<String, UserProfile> userProfiles = new HashMap<String, UserProfile>();
	protected final Map<String, Set<BusinessArea>> busAreaSets = new HashMap<String, Set<BusinessArea>>();
	protected final Map<String, Set<Dealership>> dealerSets = new HashMap<String, Set<Dealership>>();
	protected final Map<String, Set<FranchiseGroup>> groupSets = new HashMap<String, Set<FranchiseGroup>>();
	protected final Map<String, GeographicalArea> areas = new HashMap<String, GeographicalArea>();
	protected final Map<String, Set<GeographicalArea>> areaSets = new HashMap<String, Set<GeographicalArea>>();
	protected final Map<String, GeographicalRegion> regions = new HashMap<String, GeographicalRegion>();
	protected final Map<String, Set<GeographicalRegion>> regionSets = new HashMap<String, Set<GeographicalRegion>>();
	protected GeographicalStructureCategory geogCategory;
	
	//collection of users to test each system function against
	protected final List<User> testUsers = new ArrayList<User>();
	
	
    @Autowired
    protected DbUtils dbUtils;

    @Autowired
	protected SecurityUtils securityUtils;

	@Autowired
	protected BusinessAreaService businessAreaService;
	
	@Autowired
	protected OrganisationService organisationService;

	@Autowired
	protected UserProfileService userProfileService;

	@Autowired
	protected DealershipService dealershipService;

	@Autowired
	protected FranchiseGroupService franchiseGroupService;

	@Autowired
	private HibernateTransactionManager transactionManager;
	
	
	/**
	 * Setup a collection of test users to test each system function against.
	 * Only do this once, not for all tests.
	 */
	@Before
	public void setupUsersIfNotAlreadyDone() throws IOException, ParseException {
		securityUtils.loginUser("test_user");	//required to insert any data necessary for the test
		
		if(testUsers.isEmpty()) {
			setupGeogStructure();
			setupUsers();
		}
	}
	
	/**
	 * Sub-classes of this class are able to grant permissions to user profile and
	 * organisations and brands to test various scenarios. This method clears all
	 * permissions that have been granted to any user profiles or organisations after each test.
	 */
	@After
	public void clearAllGrants() {
		
		//access may have been granted to one or more user profiles during the test, so revoke it now
		for(UserProfile userProfile : userProfiles.values()) {
			userProfile.getAllowedFunctions().clear();
		}

		securityUtils.logoutUser();
	}
	
	
	/**
	 * Run a given function repeatedly as each of the test users (logged into the system).
	 * Always rolls back transaction after each function.
	 * Fails the test if the user is able to perform a function which they should not be authorised
	 * to perform, or if they are not able to perform a function which they should be able to perform.
	 * @param shouldBeAuthorised - a mapping of users to booleans to lookup whether each user should be able to perform the supplied function. 
	 * @param function - a function that performs a set of restricted functions to test authorisation for
	 */
	protected void runForEachTestUser(final Map<User, Boolean> shouldBeAuthorised, final Runnable function) {
		
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		
		//run function for each test user
		for(final User user : testUsers) {
			
			securityUtils.loginUser(user);

			//run the method in a new transaction each time
			String result = transactionTemplate.execute(new TransactionCallback<String>() {
				public String doInTransaction(TransactionStatus status) {

					//always roll back the transaction
					status.setRollbackOnly();
					
					try {
						function.run();
						
						//fail if user was able to perform function when they shouldn't have been able to
						if( ! shouldBeAuthorised.get(user)) {
							return user.getId()+" (org="+user.getOrganisation().getName()+", brand="+Joiner.on(",").join(user.getBrands())+", profile="+Joiner.on(",").join(user.getUserProfiles())+") should NOT be authorised to perform this operation";
						}
						
					} catch(AccessDeniedException err) {

						//fail if user was not able to perform function when they should have been
						if(shouldBeAuthorised.get(user)) {
							return user.getId()+" (org="+user.getOrganisation().getName()+", brand="+Joiner.on(",").join(user.getBrands())+", profile="+Joiner.on(",").join(user.getUserProfiles())+") SHOULD be authorised to perform this operation";
						}
					}
					//no authorisation errors no return no error message
					return null;
				}
			});
			
			//if an error message was returned, fail the test with that message
			if(result != null) {
				fail(result);
			}
		}
	}
	
	/**
	 * Helper function to grant access to a system function to a user profile.
	 * @param profileName - the name of the user profile (must match a user profile from user.tsv)
	 * @param func - the system function to grant to the user profile
	 */
	protected void grantSystemFunctionToProfile(String profileName, Function func) {
		UserProfile profile = userProfiles.get(profileName);

		Set<Function> permissions = EnumSet.copyOf(profile.getAllowedFunctions());
		permissions.add(func);
		
		userProfileService.updatePermissions(profile.getId(), permissions);
	}
	
	/**
	 * Helper function to check if a user has a given user profile.
	 */
	protected boolean userHasProfile(User user, String profileName) {
		for(UserProfile profile : user.getUserProfiles()) {
			if(profileName.equals(profile.getName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Setup a test geographical structure with area and region names corresponding to
	 * those in the user.tsv file in resources.
	 */
	private void setupGeogStructure() {
		
		geogCategory = new GeographicalStructureCategory();
		geogCategory.setName("testGeogCat");

		GeographicalArea area1 = new GeographicalArea();
		area1.setId(-1);
		area1.setDescription("area1");
		GeographicalArea area2 = new GeographicalArea();
		area2.setId(-2);
		area2.setDescription("area2");

		GeographicalRegion reg1 = new GeographicalRegion();
		reg1.setId(-1);
		reg1.setDescription("region1");
		GeographicalRegion reg2 = new GeographicalRegion();
		reg2.setId(-2);
		reg2.setDescription("region2");

		area1.setGeographicalRegion(reg1);
		area2.setGeographicalRegion(reg2);
		reg1.setGeographicalAreas(Collections.singleton(area1));
		reg2.setGeographicalAreas(Collections.singleton(area2));
		reg1.setGeographicalStructureCategory(geogCategory);
		reg2.setGeographicalStructureCategory(geogCategory);
		
		HashSet<GeographicalRegion> catRegions = new HashSet<GeographicalRegion>();
		catRegions.addAll(Arrays.asList(reg1, reg2));
		geogCategory.setGeographicalRegions(catRegions);

		areas.put(area1.getDescription(), area1);
		areas.put(area2.getDescription(), area2);
		regions.put(reg1.getDescription(), reg1);
		regions.put(reg2.getDescription(), reg2);
	}
	
	/**
	 * Read details of all possible permutations of user within the system from user.tsv
	 * in resources and populates testUsers with a User object for each row in the TSV file.
	 */
	private void setupUsers() throws IOException, ParseException {//NOPMD
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(AbstractSecurityTest.class.getResourceAsStream("/users.tsv")));
			
			String line = reader.readLine();	//header
			int lineCount = 1;
			while( (line = reader.readLine()) != null) {
				lineCount++;
				
				//create user for each line of the file
				String[] cols = line.split("\t", -1);
				
				if(cols.length != 9) {
					throw new ParseException("Expected 9 columns on line "+lineCount+" but found "+cols.length, lineCount);
				}
				
				
				//get user associations
				Organisation organisation = organisations.get(cols[0]);
				if(organisation == null) {
					organisation = organisationService.findByName(cols[0]);
					organisations.put(cols[0], organisation);
				}
				
				EnumSet<Brand> brands = brandSets.get(cols[1]);
				if(brands == null) {
					if("All".equals(cols[1])) {
						brands = EnumSet.allOf(Brand.class);
					} else {
						brands = EnumSet.of(Brand.valueOf(cols[1]));
					}
					brandSets.put(cols[1], brands);
				}
				
				UserProfile profile = userProfiles.get(cols[2]);
				if(profile == null) {
					profile = new UserProfile();
					profile.setName(cols[2]);
					profile.setAllowedFunctions(new HashSet<Function>());
					userProfiles.put(cols[2], profile);
					userProfileService.save(profile);
				}

				Level level = EnumUtil.getEnumObject(Integer.parseInt(cols[3]), Level.class);

				Set<BusinessArea> busAreas = busAreaSets.get(cols[4]);
				if(busAreas == null) {
					busAreas = new HashSet<BusinessArea>();
					for(String busArea : cols[4].split(",")) {
						busAreas.add(businessAreaService.findByName(busArea));
					}
					busAreaSets.put(cols[4], busAreas);
				}

				Set<GeographicalRegion> userRegions = regionSets.get(cols[5]);
				if(userRegions == null) {
					userRegions = new HashSet<GeographicalRegion>();
					if( ! cols[5].isEmpty()) {
						for(String regionName : cols[5].split(",", 0)) {
							if( ! regions.containsKey(regionName)) {
								throw new IllegalArgumentException("region "+regionName+" not found");
							}
							userRegions.add(regions.get(regionName));
						}
					}
					regionSets.put(cols[5], userRegions);
				}

				Set<GeographicalArea> userAreas = areaSets.get(cols[6]);
				if(userAreas == null) {
					userAreas = new HashSet<GeographicalArea>();
					if( ! cols[6].isEmpty()) {
						for(String areaName : cols[6].split(",", 0)) {
							if( ! areas.containsKey(areaName)) {
								throw new IllegalArgumentException("area "+areaName+" not found");
							}
							userAreas.add(areas.get(areaName));
						}
					}
					areaSets.put(cols[6], userAreas);
				}

				Set<FranchiseGroup> groups = groupSets.get(cols[7]);
				if(groups == null) {
					groups = new HashSet<FranchiseGroup>();
					for(String groupName : cols[7].split(",")) {
						if(groupName.length() > 0) {
							groups.add(franchiseGroupService.findByName(groupName));
						}
					}
					groupSets.put(cols[7], groups);
				}
				
				Set<Dealership> dealers = dealerSets.get(cols[8]);
				if(dealers == null) {
					dealers = new HashSet<Dealership>();
					for(String dealerNumber : cols[8].split(",")) {
						if(dealerNumber.length() > 0) {
							dealers.add(dealershipService.findByNumber(Integer.parseInt(dealerNumber)));
						}
					}					
					dealerSets.put(cols[8], dealers);
				}
				
				
				if(dealers.size() > 1) {
					continue;				//not currently able to assign responsibility for multiple dealers to a user
				}
				if(groups.size() > 1) {
					continue;				//not currently able to assign responsibility for multiple franchise groups to a user
				}
				
				
				//create user
				User user = new User();
				user.setUserId("User"+(lineCount - 1));
				user.setFirstName(user.getUserId());
				user.setUserProfiles(new HashSet<UserProfile>());
				user.getUserProfiles().add(profile);
				user.setPositions(new Positions());
				user.getPositions().setLevel(level);
				user.getPositions().setOrganisation(organisation);
				user.getPositions().setBrands(brands);
				user.setOrganisation(organisation);
				user.setBrands(brands);
				if( ! dealers.isEmpty()) {
					user.setDealership(dealers.iterator().next());	//get first dealership
				}
				if( ! groups.isEmpty()) {
					user.setFranchiseGroup(groups.iterator().next());	//get first franchise group
				}
				user.setGeographicalAreas(userAreas);
				user.setGeographicalRegions(userRegions);
				user.setGeographicalStructureCategories(Collections.singleton(geogCategory));
				
				testUsers.add(user);
			}
			
		} finally {
			try {
				reader.close();
			} catch(IOException err) {
				reader = null;
			}
		}
	}
}
