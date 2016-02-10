package com.vw.visitreporting.common;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.READ_UNCOMMITTED)
public class GenericDao<T> {
	
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public T getById(Serializable id, Class<T> clazz){		
		
		if(id instanceof Long) {
			return (T)getSession().get(clazz, Long.valueOf(id.toString()));
		}else {
			return (T)getSession().get(clazz, Integer.valueOf(id.toString()));
		}
	}
	
	/**
	 * Gets the currently open Hibernate session in the current transaction.
	 * This this method should be used instead of createSession() or getCurrentSession()
	 * as it will ensure that the current transactional context is used.
	 */
	public Session getSession() {
		return sessionFactory.openSession();
	}	
	
}
