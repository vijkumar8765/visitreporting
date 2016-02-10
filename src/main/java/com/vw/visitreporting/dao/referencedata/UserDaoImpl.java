package com.vw.visitreporting.dao.referencedata;

import java.util.List;

import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.GeographicalArea;
import com.vw.visitreporting.entity.referencedata.GeographicalRegion;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.User;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hsqldb.lib.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.vw.visitreporting.common.DbUtils;
import com.vw.visitreporting.dao.AbstractVRDao;


/**
 * Data access object for the User entity.
 * Provides a type-safe implementation of AbstractVRDao and adds additional search functionality.
 */
@Repository
public class UserDaoImpl extends AbstractVRDao<User> implements UserDao {

	private static final Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);
	
	public User getById(String userId) {
		return super.getById(userId.toLowerCase());
	}


	/**
	 * Gets the contacts for a given dealership.
	 * @param dealer - the dealership to find contacts for.
	 * @return a list of users of this system who are contacts for this dealership
	 */
	public List<User> getDealershipContacts(Dealership dealer) {
		
		//see 6.1.2.2.1 , 6.1.5.1.15 , 6.1.2.2.3 for detail
		
		Criteria crit = createBaseCriteria();
		
		crit.createAlias("positions", "position");
		crit.add(Restrictions.ne("position.visibleWithinContactScreen", (byte)0));

		Disjunction or = Restrictions.disjunction();
		
			crit.createAlias("geographicalAreas", "area", Criteria.LEFT_JOIN);
			for(GeographicalArea area : dealer.getRelatedAreas()) {
				or.add(Restrictions.eq("area.id", area.getId()));
			}
	
			crit.createAlias("geographicalRegions", "region", Criteria.LEFT_JOIN);
			for(GeographicalRegion region : dealer.getRelatedRegions()) {
				or.add(Restrictions.eq("region.id", region.getId()));
			}
	
			or.add(Restrictions.eq("dealership", dealer));
			
			or.add(Restrictions.eq("franchiseGroup", dealer.getFranchiseGroup()));
			
		crit.add(or);
		
		crit.addOrder(Order.asc("organisation.id"));
		crit.addOrder(Order.asc("position.positionSequence"));

		return super.findMany(crit);
	}

	/**
	 * Gets the contacts for a given organisation.
	 * @param organisation - the organisation to find contacts for.
	 * @return a list of users of this system who are contacts for this organisation
	 */
	public List<User> getOrganisationContacts(Organisation organisation) {

		Criteria crit = createBaseCriteria();
		
		crit.add(Restrictions.eq("organisation", organisation));
		
		crit.addOrder(Order.asc("position.positionSequence"));

		return super.findMany(crit);
	}
	
	
	/* (non-Javadoc)
	 * @see com.vw.visitreporting.dao.referencedata.UserDao#getOrganisationContacts(com.vw.visitreporting.entity.referencedata.Organisation, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<User> getOrganisationContacts(Organisation organisation, Integer dealershipNumber, String businessArea, String jobRole, String geographicalArea, Integer level) {
		
		LOG.debug("Class: UserDaoImpl; Method: getOrganisationContacts Entered...");
		
		LOG.debug("organisation Id ==== " + organisation.getId());
		LOG.debug("dealershipNumber ==== " + dealershipNumber);
		LOG.debug("businessArea ==== " + businessArea);
		LOG.debug("jobRole ==== " + jobRole);
		LOG.debug("geographicalArea ==== " + geographicalArea);
		LOG.debug("levels ==== " + level);
		
		Criteria crit = null;
		
			crit = createBaseCriteria();

			crit.add(Restrictions.eq("organisation.id", organisation.getId()));
			crit.add(Restrictions.eq("dealership.id", dealershipNumber));
			
			crit.createAlias("businessAreas", "businessArea", Criteria.LEFT_JOIN);
			crit.add(Restrictions.eq("businessArea.id", 2));
			
			crit.createAlias("positions", "position");
			if( ! StringUtil.isEmpty(jobRole)) {
				if(jobRole.indexOf('*') < 0) {
					crit.add(Restrictions.eq("position.description", jobRole));
				} else {
					jobRole = jobRole.replace('*', '%');
					crit.add(Restrictions.like("position.description", jobRole, MatchMode.ANYWHERE));
				}
			}
			
			crit.createAlias("geographicalAreas", "area", Criteria.LEFT_JOIN);
			if( ! StringUtil.isEmpty(geographicalArea)) {
				if(jobRole.indexOf('*') < 0) {
					crit.add(Restrictions.eq("area.description", geographicalArea));
				} else {
					jobRole = jobRole.replace('*', '%');
					crit.add(Restrictions.like("area.description", geographicalArea, MatchMode.ANYWHERE));
				}
			}
			
			crit.add(Restrictions.eq("position.level",  1));

			crit.addOrder(Order.asc("organisation.id"));
			crit.addOrder(Order.asc("position.positionSequence"));
			
		List<User> users = (List<User>) crit.list();
		System.out.println(users);
				
		return null;

//		return super.findMany(crit);
		
	}
}
