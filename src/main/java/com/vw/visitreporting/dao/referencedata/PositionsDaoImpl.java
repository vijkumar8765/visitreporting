package com.vw.visitreporting.dao.referencedata;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.entity.referencedata.Positions;
import com.vw.visitreporting.entity.referencedata.User;

/**
 * Data access object for the Positions entity.
 * Provides a type-safe implementation of AbstractVRDao that adds additional search functionality
 * and restricts access to some of the CRUD operations based on user's privileges.
 */
@Repository
@Transactional(propagation=Propagation.MANDATORY)
public class PositionsDaoImpl extends AbstractVRDao<Positions> implements PositionsDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		return SessionFactoryUtils.getSession(this.sessionFactory, false);
	}
	
	/**
	 * Find all positions in the database which are not disabled.
	 */
	@SuppressWarnings("unchecked")
	public List<Positions> findEnabledPositions() {
		Criteria crit = createBaseCriteria();
		crit.add(Restrictions.eq("disabled", Byte.valueOf((byte)0)));
		return crit.list();
		
	}
	
	/**
	 * Access to Function.EDIT_POSITION required to perform this operation.
	 */
	//@PreAuthorize("hasPermission(null, 'EDIT_POSITION')")
	public Positions save(Positions positions) {
		return super.save(positions);
	}

	/**
	 * The below method will check if the provided position is already associated with at least one user in the system.
	 */
	public boolean isPositionUsersAssociated(Positions entity){
		Criteria crit = getSession().createCriteria(User.class);
        crit.add(Restrictions.eq("positions", entity));
        crit.setProjection(Projections.rowCount());
        if((Long)crit.uniqueResult() != 0){
        	return true;
        }
		return false;
	}

	/**
	 * The below method will check whether the position is already exists in the system.
	 *  It will use description, organisation and brandcode (unique combination of brands) to determine the existence.
	 */
	@Override
	public boolean isPositionExists(Positions entity) {
		Criteria crit = getSession().createCriteria(Positions.class);
		crit.add(Restrictions.eq("description", entity.getDescription()));
		crit.add(Restrictions.eq("organisation", entity.getOrganisation()));
		crit.add(Restrictions.eq("brandcode", entity.getBrandcode()));
		crit.setProjection(Projections.rowCount());
		if((Long)crit.uniqueResult() == 0){
        	return false;
        }
		return true;
	}

	/**
	 * The below method will check whether the position sequence for a given Organisation already exists in the system.
	 *  
	 */
	@Override
	public boolean isPositionSequenceExists(Positions entity) {
		Criteria crit = getSession().createCriteria(Positions.class);
		crit.add(Restrictions.eq("organisation", entity.getOrganisation()));
		crit.add(Restrictions.eq("positionSequence", entity.getPositionSequence()));
		crit.setProjection(Projections.rowCount());
		if((Long)crit.uniqueResult() == 0){
        	return false;
        }
		return true;
	}

	/**
	 * Returns true only if the desired position description is being changed during a Position Update.
	 * The below check is required to fulfil one of the business requirement of replacing position name and/or position sequence.
	 */
	@Override
	public boolean isPositionDescriptionBeingChanged(Positions entity) {
		Criteria crit = getSession().createCriteria(Positions.class);
		crit.add(Restrictions.eq("description", entity.getDescription()));
		crit.add(Restrictions.eq("id", entity.getId()));
		crit.setProjection(Projections.rowCount());
		if((Long)crit.uniqueResult() == 0){
        	return true;
        }
		return false;
	}
}
