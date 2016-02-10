package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.StandardTopic;


/**
 * Data access object for the StandardTopic entity.
 * Provides a type-safe implementation of AbstractVRDao and adds additional search functionality.
 */
@Repository
public class StandardTopicDaoImpl extends AbstractVRDao<StandardTopic> implements StandardTopicDao {

	/**
	 * Gets a list of topics that are relevant to a given dealership.
	 * Results will be sorted by organisation, then by topic title.
	 * @param dealer - the dealer to get the topics for
	 */
	public List<StandardTopic> getDealershipTopics(Dealership dealer) {
		
		//see 6.1.2.7.1, 6.1.2.7.3 for details
		
		Criteria crit = createBaseCriteria();
		
		crit.createAlias("standardTopicRef", "topicRef");
		
		crit.setFetchMode("brands", FetchMode.JOIN);
		crit.add(Restrictions.eq("brands", dealer.getBrand().getId()));

		crit.addOrder(Order.asc("organisation.id"));
		crit.addOrder(Order.asc("topicRef.title"));
		
		return super.findMany(crit);		
	}
}
