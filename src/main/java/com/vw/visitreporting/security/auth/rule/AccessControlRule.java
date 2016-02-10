package com.vw.visitreporting.security.auth.rule;

import com.vw.visitreporting.entity.referencedata.enums.Function;

public interface AccessControlRule<T> {
	
	public Boolean invokeRule(T entity, Function function);
	
	public boolean supports(Object entity);

}
