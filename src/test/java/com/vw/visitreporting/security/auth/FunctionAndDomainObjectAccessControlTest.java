package com.vw.visitreporting.security.auth;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.security.SecurityUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath:/application-contexts/applicationContext-core.xml",
	"classpath:/application-contexts/applicationContext-data.xml",
	"classpath:/application-contexts/applicationContext-services.xml",
	"classpath:/application-contexts/applicationContext-security-authentication.xml",
	"classpath:/application-contexts/applicationContext-security-authorisation.xml"
})
@TransactionConfiguration(defaultRollback=true)
@Transactional
public class FunctionAndDomainObjectAccessControlTest{ 
	
	private AccessControlHandler accessControlHandler;
	 
	private SecurityUtils securityUtils;
   
	@Test
    public void testGetAllowedFunctionsOnObjectsSuccessScenario(){
		securityUtils.loginUser("test_user");
		
		//This is a test whether retrieval of allowed functions for passed in objects works as expected
		
		//step 1: create argument collection of objects whose accesses need to be collected and tested
		//step 2: invoke accessControlHandler.getAllowedFunctionsOnObjects
		//step 3: assert that the retrieved results are as per the state in the database
		
		//step 1
		Collection<Object> objectsWhoseAllowedFunctionsNeedToBeRetrieved = new ArrayList<Object>();
		
		//lets fill the above collection with disparate objects
		
		//brand with id 2
		objectsWhoseAllowedFunctionsNeedToBeRetrieved.add(Brand.SEAT);
		
		//business area
		
		//Collection of similar type objects which can be access controlled
		
		//step 2
		Map allowedFunctionsOnObjects = accessControlHandler.getAllowedFunctionsOnObjects(objectsWhoseAllowedFunctionsNeedToBeRetrieved);
		
		//step 3
		
		Set<Function> functionsAllowedOnSeat = (Set<Function>)allowedFunctionsOnObjects.get(Brand.SEAT);
		Assert.assertTrue(functionsAllowedOnSeat.size() == 0);
		//TestingUtil.assertCollectionContainsFunctions(functionsAllowedOnSeat,new Function[]{Function.CREATE_BUSINESS_AREA, Function.CREATE_USER_PROFILE} );	
    }
	
	@Test
    public void testFunctionAndDomainAccessSuccessScenario(){
		securityUtils.loginUser("test_user");
		assertTrue(accessControlHandler.hasPermission(null, Brand.SKODA, Function.EDIT_DEALER.name()));	
    }

	@Test
    public void testFunctionAndDomainAccessSuccessScenarioWithFunctionNull(){
		securityUtils.loginUser("test_user");
		
		assertTrue(accessControlHandler.hasPermission(null, Brand.SKODA, null));	
    }
	
	
	@Test
    public void testFunctionAndDomainAccessFailureScenario(){
		securityUtils.loginUser("test_user");

		assertFalse(accessControlHandler.hasPermission(null, Brand.SEAT, Function.EDIT_DEALER.name()));
    }
	
	@Test
    public void testFunctionAndDomainAccessFailureScenario2(){
		securityUtils.loginUser("test_user");
		
		List<Brand> brands = new ArrayList<Brand> ();
		brands.add(Brand.SEAT);

		assertFalse(accessControlHandler.hasPermission(null, brands, Function.EDIT_DEALER.name()));
    }
    
	@Test
    public void testFunctionAndDomainAccessSuccessScenario2(){
		securityUtils.loginUser("test_user");
		
		List<Brand> brands = new ArrayList<Brand> ();
		brands.add(Brand.SKODA);
		assertTrue(accessControlHandler.hasPermission(null, brands, Function.EDIT_DEALER.name()));
    }
	
	@Test
    public void testAllowedFunctionsOnObjects(){
		securityUtils.loginUser("test_user");

		Collection<Object> objectsForWhichAccessNeedToBeRetrieved = new HashSet<Object>();
		objectsForWhichAccessNeedToBeRetrieved.add(Brand.SKODA);
		objectsForWhichAccessNeedToBeRetrieved.add(Brand.SEAT);
		
		Map<Object, Set<Function>> allowedFunctionMap = accessControlHandler.getAllowedFunctionsOnObjects(objectsForWhichAccessNeedToBeRetrieved);
		
		assertTrue(allowedFunctionMap.get(Brand.SKODA).size() >= 1);
		assertTrue(allowedFunctionMap.get(Brand.SEAT).size() == 0);
	}	
	

	@Autowired
	public void setAccessControlHandler(AccessControlHandler accessControlHandler) {
		this.accessControlHandler = accessControlHandler;
	}

	@Autowired
	public void setSecurityUtils(SecurityUtils securityUtils) {
		this.securityUtils = securityUtils;
	}


}
