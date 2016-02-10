package com.vw.visitreporting.service.system;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vw.visitreporting.dao.system.AuditRecordDao;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.system.AuditRecord;
import com.vw.visitreporting.entity.system.AuditType;


@Service
public class AuditRecordServiceImpl implements AuditRecordService {

	@Autowired
	private AuditRecordDao auditRecordDao;
	
	/**
	 * Gets an audit record for the last change to a given entity
	 * @param entityClass - the class of the entity to return search audit history of
	 * @param entityId - the primary key of the entity to return search audit history of
	 * @param type - the type of audit record to return (ADD,MOD,DEL)
	 * @param propertyName - if searching for MOD type of audit record, search for modifications of this property
	 * @return the most recent audit record associated to the specified entity, or null if no audit records were found
	 */
	public AuditRecord findLastChange(Class<? extends BaseVREntity> entityClass, Serializable entityId, AuditType type, String propertyName) {
		return auditRecordDao.findLastChange(entityClass, entityId, type, propertyName);
	}
}
