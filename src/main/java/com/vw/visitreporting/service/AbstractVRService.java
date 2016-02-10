package com.vw.visitreporting.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.accesscontrolled.AccessControlled;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.referencedata.util.FunctionManager;
import com.vw.visitreporting.security.auth.AccessControlHandler;


public abstract class AbstractVRService<T extends BaseVREntity> {

	private AccessControlHandler accessControlHandler;
	
	protected final Class<T> entityClass;

	
	protected AbstractVRService(){	
		//derive what type T is (for this instance) using reflection
		ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();
		this.entityClass = (Class<T>)parameterizedType.getActualTypeArguments()[0];
	}
	
	
	@Autowired
	public void setAccessControlHandler(AccessControlHandler accessControlHandler) {
		this.accessControlHandler = accessControlHandler;
	}
	
	protected abstract VRDao<T> getTDao();
	
	
	public T getById(Serializable id){
		
		T t = getTDao().getById(id);	
		return t;
	}
	
	public T save(T t){

		if (AccessControlled.class.isAssignableFrom(entityClass)){
			String functionPrefix = null;
			if(t.getId() == null){
				functionPrefix = "CREATE";
			}
			else{
				functionPrefix = "EDIT";
			}
			
			Function function = FunctionManager.getFunctionForUserActionAndClass(functionPrefix, entityClass);
			if( accessControlHandler.hasPermission(null, t, function) ){
				throw new AccessDeniedException("User not authorised to invoke action");
			}
		}
		
		return getTDao().save(t);
	}
	
	public String delete(T t){
		
		if (AccessControlled.class.isAssignableFrom(entityClass)){
			String functionPrefix = "DELETE";

			Function function = FunctionManager.getFunctionForUserActionAndClass(functionPrefix, entityClass);
			if( accessControlHandler.hasPermission(null, t, function) ){
				throw new AccessDeniedException("User not authorised to invoke action");
			}
		}

		
		return getTDao().delete(t);
	}
	
	public List<T> list(){
		return getTDao().list();
	}
}
