package com.vw.visitreporting.security.auth.rule.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vw.visitreporting.common.CollectionUtil;
import com.vw.visitreporting.common.exception.ExceptionCode;
import com.vw.visitreporting.common.exception.VisitReportingException;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.security.auth.AccessControlledEntity;
import com.vw.visitreporting.security.auth.AccessControlledEntityCollection;
import com.vw.visitreporting.security.auth.rule.AccessControlRule;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;

//T is the object on which access check will be applied
//S is the type of object for which access check is made(e.g. Brand, Organisation,GeographicalStructureCategory etc..)
public abstract class AbstractAccessControlRule<T, S> implements
		AccessControlRule<T> {

	protected Class classS;

	protected AbstractAccessControlRule() {
		// derive what type T is (for this instance) using reflection
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		this.classS = (Class<S>) parameterizedType.getActualTypeArguments()[1];

	}

	protected abstract Collection<S> getUserAllowedEntities();
	
	public abstract Boolean specificAccessCheck(T entity, Function function);

	public boolean supports(Object entity){
		if(entity == null){
			return false;
		}
		if(classS.isInstance(entity) || CollectionUtil.isObjectCollectionOfClazzObjects(entity,classS) ){
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean invokeRule(T entity, Function function) {

		//this code is intended to test access control to a object without caring about the function
		//the code will work for entity comparison for instance, the argument(entity) collection of Brands are available in user's associated brand list
		//or the argument(entity) collection of  are available in user's associated brand list etc
		
		Boolean allowed = specificAccessCheck(entity, function);
		if (allowed != null) {
			return allowed.booleanValue();
		}

		// get the user
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();

		if (classS.isInstance(entity)) {
			// is this object available in the set of allowedObjects configured for the
			// user.
			if (getUserAllowedEntities().contains(classS.cast(entity))) {
				return true;
			} else {
				return false;
			}
		}

		if (CollectionUtil.isObjectCollectionOfClazzObjects(entity,classS)) {
			// user should have access to all the objects
			if (getUserAllowedEntities().containsAll((Collection) entity)) {
				return true;
			} else {
				return false;
			}
		}
		
		return null;

	}

}
