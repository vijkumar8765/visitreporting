package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import java.util.Map;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.referencedata.DealerKpiActual;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.Kpi;


public interface DealerKpiActualDao extends VRDao<DealerKpiActual> {

	/**
	 * Gets the most recent scores for a given dealership against each KPI.  
	 * @param dealer - the dealership to find KPI scores for.
	 * @param kpis - collection of all non-terminated KPIs related to the given dealer
	 * @return a mapping from KPI to the scores for that kpi for the given dealership
	 */
	Map<Kpi, DealerKpiActual> getDealershipActualKPIs(Dealership dealer, List<Kpi> kpis);
}
