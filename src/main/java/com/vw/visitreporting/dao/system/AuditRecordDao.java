package com.vw.visitreporting.dao.system;

import java.io.Serializable;

import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.system.AuditRecord;
import com.vw.visitreporting.entity.system.AuditType;


/**
 * Contract for a type-safe implementation of AbstractVRDao that adds additional search functionality.
 */
public interface AuditRecordDao {

	/**
	 * Insert a new AuditRecord into the database.
	 * @param t - instance of the entity class containing initial data to insert into the database.
	 * @return the same object that was passed to this method, with the primary key property set
	 */
	AuditRecord create(AuditRecord record);

	/**
	 * Gets an audit record for the last change to a given entity
	 * @param entityClass - the class of the entity to return search audit history of
	 * @param entityId - the primary key of the entity to return search audit history of
	 * @param type - the type of audit record to return (ADD,MOD,DEL)
	 * @param propertyName - if searching for MOD type of audit record, search for modifications of this property
	 * @return the most recent audit record associated to the specified entity, or null if no audit records were found
	 */
	AuditRecord findLastChange(Class<? extends BaseVREntity> entityClass, Serializable entityId, AuditType type, String propertyName);
}
