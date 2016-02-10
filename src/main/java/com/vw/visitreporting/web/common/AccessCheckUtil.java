package com.vw.visitreporting.web.common;

import java.util.Map;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vw.visitreporting.entity.accesscontrolled.AccessControlled;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.referencedata.util.FunctionManager;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;

public class AccessCheckUtil {

	//TODO this will not work now as the first time authentication functionality needs to be modified to load allowedFunctions
	public static boolean canUserAccessFunction(String function){
		
		Function theFunction = Function.valueOf(function);
		//get the user first
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		if(userDetailsImpl.getAllowedFunctions().contains(theFunction)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean canUserAccessFunctionOnObject(Map<Object, Set<Function>> mapOfFunctionAccess, String function, AccessControlled entity){
		//this method can be invoked only from a web page
		//the assumption is that authorities for each access-controlled object have been retrieved and stored in mapOfFunctionAccess
		Function theFunction = Function.valueOf(function);

		
		if(! mapOfFunctionAccess.containsKey(entity)){
			//entity was not allowed at all to the user - so it is not present in the map
			return false;
		}
		
		Set<Function> functions = mapOfFunctionAccess.get(entity);
		if(functions == null && ! FunctionManager.doesClassHaveAnyAssociatedFunction(entity.getClass().getSimpleName())){
			//the entity has no associated function but the entity has passed permission check.
			return true;
		}
		
		if(functions.contains(theFunction)){
			return true;
		}
		else{
			return false;
		}
	}
	
}
