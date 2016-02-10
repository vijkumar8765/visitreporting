package com.vw.visitreporting.common;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.READ_UNCOMMITTED)
public class GenericService<T> {
	
	@Autowired
	private GenericDao<T> genericDao;
	
	public T getById(Serializable id, Class<T> clazz){		
		return genericDao.getById(id, clazz);
	}

}
