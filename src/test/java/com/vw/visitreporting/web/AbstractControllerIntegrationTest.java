package com.vw.visitreporting.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import com.vw.visitreporting.AbstractIntegrationTest;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Function;


/**
 * Provides common functionality for Controller integration tests using a test Spring
 * application context and DB-Unit to setup the database for each test.
 */
public abstract class AbstractControllerIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	protected AnnotationMethodHandlerAdapter handlerAdapter;
	
	@Autowired
	protected ApplicationContext context;

	private static boolean testDataLoaded = false;
	

	/**
	 * Load any DBUnit datasets required for this test class before any tests run.
	 * The data will only be inserted once per test run.
	 * Subclasses should specify which files are required by overriding the
	 * getDbUnitDataFiles() method.
	 */
	@Before
	public void setupDbUnit() {
		if( ! testDataLoaded) {
			super.dbUtils.insertData(getDbUnitDataFiles());
			testDataLoaded = true;
		}
	}
	
	/**
	 * Subclasses should overide this method to specify which DBUnit dataset files
	 * should be loaded before test in this class run. The file will only be imported
	 * into the database once and the data will not be removed afterwards.
	 * @return a collection of resource locations of DBUnit dataset files, e.g. "/myData.xml"
	 */
	protected String[] getDbUnitDataFiles() {
		return new String[0];
	}

	
	/**
     * This is a helper method which invokes the Spring MVC framework and returns the
     * ModelAndView from a controller method call.
     * @param method - the HTTP method to simulate, e.g. GET or POST
     * @param requestUrl - the request URI to simulate
     */
    protected ModelAndView handle(String method, String requestUrl) {
    	
    	Map<String, Object> params = null;
    	int pos = requestUrl.indexOf('?');
    	if(pos > 0) {
    		params = new HashMap<String, Object>();
    		String[] keyValPairs = requestUrl.substring(pos+1).split("&");
    		for(String keyValPair : keyValPairs) {
    			String[] tokens = keyValPair.split("=");
    			params.put(tokens[0], tokens[1]);
    		}
    		requestUrl = requestUrl.substring(0, pos);
    	}
    	
    	return this.handle(method, requestUrl, params);
    }

    /**
     * This is a helper method which invokes the Spring MVC framework and returns the
     * ModelAndView from a controller method call.
     * @param method - the HTTP method to simulate, e.g. GET or POST
     * @param requestUrl - the request URI to simulate
     */
    protected ModelAndView handle(String method, String requestUrl, Map<String, Object> params) {
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpServletResponse response = new MockHttpServletResponse();

        request.setMethod(method);
        request.setRequestURI(requestUrl);
        if(params != null) {
        	request.setParameters(params);
        }

        return handle(request, response);
    }

	/**
     * This is a helper method which invokes the Spring MVC framework and returns the
     * ModelAndView from a controller method call.
     * @param request - an HTTP request simulating a request from a client browser
     * @param response - an HTTP response simulating the response to the client browser
     */
    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		//get HandlerExecutionChain based on application context
    		Map<String, HandlerMapping> matchingMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
    		List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>(matchingMappings.values());
			OrderComparator.sort(handlerMappings);
			HandlerExecutionChain handler = null;
			for(HandlerMapping hm : handlerMappings) {
				handler = hm.getHandler(request);
				if(handler != null) {
					break;
				}
			}

	        Object controller = handler.getHandler();

			//get handler adapter
	        /*
			Map<String, HandlerAdapter> matchingAdapters = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerAdapter.class, true, false);
			List<HandlerAdapter> handlerAdapters = new ArrayList<HandlerAdapter>(matchingAdapters.values());
			OrderComparator.sort(handlerAdapters);
			HandlerAdapter handlerAdapter = null;
			for(HandlerAdapter ha : handlerAdapters) {
				if(ha.supports(controller)) {
					handlerAdapter = ha;
					break;
				}
			}
			*/
			
    		//HandlerExecutionChain handler = handlerMapping.getHandler(request);
	        Assert.assertNotNull("No handler found for request, check you request mapping", handler);
	
	
	        HandlerInterceptor[] interceptors = handler.getInterceptors();
	        for (HandlerInterceptor interceptor : interceptors) {
	            boolean carryOn = interceptor.preHandle(request, response, controller);
	            if (!carryOn) {
	                return null;
	            }
	        }
	
	        ModelAndView mav = handlerAdapter.handle(request, response, controller);
	        //the code doesn't invoke appropriate interceptors
// TODO the follwong code needs to be removed
	        mav.getModelMap().addObject("brand", Brand.SKODA);

			for(HandlerMapping hm : handlerMappings) {
				handler = hm.getHandler(request);
				if(handler != null) {
					
			        HandlerInterceptor[] interceptors2 = handler.getInterceptors();
			        for (HandlerInterceptor interceptor : interceptors2) {
			            interceptor.postHandle(request, response, handlerAdapter, mav);
			        }				
				}
			}
			Map<Object, Set<Function>> mapOfFunctionAccess = (Map<Object, Set<Function>>)mav.getModelMap().get("mapOfFunctionAccess");
			Assert.assertTrue(mapOfFunctionAccess.keySet().size() != 0);
//TODO remove upto here		
	        
	        //if redirect returned then handle that redirect
	        if(mav.getViewName().startsWith("redirect:")) {
	        	return handle("GET", mav.getViewName().substring("redirect:".length()).replace("content/", ""));
	       
	        //otherwise, just return mav as it is
	        } else {
	        	return mav;
	        }
	        
    	} catch(Exception err) {
    		throw new RuntimeException("failed to handle mock request to: "+request.getRequestURI(), err);
    	}
    }
}
