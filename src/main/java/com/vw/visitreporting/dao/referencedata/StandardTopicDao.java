package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.StandardTopic;


public interface StandardTopicDao extends VRDao<StandardTopic>{

	/**
	 * Gets a list of topics that are relevant to a given dealership.
	 * Results will be sorted by organisation, then by topic title.
	 * @param dealer - the dealer to get the topics for
	 */
	List<StandardTopic> getDealershipTopics(Dealership dealer);
}
