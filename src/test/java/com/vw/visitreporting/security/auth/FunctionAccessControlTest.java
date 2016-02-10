package com.vw.visitreporting.security.auth;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.vw.visitreporting.entity.accesscontrolled.AccessControlled;
import com.vw.visitreporting.entity.referencedata.UserProfile;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.security.SecurityUtils;
import com.vw.visitreporting.web.common.AccessCheckUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath:/application-contexts/applicationContext-core.xml",
	"classpath:/application-contexts/applicationContext-web.xml",
	"classpath:/application-contexts/applicationContext-data.xml",
	"classpath:/application-contexts/applicationContext-services.xml",
	"classpath:/application-contexts/applicationContext-security-authentication.xml",
	"classpath:/application-contexts/applicationContext-security-authorisation.xml"
})
@TransactionConfiguration(defaultRollback=true)
@Transactional
public class FunctionAccessControlTest{

	private AccessControlHandler accessControlHandler;    

	protected SecurityUtils securityUtils;

    
	@Test
    public void testFunctionAccessSuccessScenario(){
		securityUtils.loginUser("test_user");

		assertTrue(accessControlHandler.hasPermission(null,null, Function.EDIT_DEALER.name()));
    }
    
	@Test
    public void testFunctionAccessFailureScenario(){
		securityUtils.loginUser("test_user");

		assertFalse(accessControlHandler.hasPermission(null,null, Function.EDIT_ACTION.name()));
    }
	
	
	@Test
    public void testFunctionAccessGrantedRightsSuccessScenario(){
		securityUtils.loginUser("test_user");

		assertTrue(accessControlHandler.hasPermission(null, null, Function.EDIT_AGENDA.name()));
    }
	
	@Test
    public void testFunctionAccessGrantedRightsFailureScenario(){
		securityUtils.loginUser("test_user");

		assertFalse(accessControlHandler.hasPermission(null,null, Function.EDIT_VISIT_REPORT.name()));
    }
	
	
	@Test
    public void testFunctionAccessSuccessScenarioFromWebPage(){
		securityUtils.loginUser("test_user");
		assertTrue(AccessCheckUtil.canUserAccessFunction("EDIT_DEALER"));
    }
    
	@Test
    public void testFunctionAccessFailureScenarioFromWebPage(){
		securityUtils.loginUser("test_user");

		assertFalse(AccessCheckUtil.canUserAccessFunction("EDIT_ACTION"));
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
