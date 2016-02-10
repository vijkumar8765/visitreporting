package com.vw.visitreporting.web;

import static com.vw.visitreporting.CustomMatchers.containsText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.ModelAndViewAssert;
import java.io.File;
import java.util.Date;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.vw.visitreporting.logging.LogConfigManagerTest;


/**
 * Performs a test of the fault barrier by testing its response to an Exception.
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
public class FaultBarrierTest {

	@Autowired
	private ApplicationContext context;
	
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    
    /**
     * Test that any Exceptions thrown by a controller (or any layer below) are logged
     * and that the client is redirected to a user-friendly error page.
     */
    @Test
    public void testExceptionsGetLoggedAndRedirectToErrorPage() {
    	
    	//get all HandlerExceptionResolver instances that are registers in the web application context
    	Map<String, HandlerExceptionResolver> resolvers = context.getBeansOfType(HandlerExceptionResolver.class);
    	
		//configure logging to known log file location and error level
		File logFile = LogConfigManagerTest.setLogLevelTo("error", tempFolder);

		//simulate a request that will throw an exception
    	String errMsg = "--FAULT_BARRIER_TEST--"+new Date().getTime()+"--";
    	Exception err = new Exception(errMsg);

    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpServletResponse response = new MockHttpServletResponse();

        request.setMethod("GET");
        request.setRequestURI("/somePageThatThrowsAnException");
    	
    	//loop through exception resolvers until one handles it
    	ModelAndView mav = null;
    	for(HandlerExceptionResolver resolver : resolvers.values()) {
    		mav = resolver.resolveException(request, response, null, err);
    		if(mav != null) {
    			break;
    		}
    	}
    	assertThat(mav, is(not(nullValue())));
        
        //check that request was redirected to error page
        ModelAndViewAssert.assertViewName(mav, "error");
            
		//check that error was logged
		assertThat(logFile,containsText(errMsg));
    }
}
