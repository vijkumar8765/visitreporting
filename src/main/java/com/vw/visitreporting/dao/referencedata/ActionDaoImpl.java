package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.entity.referencedata.Action;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.enums.ActionStatus;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;


/**
 * Data access object for the Action entity.
 * Provides a type-safe implementation of AbstractVRDao and adds additional search functionality.
 */
@Repository
public class ActionDaoImpl extends AbstractVRDao<Action> implements ActionDao {

	/**
	 * Finds all the actions related to a dealership for a given
	 * dealer number. Extra criteria are also applied to filter out closed actions and
	 * actions that the current user should not have access to. Supplying a false
	 * moreActions parameter will filter out actions that are not directly related
	 * to the current user. 
	 * @param dealer - the dealership to find actions for.
	 * @param moreActions - if true will return all actions associated with the dealership, otherwise, only the actions associated also with the currently logged in user.
	 */
	public List<Action> getDealershipActions(Dealership dealer, boolean moreActions) {
		
		String userId = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && auth.getPrincipal() != null) {
			userId = ((UserDetailsImpl)auth.getPrincipal()).getUser().getUserId();
		}
		if(userId == null) {
			throw new AuthenticationCredentialsNotFoundException("currently logged-in user could not be determined");
		}
		
		
		Criteria crit = createBaseCriteria();
		
		crit.createAlias("dealerships", "dealer1", Criteria.LEFT_JOIN);
		crit.createAlias("franchiseGroups", "group", Criteria.LEFT_JOIN);
		crit.createAlias("group.dealerships", "dealer2", Criteria.LEFT_JOIN);
		crit.createAlias("owners", "owner", Criteria.LEFT_JOIN);
		//crit.createAlias("accessGrants", "grant");
		
		Disjunction or1 = Restrictions.disjunction();
		
			or1.add(Restrictions.eq("dealer1.id", dealer.getId()));
			or1.add(Restrictions.eq("dealer2.id", dealer.getId()));
	
		crit.add(or1);
		
		crit.add(Restrictions.ne("status", ActionStatus.CLOSED.getId()));

		if(moreActions) {

			Disjunction or2 = Restrictions.disjunction();
			
				or2.add(Restrictions.eq("createdBy", userId));
				or2.add(Restrictions.eq("owner.userId", userId));
				or2.add(Restrictions.eq("confidential", (byte)0));
				//or2.add(Restrictions.eq("grant.userId", userId));
		
			crit.add(or2);
			
		} else {

			Disjunction or2 = Restrictions.disjunction();
			
				or2.add(Restrictions.eq("createdBy", userId));
				or2.add(Restrictions.eq("owner.userId", userId));
		
			crit.add(or2);

			Disjunction or3 = Restrictions.disjunction();
			
				or3.add(Restrictions.eq("confidential", (byte)0));
				//or3.add(Restrictions.eq("grant.userId", userId));
		
			crit.add(or3);
		}
		crit.addOrder(Order.desc("dueDate"));

		return super.findMany(crit);		
	}
}
