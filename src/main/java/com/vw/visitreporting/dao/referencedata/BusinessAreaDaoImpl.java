package com.vw.visitreporting.dao.referencedata;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.entity.referencedata.BusinessArea;


@Repository
public class BusinessAreaDaoImpl extends AbstractVRDao<BusinessArea> implements BusinessAreaDao{

	/**
	 * Provide special find functionality to return an BusinessArea entity by its name.
	 * @param name - the name of the BusinessArea, as it appears in the database
	 * @return a single instance matching the name
	 * @throws NoResultException if less than one result is found
	 * @throws NonUniqueResultException if more than one result is found
	 */
	public BusinessArea findByName(String name) {
		
		Criteria crit = createBaseCriteria();
		crit.add(Restrictions.eq("name", name));
		
		return super.findOne(crit);
	}
}
