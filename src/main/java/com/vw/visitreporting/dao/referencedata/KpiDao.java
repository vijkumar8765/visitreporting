package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.Kpi;


/**
 * Contract for a type-safe implementation of AbstractVRDao that adds additional search functionality.
 */
public interface KpiDao extends VRDao<Kpi> {

	/**
	 * Finds all the KPIs related to a given dealership.
	 * Results are ordered by organisation and then by displaySequence.
	 * @param dealer - the dealership to find KPIs for.
	 * @return a sorted list of KPIs relating to the given dealer
	 */
	List<Kpi> getDealershipKpis(Dealership dealer);
}
