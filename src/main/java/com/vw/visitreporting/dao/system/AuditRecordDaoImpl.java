package com.vw.visitreporting.dao.system;

import java.io.Serializable;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.system.AuditRecord;
import com.vw.visitreporting.entity.system.AuditType;


/**
 * Data access object for the AuditRecord entity.
 * Provides a type-safe implementation of AbstractVRDao and adds additional search functionality.
 */
@Repository
public class AuditRecordDaoImpl implements AuditRecordDao {

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * Helper method to concisely obtain an open Hibernate session.
	 */
	protected Session getSession() {
		
		//use this method instead of createSession() or getCurrentSession() - it will ensure that the current transactional context is used.
		return SessionFactoryUtils.getSession(this.sessionFactory, true);
	}

	/**
	 * Insert a new AuditRecord into the database.
	 * @param t - instance of the entity class containing initial data to insert into the database.
	 * @return the same object that was passed to this method, with the primary key property set
	 */
	public AuditRecord create(AuditRecord record) {
		Session session = getSession();
		session.save(record);
		session.flush();	//always flush audit record creation in case a query needs to be run in the same transaction
		return record;
	}
	
	
	/**
	 * Gets an audit record for the last change to a given entity
	 * @param entityClass - the class of the entity to return search audit history of
	 * @param entityId - the primary key of the entity to return search audit history of
	 * @param type - the type of audit record to return (ADD,MOD,DEL)
	 * @param propertyName - if searching for MOD type of audit record, search for modifications of this property
	 * @return the most recent audit record associated to the specified entity, or null if no audit records were found
	 */
	public AuditRecord findLastChange(Class<? extends BaseVREntity> entityClass, Serializable entityId, AuditType type, String propertyName) {
		
		Criteria crit = getSession().createCriteria(AuditRecord.class);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		crit.setCacheable(true);
		
		crit.add(Restrictions.eq("entityClass", AuditRecord.getEntityClassName(entityClass)));
		crit.add(Restrictions.eq("entityId", entityId.toString()));
		crit.add(Restrictions.eq("eventType", type.getId()));
		if(type == AuditType.MOD) {
			crit.add(Restrictions.eq("propertyName", propertyName));
		}
		
		//get most recent
		crit.addOrder(Order.desc("id"));
		crit.setMaxResults(1);
	
		return (AuditRecord)crit.uniqueResult();
	}
}
