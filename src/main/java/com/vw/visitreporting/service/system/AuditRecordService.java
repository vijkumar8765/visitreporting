package com.vw.visitreporting.service.system;

import java.io.Serializable;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.system.AuditRecord;
import com.vw.visitreporting.entity.system.AuditType;


public interface AuditRecordService {

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
