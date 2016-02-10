package com.vw.visitreporting.web.interceptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.vw.visitreporting.entity.accesscontrolled.AccessControlled;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.security.auth.AccessControlHandler;

public class DomainObjectAuthorisationInterceptor extends HandlerInterceptorAdapter{
	
	private AccessControlHandler accessControlHandler;
	
	//after the handler is executed
	public void postHandle(
		HttpServletRequest request, HttpServletResponse response, 
		Object handler, ModelAndView modelAndView)
		{
		
		Collection<Object> objectsForWhichToGetAllowedFunctions = new HashSet<Object>();
		ModelMap mapOfModelObjects = modelAndView.getModelMap();
		Set<String> keys = mapOfModelObjects.keySet();
		for(String aKey:keys){
			Object aModelObject = mapOfModelObjects.get(aKey);
			if(aModelObject instanceof AccessControlled){
				objectsForWhichToGetAllowedFunctions.add(aModelObject);
			}
			/*
			We will not support getting the accesses for collections
			
			if(aModelObject instanceof Collection){
				for(Object anObjectInCollection: (Collection)aModelObject){
					//iterate through each object in the collection
					if(anObjectInCollection instanceof AccessControlled){
						objectsForWhichToGetAllowedFunctions.add(anObjectInCollection);
					}
				}
			}
			*/
		}
		
			
		Map<Object, Set<Function>> mapOfFunctionAccess = null;
		if(objectsForWhichToGetAllowedFunctions.size() != 0){
			mapOfFunctionAccess = accessControlHandler.getAllowedFunctionsOnObjects(objectsForWhichToGetAllowedFunctions);
			//modified the exisitng modelAndView
		}
		else{
			//empty map - can't leave it null
			mapOfFunctionAccess = new HashMap<Object, Set<Function>>();
		}

		modelAndView.addObject("mapOfFunctionAccess",mapOfFunctionAccess);

	}
	
	@Autowired
	public void setAccessControlHandler(AccessControlHandler accessControlHandler) {
		this.accessControlHandler = accessControlHandler;
	}

}
