package com.vw.visitreporting.security.auth;

import java.util.Set;

import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.security.auth.rule.AccessControlRule;

public class AccessControlRuleInvocationBean {

	private String entity;
	private Function function;
	private boolean invokeCommonRules = true;
	private Set<AccessControlRule> rulesToBeInvoked;
	
	
	public boolean applyAccessControlRules(Object entityToBeAccessChecked){
		
		for(AccessControlRule aRule: rulesToBeInvoked){
			Boolean allowed = aRule.invokeRule(entityToBeAccessChecked,function);
			//allowed = null implies - the rule is not applicable
			//if false return straight away, else look for other rules - all rules must be successful(or not application) to assume that user has access
			if(allowed!= null && (! allowed.booleanValue())){
				return false;
			}
		}
		return true;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public boolean invokeCommonRules() {
		return invokeCommonRules;
	}

	public void setInvokeCommonRules(boolean invokeCommonRules) {
		this.invokeCommonRules = invokeCommonRules;
	}

	public Set<AccessControlRule> getRulesToBeInvoked() {
		return rulesToBeInvoked;
	}

	public void setRulesToBeInvoked(
			Set<AccessControlRule> rulesToBeInvoked) {
		this.rulesToBeInvoked = rulesToBeInvoked;
	}
}
