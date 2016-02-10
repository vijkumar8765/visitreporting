package com.vw.visitreporting.service.auth;

import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.security.GenericAccessGrant;
import com.vw.visitreporting.service.VRService;

/**
 * 
 * @author FOX0SHN
 * This class facilitates granting access to other users.
 * However this class needs to be tightly controlled so that a user cannot grant access
 * for something he himselves doesn't have access
 *
 */
public interface GenericAccessGrantService extends VRService<GenericAccessGrant>{

	public boolean isUserGrantedAccessToFunctionOnEntity(User user, BaseVREntity entity, Function function);
	
	public boolean isUserGrantedAccessToFunction(User user, Function function);
	
	public boolean doesEntitySupportAccessGrant (String className);
	
	public boolean doesFunctionBelongToAnEntityWhichSupportsAccessGrant(Function function);
	
	//other search methods come here
	
}
