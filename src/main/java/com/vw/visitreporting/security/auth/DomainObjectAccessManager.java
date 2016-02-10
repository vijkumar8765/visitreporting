package com.vw.visitreporting.security.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vw.visitreporting.common.CollectionUtil;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.accesscontrolled.AccessControlled;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.referencedata.util.FunctionManager;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;
import com.vw.visitreporting.service.auth.GenericAccessGrantService;

public class DomainObjectAccessManager {

	private CommonRuleInvocationBean commonRuleInvoker;

	private Set<AccessControlRuleInvocationBean> accessControlRuleInvokers;
	
	private FunctionAccessManager functionAccessManager;

	private GenericAccessGrantService accessGrantService;
	
	public Map<Object,Set<Function>> getAllowedFunctionsOnObjects(Collection<Object> objects){
		
		//we need to validate that the passed in objects can be access controlled
		//for now Collection objects are not supported
		Map<Object,Set<Function>> map = new HashMap<Object,Set<Function>>();
		for(Object obj: objects){
			if (! (obj instanceof AccessControlled) ){
				//this will exclude collections or other objects which are not marked with AccessControlled
				continue;
			}
			
			Set<Function> allowedFunctions = getAllowedFunctionsForObject(obj);
			
			if(!(allowedFunctions == null &&  ! hasPermission(obj, null) )){
				map.put(obj, getAllowedFunctionsForObject(obj) );
			}
			
		}
		return map;
	}

	private Set<Function> getAllowedFunctionsForObject(Object obj){
		//get set of functions possible for the object type
		
		Set<Function> allFunctionsForClassType = FunctionManager.getAllPossibleFunctionsForObjectType(obj.getClass().getSimpleName());
		
		if (allFunctionsForClassType == null){
			//no associated functions for domain object
			return null;
		}
		Set<Function> allowedFunctions = new HashSet<Function>();
		//test for each function if the user is allowed to invoke it on the domainObject
		for(Function aFunction: allFunctionsForClassType ){
			if(hasPermission(obj, aFunction)){
				allowedFunctions.add(aFunction);
			}
		}
		return allowedFunctions;
	}
	
	public boolean hasPermission(Object entity, Function function) {
		
		//TODO explain the algorithm here
		
		if(!(entity instanceof AccessControlled || CollectionUtil.isObjectCollectionOfClazzObjects(entity, AccessControlled.class))){
			throw new IllegalArgumentException("Only AccessControlled objects can be checked for access control");
		}
		
		//function can be null - in such a case function access check is not required
		//this becomes simple access check on domain object
		if( function != null && ! functionAccessManager.hasPermission(function)){
			return false;
		}

		if (accessGrantService.doesEntitySupportAccessGrant(entity.getClass().getSimpleName())) {
			
			//get the user first
			User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal()).getUser();
			
			if (accessGrantService.isUserGrantedAccessToFunctionOnEntity(user,
					(BaseVREntity) entity, function)) {
				// if access has been granted no need to do other checks
				return true;
			}
		}
	
		// lets look for an invocationBean which is valid for this entity and
		// function pair

		AccessControlRuleInvocationBean accessControlRuleInvocationBean = null;

		accessControlRuleInvocationBean = getInvocationBeanForEntityFunctionCombination(entity,
				function);
		
		if (accessControlRuleInvocationBean == null) {
			// lets look for an invocationBean which has been configured for this entity
			accessControlRuleInvocationBean = getInvocationBeanForEntity(entity);
		}
		
		if (accessControlRuleInvocationBean == null) {
			// no matching beans have been found - lets apply common rules and
			// return
			return commonRuleInvoker.applyCommonAccessControlRules(entity,
					function);
		}
		
		//does the invocationBean allow invocation of common rules
		boolean allowedAccess = accessControlRuleInvocationBean
				.applyAccessControlRules(entity);
		
		if (! allowedAccess) {
			return false;
		}
		else{
			// specific rules have been successful. Lets see if common rules can be
			// invoked
			// if yes, invoke the rules
			if (accessControlRuleInvocationBean.invokeCommonRules()) {
				// common rules can be applied

				return commonRuleInvoker.applyCommonAccessControlRules(entity,
						function);
			}
		}
		
		return true;

	}

	public FunctionAccessManager getFunctionAccessManager() {
		return functionAccessManager;
	}

	public void setFunctionAccessManager(FunctionAccessManager functionAccessManager) {
		this.functionAccessManager = functionAccessManager;
	}

	private AccessControlRuleInvocationBean getInvocationBeanForEntityFunctionCombination(
			Object entity, Function function) {

		if(entity == null || function == null){
			return null;
		}
		
		for (AccessControlRuleInvocationBean anInvocationBean : accessControlRuleInvokers) {
			if (anInvocationBean.getEntity()
					.equals(entity.getClass().getSimpleName())
					&& (anInvocationBean.getFunction() != null && anInvocationBean.getFunction().equals(function))) {
				return anInvocationBean;
			}
		}
		return null;
	}

	private AccessControlRuleInvocationBean getInvocationBeanForEntity(Object entity) {

		if(entity == null){
			return null;
		}
		//look into all invokers
		for (AccessControlRuleInvocationBean anInvocationBean : accessControlRuleInvokers) {
			if (anInvocationBean.getEntity()
					.equals(entity.getClass().getSimpleName())
					&& anInvocationBean.getFunction() == null) {
				// the null check is required so that we do not pick up an
				// invoker which is configured for another entity-function pair
				return anInvocationBean;
			}
		}

		return null;
	}

	public CommonRuleInvocationBean getCommonRuleInvoker() {
		return commonRuleInvoker;
	}

	public void setCommonRuleInvoker(CommonRuleInvocationBean commonRuleInvoker) {
		this.commonRuleInvoker = commonRuleInvoker;
	}

	public Set<AccessControlRuleInvocationBean> getAccessControlRuleInvokers() {
		return accessControlRuleInvokers;
	}

	public void setAccessControlRuleInvokers(
			Set<AccessControlRuleInvocationBean> accessControlRuleInvokers) {
		this.accessControlRuleInvokers = accessControlRuleInvokers;
	}


	@Autowired
	public void setAccessGrantService(
			GenericAccessGrantService accessGrantService) {
		this.accessGrantService = accessGrantService;
	}

}
