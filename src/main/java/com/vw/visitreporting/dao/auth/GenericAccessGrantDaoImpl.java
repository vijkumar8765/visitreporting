package com.vw.visitreporting.dao.auth;

import org.springframework.stereotype.Repository;

import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.security.GenericAccessGrant;

@Repository
public class GenericAccessGrantDaoImpl extends AbstractVRDao<GenericAccessGrant> implements GenericAccessGrantDao{

	public boolean isUserGrantedAccessToFunctionOnEntity(User user, BaseVREntity entity, Function function){
		//just check for the presence of that function and entityId,entityName for the user
		//also look at the data range
		return false;
	}
	
	public boolean isUserGrantedAccessToFunction(User user, Function function){
		//just check for the presence of that function for the userId
		//also look at the data range
		return false;
	}

	//the create method works as grant while delete method works as revoke
	
	//other search methods come here

}
