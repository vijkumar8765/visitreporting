package com.vw.visitreporting.dao.referencedata;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.referencedata.BusinessArea;

public interface BusinessAreaDao extends VRDao<BusinessArea> {

	/**
	 * Provide special find functionality to return an BusinessArea entity by its name.
	 * @param name - the name of the BusinessArea, as it appears in the database
	 * @return a single instance matching the name
	 * @throws NoResultException if less than one result is found
	 * @throws NonUniqueResultException if more than one result is found
	 */
	BusinessArea findByName(String name);
	
}
