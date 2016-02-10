package com.vw.visitreporting.security.authentication;

import java.util.EnumSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import com.vw.visitreporting.dao.referencedata.UserDao;
import com.vw.visitreporting.entity.referencedata.OrganisationFunction;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.UserProfile;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.service.auth.GenericAccessGrantService;


/**
 * Custom implementation of AuthenticationUserDetailsService that provides a custom implementation of UserDetails
 */
public class AuthenticationUserDetailsServiceImpl implements AuthenticationUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationUserDetailsServiceImpl.class);

	private UserDao userDao;
	
	private GenericAccessGrantService accessGrantService;

	/**
	 * Looks up a user in the database by window user name specified in an authentication token
	 * and returns a custom implementation of the Spring UserDetails interface.
	 */
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {

		String username = token.getPrincipal().toString();
		User user = null;
		try {
			user = userDao.getById(username);
			
		} catch(Exception err) {
			throw new AuthenticationServiceException("problem connecting to database", err);
		}
		
		if(user == null) {
			throw new UsernameNotFoundException(username+" is not a user of this system.");
		}
		if(user.isDisabled()) {
			throw new DisabledException(username+" user account has been disabled in this system.");
		}

		LOG.info(username+" successfully logged into the system.");

		UserDetailsImpl userDetailsImpl= new UserDetailsImpl(user);
		userDetailsImpl.setAllowedFunctions(loadAllowedFunctionsForUser(user));
		return userDetailsImpl;
	}
	
	private EnumSet<Function> loadAllowedFunctionsForUser(User user){
		EnumSet<Function> allFunctions = EnumSet.allOf(Function.class);
		EnumSet<Function> allowedFunctions = EnumSet.noneOf(Function.class);
		for(Function aFunction:allFunctions ){
			if(checkPermission(user, aFunction)){
				allowedFunctions.add(aFunction);
			}
		}
		
		return allowedFunctions;
	}

	
	private boolean checkPermission(User user, Function function){
		
		if( organisationContainsFunctionAccess(user, function ) && userProfilesContainFunctionAccess(user, function ) ){
			return true;
		}
		
		//checking if the user has been granted access (irrespective of whether the function is in user's organisation and userprofile function access)
		if(accessGrantService.isUserGrantedAccessToFunction(user,function)){
			return true;
		}
		
		return false;
	}
	

	private boolean organisationContainsFunctionAccess(User user,
			Function function) {
		Set<OrganisationFunction> organisationFunctions = user
				.getOrganisation().getAllowedFunctions();
		for (OrganisationFunction anOrganisationFunction : organisationFunctions) {
			if (anOrganisationFunction.getFunction().equals(function)
					&& user.getBrands().contains(
							anOrganisationFunction.getBrand())) {
				return true;
			}
		}
		return false;
	}

	private boolean userProfilesContainFunctionAccess(User user,
			Function function) {
		Set<UserProfile> profiles = user.getUserProfiles();
		for (UserProfile profile : profiles) {
			if (profile.getAllowedFunctions().contains(function)) {
				return true;
			}
		}
		return false;
	}

	@Autowired
	public void setAccessGrantService(
			GenericAccessGrantService accessGrantService) {
		this.accessGrantService = accessGrantService;
	}
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}



}
