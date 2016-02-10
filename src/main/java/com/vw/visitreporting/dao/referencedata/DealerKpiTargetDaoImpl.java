package com.vw.visitreporting.dao.referencedata;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.entity.referencedata.DealerKpiTarget;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.Kpi;
import com.vw.visitreporting.entity.referencedata.enums.KpiStatus;


/**
 * Data access object for the DealerKpiTarget entity.
 */
@Repository
public class DealerKpiTargetDaoImpl extends AbstractVRDao<DealerKpiTarget> implements DealerKpiTargetDao {


	/**
	 * Gets the most recent targets for a given dealership against each KPI.  
	 * @param dealer - the dealership to find KPI targets for.
	 * @return a mapping from KPI to the targets for that kpi for the given dealership
	 */
	public Map<Kpi, DealerKpiTarget> getDealershipTargetKPIs(Dealership dealer) {
		
		Calendar cal = GregorianCalendar.getInstance(Locale.getDefault());
		
		Criteria crit = createBaseCriteria();
		crit.createAlias("kpi", "kpiObj");
		crit.createAlias("kpiObj.kpiBrands", "kpiBrand");
		
		crit.add(Restrictions.eq("dealership", dealer));

		crit.add(Restrictions.eq("year", (short)cal.get(Calendar.YEAR)));

		crit.add(Restrictions.eq("kpiBrand.brandId", dealer.getBrand().getId()));

		crit.add(Restrictions.ne("kpiObj.status", KpiStatus.TERMINATED.getId()));

		List<DealerKpiTarget> list = super.findMany(crit);
		
		
		Map<Kpi, DealerKpiTarget> result = new HashMap<Kpi, DealerKpiTarget>();
		for(DealerKpiTarget kpiTarget : list) {
			result.put(kpiTarget.getKpi(), kpiTarget);
		}
		return result;		
	}
}
