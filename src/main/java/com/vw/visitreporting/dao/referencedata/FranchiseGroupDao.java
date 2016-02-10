package com.vw.visitreporting.dao.referencedata;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.referencedata.FranchiseGroup;


/**
 * Contract for a type-safe implementation of AbstractVRDao that adds additional search functionality.
 */
public interface FranchiseGroupDao extends VRDao<FranchiseGroup> {

	/**
	 * Provide special find functionality to return an FranchiseGroup entity by its name.
	 * @param name - the name of the FranchiseGroup, as it appears in the database
	 * @return a single instance matching the name
	 * @throws NoResultException if less than one result is found
	 * @throws NonUniqueResultException if more than one result is found
	 */
	FranchiseGroup findByName(String name);

}