package com.vw.visitreporting.dao.referencedata;

import java.util.Map;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.referencedata.DealerKpiTarget;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.Kpi;


public interface DealerKpiTargetDao extends VRDao<DealerKpiTarget> {

	/**
	 * Gets the most recent targets for a given dealership against each KPI.  
	 * @param dealer - the dealership to find KPI targets for.
	 * @return a mapping from KPI to the targets for that kpi for the given dealership
	 */
	Map<Kpi, DealerKpiTarget> getDealershipTargetKPIs(Dealership dealer);
}
