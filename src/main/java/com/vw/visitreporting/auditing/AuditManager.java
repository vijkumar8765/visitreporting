package com.vw.visitreporting.auditing;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import javax.persistence.Entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.vw.visitreporting.common.exception.ExceptionCode;
import com.vw.visitreporting.common.exception.VisitReportingException;
import com.vw.visitreporting.dao.system.AuditRecordDao;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.DealerRelated;
import com.vw.visitreporting.entity.system.AuditRecord;
import com.vw.visitreporting.entity.system.AuditType;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;


/**
 * Encapsulated all the logic concerning auditing in this system.
 * The methods in this component should be called by the AbstractVRDao
 * whenever any entity is created/updated/deleted, even if the entity is
 * not annotated with @Audited.
 */
@Component
public class AuditManager {

	@Autowired
	private AuditRecordDao auditRecordDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	/**
	 * This method should be called after any entity is inserted into the database
	 * (whether or not it is annotated with @Audited)
	 * @param newObject - the new entity that has just been persisted (id must have been set)
	 */
	public void onEntityCreation(BaseVREntity newObject) {
		if( ! newObject.getClass().isAnnotationPresent(Audited.class)) {
			return;
		}
		
		//create audit record and persist it
		AuditRecord record = newAuditRecord(newObject);
		record.setEventType(AuditType.ADD);
		auditRecordDao.create(record);
	}
	
	/**
	 * This method should be called after any entity is deleted from the database
	 * (whether or not it is annotated with @Audited)
	 * @param oldObject - the new entity that has just been deleted
	 */
	public void onEntityDeletion(BaseVREntity oldObject) {
		if( ! oldObject.getClass().isAnnotationPresent(Audited.class)) {
			return;
		}
		
		//create audit record and persist it
		AuditRecord record = newAuditRecord(oldObject);
		record.setEventType(AuditType.DEL);
		auditRecordDao.create(record);
	}
	
	/**
	 * This method should be called before any entity is modified in the database
	 * (whether or not it is annotated with @Audited).
	 * A new audit record is written for each property that has been changed.
	 * @param prevVersion - the last version of this entity that was persisted to the database
	 * @param nextVersion - the current version that just about to be persisted to the database.
	 */
	public void onEntityModification(BaseVREntity nextVersion) {

		//get entity class
		Class<?> entityClass = nextVersion.getClass();
		if( ! entityClass.isAnnotationPresent(Entity.class)) {	//if class is a proxy sub-class then use superclass
			entityClass = entityClass.getSuperclass();
		}
		
		Session session = null;
		String propertyName = null;
		try {
			session = sessionFactory.openSession();
			BaseVREntity prevVersion = (BaseVREntity)session.get(entityClass, nextVersion.getId());
		
			//find any changes between prev and next version
			for(Method m : entityClass.getMethods()) {
				
				//look for setter methods that are annotated with Audited
				if(m.getName().startsWith("set") && m.isAnnotationPresent(Audited.class)) {
					
					propertyName = m.getName().substring(3);
					
					//get the corresponding getter method
					Method getter = entityClass.getMethod("get"+propertyName);
	
					/*
					 * TODO: do this instead for collection properties
					if(PersistentCollection.class.isAssignableFrom(getter.getGenericReturnType().getClass()) {
						PersistentCollection nextValue = (PersistentCollection)getter.invoke(nextVersion);
						if(nextValue.isDirty()) {
							
						}
					}
					*/
					
					//if this property has changed
					if( ! getter.invoke(prevVersion).equals(getter.invoke(nextVersion))) {
	
						//create audit record and persist it
						AuditRecord record = newAuditRecord(prevVersion);
						record.setEventType(AuditType.MOD);
						record.setPropertyName(propertyName);
						auditRecordDao.create(record);
					}
				}
			}
		} catch(NoSuchMethodException err) {
			throw new VisitReportingException(ExceptionCode.GENERAL, "getter method could not be found for property "+propertyName+" of entity class "+nextVersion.getClass().getSimpleName(), err);
		} catch(InvocationTargetException err) {
			throw new VisitReportingException(ExceptionCode.GENERAL, "could not invoke "+nextVersion.getClass().getSimpleName()+".get"+propertyName+"()", err);
		} catch(IllegalAccessException err) {
			throw new VisitReportingException(ExceptionCode.GENERAL, "could not invoke "+nextVersion.getClass().getSimpleName()+".get"+propertyName+"()", err);
			
		} finally {
			session.close();
		}
	}

	
	/**
	 * Creates a new AuditRecord instance (without persisting it in the database)
	 * and populates the some of the fields.
	 */
	private AuditRecord newAuditRecord(BaseVREntity entity) {
		AuditRecord record = new AuditRecord();
		
		//Serializable entityId = sessionFactory.getCurrentSession().getIdentifier(entity);
		
		//get the username of the user that made the change
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = (auth == null) ? "<annoymous>" : ((UserDetailsImpl)auth.getPrincipal()).getUsername().toLowerCase();
		
		record.setEntityClass(entity.getClass());
		record.setEntityId(entity.getId());
		record.setUserId(username);
		record.setEventTimestamp(new Date());
		
		if(entity instanceof DealerRelated) {
			record.setDealersInvolved( ((DealerRelated)entity).getRelatedDealers() );
		}
		return record;
	}
}
