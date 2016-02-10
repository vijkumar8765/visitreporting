package com.vw.visitreporting.security.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hsqldb.lib.StringUtil;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;


/**
 * Custom implementation of AbstractPreAuthenticatedProcessingFilter to translate user name
 * from JCIFS NTLM bean in request scope to a user name and password.
 *
 * NOTE: for pre-authentication solutions where user_id is in request header, use RequestHeaderAuthenticationFilter  -->
 *       http://docs.huihoo.com/spring/spring-security/3.0.x/preauth.html#d4e2269
 */
public class PreAuthenticatedProcessingFilterImpl extends AbstractPreAuthenticatedProcessingFilter {

	/**
	 * Get the user name from the request.
	 * Assume that the jcifs.http.NtlmHttpFilter filter occurs before this in the filter chain.
	 * Obtain the NtlmHttpAuth bean from JCIFS NTLM authentication and extract the user name.
	 */
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		
		//TODO: change this to retrive username from request header (put there by pre-authentication solution)
		
////////////////		
		HttpSession session = ((HttpServletRequest)request).getSession(false);
		if(session == null) {
			throw new AuthenticationCredentialsNotFoundException("no session created");
		}
		String userId = null;
		if(session == null || session.getAttribute("userId") == null) {
			userId = request.getParameter("userId");
			if(session != null && userId != null) {
				session.setAttribute("userId", userId);
			}
		} else {
			userId = (String)session.getAttribute("userId");
		}
		if(StringUtil.isEmpty(userId)) {
			throw new AuthenticationCredentialsNotFoundException("userId request parameter not found");
		}
////////////////
		
		return userId;
	}

	/**
	 * Get the password from the request.
	 * In this case, there is no password/credentials so return empty string.
	 */
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return "";
	}
}
