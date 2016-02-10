package com.vw.visitreporting.dao;

import java.io.Serializable;
import java.util.List;
import com.vw.visitreporting.entity.BaseVREntity;


/**
 * Specifies the contract for the generic DAO to implement CRUD operations for Hibernate entities.
 * @param <T> the type of entity that the sub-class instance of this class works with.
 */
public interface VRDao<T extends BaseVREntity> {

	/**
	 * Get an entity from the database.
	 * @param id - the primary key of the entity to return
	 * @return a single entity specified by the primary key or null if an entity could not be found 
	 */
	T getById(Serializable id);

	/**
	 * Saved an entity into the database. If the entity is new a new row is inserted,
	 * otherwise an existing row is updated. The created*,modified* properties of the
	 * entity are also updated appropriately. 
	 */
	T save(T t);

	/**
	 * Deletes an entity from the database.
	 * @param t - the persistent object or a transient object with the primary key set.
	 */
	String delete(T t);

	/**
	 * Return all entities from the database of this type.
	 */
	List<T> list();
}
