package com.vw.visitreporting.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vw.visitreporting.auditing.AuditManager;
import com.vw.visitreporting.common.constants.VRCommonConstants;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.accesscontrolled.AccessControlled;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.referencedata.util.FunctionManager;
import com.vw.visitreporting.security.auth.AccessControlHandler;


/**
 * Generic DAO to implement CRUD operations for Hibernate entities.
 * This allows boiler plate code to maintained in one place while providing type-safe
 * DOA implementations, with no generic casting warnings, for all entities.
 * @param <T> the type of entity that the sub-class instance of this class works with.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractVRDao<T extends BaseVREntity> implements VRDao<T> {
	
	private SessionFactory sessionFactory;
	
	private AuditManager auditManager;
	
	protected final Class<T> entityClass;
	
	/**
	 * Constructor. Deduces the class type of T and determines level of query
	 * caching to apply based on the usage property of the Cache annotation on the entity.
	 */
	protected AbstractVRDao() {
		//derive what type T is (for this instance) using reflection
		ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();
		this.entityClass = (Class<T>)parameterizedType.getActualTypeArguments()[0];
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Autowired
	public void setAuditManager(AuditManager auditManager) {
		this.auditManager = auditManager;
	}
		
	/**
	 * Get an entity from the database.
	 * @param id - the primary key of the entity to return
	 * @return a single entity specified by the primary key or null if an entity could not be found 
	 */
	public T getById(Serializable id){
		T t = (T)getSession().get(this.entityClass, id);;
		return t;
	}
	
	/**
	 * Insert a new entity into the database.
	 * @param t - instance of the entity class containing initial data to insert into the database.
	 * @return the same object that was passed to this method, with the primary key property set
	 */
	private T create(T t) {
		
		t.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
		t.setCreatedDate(new Date());
		getSession().save(t);
		auditManager.onEntityCreation(t);
		return t;
	}
	
	/**
	 * Update an entity in the database. If the object does not already exist in the database a
	 * new one is inserted with the initial data specified by the object supplied.
	 * @param t - an object containing all data to set in the database.
	 * @return the updated persistent object (assume different to argument supplied to method)
	 */
	private T update(T t) {

		auditManager.onEntityModification(t);
		t.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
		t.setModifiedDate(new Date());
		return (T)getSession().merge(t);
	}
	
	/**
	 * Saved an entity into the database. If the entity is new a new row is inserted,
	 * otherwise an existing row is updated. The created*,modified* properties of the
	 * entity are also updated appropriately. 
	 */
	public T save(T t) {
		if(t.getId() == null) {
			return this.create(t);
		} else {
			return this.update(t);
		}
	}
	
	/**
	 * Deletes an entity from the database.
	 * @param t - the persistent object or a transient object with the primary key set.
	 */
	public String delete(T t) {
		
		getSession().delete(t);
		auditManager.onEntityDeletion(t);
		return VRCommonConstants.SUCCESS;
	}
	
	/**
	 * Return all entities from the database of this type.
	 */
	public List<T> list() {
		return findMany(createBaseCriteria());		
	}	
	
	/**
	 * Return a single of entity from the database based on the supplied criteria.
	 * @param searchCriteria - Hibernate Criteria object populated with all necessary filters
	 * @return A single entity from the database. 
	 */
	protected T findOne(Criteria searchCriteria) {
		T result = (T)searchCriteria.uniqueResult();
		if(result == null) {
			throw new NoResultException("No results found");
		}
		return result;
	}

	/**
	 * Return a collection of entities from the database based on the supplied criteria.
	 * @param searchCriteria - Hibernate Criteria object populated with all necessary filters
	 * @return A collection of entities from the database. 
	 */
	protected List<T> findMany(Criteria searchCriteria) {
		return (List<T>)searchCriteria.list();
	}


	/**
	 * Gets the currently open Hibernate session in the current transaction.
	 * This this method should be used instead of createSession() or getCurrentSession()
	 * as it will ensure that the current transactional context is used.
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	
	/**
	 * Create a new Criteria instance attached to the current session and
	 * correctly configured for caching.
	 * @param entityClass - the type of entity that is being queries for.
	 */
	public Criteria createBaseCriteria() {
		
		//determine when to cache queries, based on Cache attribute of entity
		boolean cacheQueries = false;
		Cache cacheAnnotation = entityClass.getAnnotation(Cache.class);
		if(cacheAnnotation != null) {
			switch(cacheAnnotation.usage()) {
				case READ_ONLY:  cacheQueries = true;  break;
				case READ_WRITE: cacheQueries = true;  break;
				default:         cacheQueries = false; break;
			}
		}

		Criteria crit = getSession().createCriteria(entityClass);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		crit.setCacheable(cacheQueries);
		//searchCriteria.setCacheRegion("query.myQueryRegion");
		//crit.setMaxResults(DEFAULT_MAX_RESULTS);
		
		return crit;
	}
}
