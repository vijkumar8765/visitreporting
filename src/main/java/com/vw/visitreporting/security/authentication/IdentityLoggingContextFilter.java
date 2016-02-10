package com.vw.visitreporting.security.authentication;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Filter to be placed at the end of the filter chain to obtain the currently logged in user
 * from the Spring Security Context and add it to the logging framework (for this thread) during this request.
 */
public class IdentityLoggingContextFilter implements Filter {

	/**
	 * Relays request on while adding a user name parameter to the logging framework and removing it after the request has been handled.
	 * Assumes that the user has been authenticated by Spring security and the Spring Security Context has a valid Authentication object.
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String username = (auth == null) ? "<annoymous>" : ((UserDetailsImpl)auth.getPrincipal()).getUsername();
		
		try {
			MDC.put("username", username);	   
			chain.doFilter(request, response);	 
		} finally {
			MDC.remove("username");
		}
	}
	
	/**
	 * No initialisation required.
	 */
	public void init(FilterConfig config) throws ServletException {
		//No initialisation required
	}
	
	/**
	 * No disposal required.
	 */
	public void destroy() {
		//No disposal required.
	}
}
