package com.vw.visitreporting.dao.auth;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.security.GenericAccessGrant;


public interface GenericAccessGrantDao extends VRDao<GenericAccessGrant>{

	public boolean isUserGrantedAccessToFunctionOnEntity(User user, BaseVREntity entity, Function function);
	
	public boolean isUserGrantedAccessToFunction(User user, Function function);

	//the create method works as grant while delete method works as revoke
	
	//other search methods come here

}
