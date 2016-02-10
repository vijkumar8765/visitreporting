package com.vw.visitreporting.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;


/**
 * Provides common functionality for controller integration tests.
 */
@Component
public class ControllerUtil {

	@Autowired
	protected ApplicationContext context;
	

	/**
     * This is a helper method which invokes the Spring MVC framework and returns the
     * ModelAndView from a controller method call.
     * @param method - the HTTP method to simulate, e.g. GET or POST
     * @param requestUrl - the request URI to simulate
     */
	public ModelAndView handle(String method, String requestUrl) {
    	
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
    public ModelAndView handle(String method, String requestUrl, Map<String, Object> params) {
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
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		//HandlerExecutionChain handler = handlerMapping.getHandler(request);

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
	        Assert.assertNotNull("No handler found for request, check you request mapping", handler);

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
	        
	    	AnnotationMethodHandlerAdapter handlerAdapter = BeanFactoryUtils.beanOfType(context, AnnotationMethodHandlerAdapter.class);
	
	
	        HandlerInterceptor[] interceptors = handler.getInterceptors();
	        for (HandlerInterceptor interceptor : interceptors) {
	            boolean carryOn = interceptor.preHandle(request, response, controller);
	            if (!carryOn) {
	                return null;
	            }
	        }
	
	        ModelAndView mav = handlerAdapter.handle(request, response, controller);

			for(HandlerMapping hm : handlerMappings) {
				handler = hm.getHandler(request);
				if(handler != null) {
					
			        HandlerInterceptor[] interceptors2 = handler.getInterceptors();
			        for (HandlerInterceptor interceptor : interceptors2) {
			            interceptor.postHandle(request, response, handlerAdapter, mav);
			        }				
				}
			}
	        
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
