package com.vw.visitreporting.security.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;

@Component
public class FunctionAccessManager {

	public FunctionAccessManager() {
	}

	public boolean hasPermission(Function function){
		
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		return (userDetailsImpl.getAllowedFunctions().contains(function));
	}
}
