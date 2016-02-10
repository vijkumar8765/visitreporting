package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hsqldb.lib.StringUtil;
import org.springframework.stereotype.Repository;
import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.dto.PagingDTO;
import com.vw.visitreporting.entity.referencedata.Dealership;


/**
 * Data access object for the Dealership entity.
 * Provides a type-safe implementation of AbstractVRDao and adds additional search functionality.
 */
@Repository
public class DealershipDaoImpl extends AbstractVRDao<Dealership> implements DealershipDao {

	/**
	 * Provide special find functionality to return an Dealer entity by its number.
	 * @param number - the number of the Dealer
	 * @return a single instance matching the number
	 * @throws NoResultException if less than one result is found
	 * @throws NonUniqueResultException if more than one result is found
	 */
	public Dealership findByNumber(Integer number) {
		
		Criteria crit = createBaseCriteria();
		crit.add(Restrictions.eq("dealerNumber", number));
		
		return super.findOne(crit);
	}

	
	/**
	 * Search for all dealerhips satisfiying all of the following search criteria.
	 * If no sorting parameters are supplied, results will be sorted in dealerNumber ascending order.
	 * @param dealerNumber - the dealer number (if null, no filter applied)
	 * @param dealerName - the name of the dealership, can contain * wildcards (if null, no filter applied)
	 * @param franchiseGroupName - the name of the franchise group of the dealership, can contain * wildcards (if null, no filter applied)
	 * @param paging - the paging/sorting settings for the query result
	 * @return a list of dealers (in the order specified in the paging settings) satifying the search criteria.
	 */
	public List<Dealership> findfor(Integer dealerNumber, String dealerName, String franchiseGroupName, PagingDTO paging) {
		
		Criteria crit = createBaseCriteria();
		
		if(dealerNumber != null) {
			crit.add(Restrictions.eq("dealerNumber", dealerNumber));
		}
		if( ! StringUtil.isEmpty(dealerName)) {
			if(dealerName.indexOf('*') < 0) {
				crit.add(Restrictions.eq("dealerName", dealerName));
			} else {
				dealerName = dealerName.replace('*', '%');
				crit.add(Restrictions.like("dealerName", dealerName, MatchMode.ANYWHERE));
			}
		}
		if( ! StringUtil.isEmpty(franchiseGroupName)) {
			crit.createAlias("franchiseGroup", "group");
			if(franchiseGroupName.indexOf('*') < 0) {
				crit.add(Restrictions.eq("group.name", franchiseGroupName));
			} else {
				franchiseGroupName = franchiseGroupName.replace('*', '%');
				crit.add(Restrictions.like("group.name", franchiseGroupName, MatchMode.ANYWHERE));
			}
		}
		return super.findMany(crit);
	}
}
