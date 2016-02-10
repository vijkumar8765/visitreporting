package com.vw.visitreporting.web;

import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;


/**
 * Define a fault barrier that catches all uncaught exceptions during a request and redirects to an appropriate error message page. 
 */
public class CustomMappingExceptionResolver extends SimpleMappingExceptionResolver {

	private static final Logger LOG = LoggerFactory.getLogger(CustomMappingExceptionResolver.class);


	/**
	 * Called when an Exception is thrown by this system but not caught.
	 * Redirects user to a friendly error message page and logs the error.
	 */
	public ModelAndView resolveException(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {

		//log the exception
		LOG.error("Unrecoverable error: "+ex.getMessage(), ex);

		//redirect to friendly error message page
		return super.resolveException(request, response, handler, ex);
	}
}