package com.vw.visitreporting.dao.referencedata;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.entity.referencedata.FranchiseGroup;


/**
 * Data access object for the OrganisationFunction entity.
 * Provides a type-safe implementation of AbstractVRDao and adds additional search functionality.
 */
@Repository
public class FranchiseGroupDaoImpl extends AbstractVRDao<FranchiseGroup> implements FranchiseGroupDao {

	/**
	 * Provide special find functionality to return an FranchiseGroup entity by its name.
	 * @param name - the name of the FranchiseGroup, as it appears in the database
	 * @return a single instance matching the name
	 * @throws NoResultException if less than one result is found
	 * @throws NonUniqueResultException if more than one result is found
	 */
	public FranchiseGroup findByName(String name) {
		
		Criteria crit = createBaseCriteria();
		crit.add(Restrictions.eq("name", name));
		
		return super.findOne(crit);
	}
}
