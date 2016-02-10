package com.vw.visitreporting.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.auth.GenericAccessGrantDao;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.security.GenericAccessGrant;
import com.vw.visitreporting.service.AbstractVRService;

/**
 * 
 * @author FOX0SHN
 * This class facilitates granting access to other users.
 * However this class needs to be tightly controlled so that a user cannot grant access
 * for something he himselves doesn't have access
 *
 */
@Service
public class GenericAccessGrantServiceImpl extends AbstractVRService<GenericAccessGrant> implements GenericAccessGrantService{
//TODO this class still needs to be implemented
	private GenericAccessGrantDao genericAccessGrantDao;

	private String [] entitiesForWhichAccessCanBeGranted = {"Action","VisitReport","Agenda"}; //TODO this should be configured as it is not maintainable now
	
	public GenericAccessGrantServiceImpl(){
	}
	
	public boolean doesEntitySupportAccessGrant (String className){
		//the argument should be simpleClassName
		if(className == null){
			throw new IllegalArgumentException("className cannot be null");
		}
		
		for(String anEntity: entitiesForWhichAccessCanBeGranted){
			if(className.equals(anEntity)){
				return true;
			}
		}
		return false;
	}
	
	public boolean doesFunctionBelongToAnEntityWhichSupportsAccessGrant(Function function){
		return doesEntitySupportAccessGrant(function.getClass().getSimpleName());
	}
	
	public boolean isUserGrantedAccessToFunctionOnEntity(User user, BaseVREntity entity, Function function){
		
		//first see if the entity supports accessGrant
		//TODO this code has been stubbed
		if(function.equals(Function.EDIT_AGENDA)){
			return true;
		}
		if(function.equals(Function.EDIT_VISIT_REPORT)){
			return false;
		}
		return false;
		//return genericAccessGrantDao.isUserGrantedAccessToFunctionOnEntity(user, entity, function);
	}
	
	public boolean isUserGrantedAccessToFunction(User user, Function function){

		//first see if the function is associated to an entity which supports accessGrant
		
		//TODO this code has been stubbed
		if(function.equals(Function.EDIT_AGENDA)){
			return true;
		}
		if(function.equals(Function.EDIT_VISIT_REPORT)){
			return false;
		}
		return false;
		//return genericAccessGrantDao.isUserGrantedAccessToFunction(user, function);
	}
	
	
	@Autowired
	public void setGenericAccessGrantDao(GenericAccessGrantDao genericAccessGrantDao) {
		this.genericAccessGrantDao = genericAccessGrantDao;
	}
	
	@Override
	protected VRDao<GenericAccessGrant> getTDao() {
		return genericAccessGrantDao;
	}
	
	//other search methods come here
	
}
