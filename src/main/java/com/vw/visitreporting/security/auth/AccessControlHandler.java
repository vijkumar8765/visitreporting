package com.vw.visitreporting.security.auth;


import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.vw.visitreporting.common.exception.ExceptionCode;
import com.vw.visitreporting.common.exception.OperationNotSupportedException;
import com.vw.visitreporting.entity.referencedata.enums.Function;

public class AccessControlHandler implements PermissionEvaluator {

	private DomainObjectAccessManager domainObjectAccessManager;

	private FunctionAccessManager functionAccessManager;

	public AccessControlHandler() {
	}

	@Override
	public boolean hasPermission(Authentication arg0,
			Object targetDomainObject, Object function) {

		// TODO the reason why access has been denied should be placed in a
		// ThreadLocal instance
		// It will be valuable in debugging

		Function theFunction = null;

		if (function != null) {
			if (function instanceof Function) {
				theFunction = (Function) function;
			} else {
				try {
					theFunction = Function.valueOf((String) function);
				} catch (ClassCastException cce) {
					throw new IllegalArgumentException(
							"The argument function is not a valid function");
				}
			}
		}

		// There are 4 scenarios which can apply
		// 1. function and object null
		// 2. funtion provided, object null
		// 3. function null, object provided
		// 4. function and object provided

		if (theFunction == null && targetDomainObject == null) {
			throw new IllegalArgumentException(
					"Both function and targetDomainObject cannot be null");
		}

		if (targetDomainObject == null) {
			return functionAccessManager.hasPermission(theFunction);
		} else {
			return domainObjectAccessManager.hasPermission(targetDomainObject,
					theFunction);
		}
	}

	@Override
	public boolean hasPermission(Authentication arg0, Serializable targetId,
			String targetType, Object function) {
		throw new OperationNotSupportedException(
				ExceptionCode.ACCESS_CONTROL_HANDLER_OPERATION_NOT_SUPPORTED,
				"");
	}

	public Map<Object, Set<Function>> getAllowedFunctionsOnObjects(
			Collection<Object> objectsForWhichToGetAllowedFunctions) {
		return domainObjectAccessManager
				.getAllowedFunctionsOnObjects(objectsForWhichToGetAllowedFunctions);
	}

	@Autowired
	public void setDomainObjectAccessManager(
			DomainObjectAccessManager domainObjectAccessManager) {
		this.domainObjectAccessManager = domainObjectAccessManager;
	}

	@Autowired
	public void setFunctionAccessManager(
			FunctionAccessManager functionAccessManager) {
		this.functionAccessManager = functionAccessManager;
	}

}
