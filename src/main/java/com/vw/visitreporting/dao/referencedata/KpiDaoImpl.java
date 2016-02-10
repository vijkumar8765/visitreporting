package com.vw.visitreporting.dao.referencedata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.entity.referencedata.BusinessArea;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.Kpi;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.KpiStatus;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;


/**
 * Data access object for the Kpi entity.
 * Provides a type-safe implementation of AbstractVRDao and adds additional search functionality.
 */
@Repository
@Transactional(propagation=Propagation.MANDATORY)
public class KpiDaoImpl extends AbstractVRDao<Kpi> implements KpiDao{

	/**
	 * Finds all the KPIs related to a given dealership.
	 * Results are ordered by organisation and then by displaySequence.
	 * @param dealer - the dealership to find contacts for.
	 * @return a sorted list of KPIs relating to the given dealer
	 */
	public List<Kpi> getDealershipKpis(Dealership dealer) {
		
		//see 6.1.2.5.1 for detail
		
		Criteria crit = createBaseCriteria();
		
		crit.createAlias("kpiBrands", "kpiBrand");
		crit.add(Restrictions.eq("kpiBrand.brandId", dealer.getBrand().getId()));

		crit.add(Restrictions.ne("status", KpiStatus.TERMINATED.getId()));
		
		crit.addOrder(Order.asc("organisation.id"));
		crit.addOrder(Order.asc("displaySequence"));
		
		return super.findMany(crit);		
	}
	
	
	@Override
	public List<Kpi> list() {	
		
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();		
		
		//Get appropriate business areas of logged in user
		Set<BusinessArea> currentUserBusAreas = user.getBusinessAreas();
		Collection<Integer> currentUserBusAreasIds = new ArrayList<Integer>();
		//Map<String, Integer> currentUserBusAreasIds = new HashMap<String, Integer>();
		if(currentUserBusAreas != null){
			for (BusinessArea businessArea : currentUserBusAreas) {
				currentUserBusAreasIds.add(businessArea.getId());
				//currentUserBusAreasIds.put("id", businessArea.getId());
			}
		}	
		
		Set<Brand> currentUserBrands = user.getBrands();
		Collection<Integer> currentUserBrandIds = new ArrayList<Integer>();
		//Map<String, Integer> currentUserBrandIds = new HashMap<String, Integer>();
		if(currentUserBrands != null){
			for (Brand brand : currentUserBrands) {
				currentUserBrandIds.add(brand.getId());
				//currentUserBrandIds.put("brandId", brand.getId());
			}
		}	
		
		Criteria crit = createBaseCriteria();
		crit.add(Restrictions.eq("organisation.id", user.getOrganisation().getId()));		
		//--crit.createCriteria("businessAreas").add(Restrictions.in("id",currentUserBusAreasIds));
		//crit.createCriteria("businessAreas").add(Restrictions.allEq(currentUserBusAreasIds));		
		//--crit.createCriteria("kpiBrands").add(Restrictions.in("brandId", currentUserBrandIds));
		//crit.createCriteria("kpiBrands").add(Restrictions.allEq(currentUserBrandIds));
		
		crit.addOrder(Order.asc("organisation.id"));
		crit.addOrder(Order.asc("displaySequence"));
		return super.findMany(crit);
	}
	
}
