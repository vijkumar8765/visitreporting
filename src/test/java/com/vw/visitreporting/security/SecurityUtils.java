package com.vw.visitreporting.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.security.authentication.AuthenticationUserDetailsServiceImpl;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;


/**
 * Provides utility functions for unit tests to allow authentication to be
 * mimicked in a test instance of the system.
 */
@Component
public class SecurityUtils {

	@Autowired
	private AuthenticationUserDetailsServiceImpl authenticationUserDetailsServiceImpl;
	

	/**
	 * Simulate an existing user being logged into the system.
	 * The User table in the database must contain a row for the user name supplied.
	 * @param username - the windows user name of the user to login to the system.
	 */
	public void loginUser(String username) {

		//use out implementation of AuthenticationUserDetailsService to lookup the user details for the given user name
		PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(username, "");
		UserDetails userDetails = authenticationUserDetailsServiceImpl.loadUserDetails(token);
		
		//add user to Spring SecurityContextHolder
		Authentication auth = new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	/**
	 * Simulate a user that has been created on the fly being logged into the system.
	 * @param user - a new instance of User that is not necessarily in the database
	 */
	public void loginUser(User user) {

		UserDetails userDetails = new UserDetailsImpl(user);
		
		//add user to Spring SecurityContextHolder
		Authentication auth = new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	/**
	 * Simulate the current logged in user being logged out of the system.
	 */
	public void logoutUser() {
		
		//remove user from Spring SecurityContextHolder
		SecurityContextHolder.getContext().setAuthentication(null);
	}
}
