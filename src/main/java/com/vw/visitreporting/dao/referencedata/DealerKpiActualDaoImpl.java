package com.vw.visitreporting.dao.referencedata;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.entity.referencedata.DealerKpiActual;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.Kpi;
import com.vw.visitreporting.entity.referencedata.enums.KpiStatus;


/**
 * Data access object for the DealerKpiActual entity.
 */
@Repository
public class DealerKpiActualDaoImpl extends AbstractVRDao<DealerKpiActual> implements DealerKpiActualDao {

	/**
	 * Gets the most recent scores for a given dealership against each KPI.  
	 * @param dealer - the dealership to find KPI scores for.
	 * @return a mapping from KPI to the scores for that kpi for the given dealership
	 */
	public Map<Kpi, DealerKpiActual> getDealershipActualKPIs(Dealership dealer, List<Kpi> kpis) {
		
		Calendar cal = GregorianCalendar.getInstance(Locale.getDefault());
		
		Criteria crit1 = createBaseCriteria();
		crit1.createAlias("kpi", "kpiObj");
		crit1.createAlias("kpiObj.kpiBrands", "kpiBrand");
		
		crit1.add(Restrictions.eq("dealership", dealer));
		
		crit1.add(Restrictions.eq("kpiBrand.brandId", dealer.getBrand().getId()));

		crit1.add(Restrictions.eq("kpiObj.status", KpiStatus.ACTIVE.getId()));

		crit1.add(Restrictions.eq("scoreMonth", (byte)cal.get(Calendar.MONTH)));

		crit1.add(Restrictions.eq("scoreYear", (short)cal.get(Calendar.YEAR)));

		List<DealerKpiActual> list1 = super.findMany(crit1);
		
		
		Map<Kpi, DealerKpiActual> result = new HashMap<Kpi, DealerKpiActual>();
		for(DealerKpiActual kpiActual : list1) {
			result.put(kpiActual.getKpi(), kpiActual);
		}
		for(Kpi kpi : kpis) {
			if( ! result.containsKey(kpi)) {
				
				Criteria crit2 = createBaseCriteria();
				
				crit2.add(Restrictions.eq("dealership", dealer));

				crit2.add(Restrictions.eq("kpi", kpi));
				
				crit2.addOrder(Order.desc("scoreYear"));
				crit2.addOrder(Order.desc("scoreMonth"));
				
				crit2.setFetchSize(1);
				List<DealerKpiActual> kpiActualResult = super.findMany(crit2);
				if(kpiActualResult.size() == 1) {
					result.put(kpi, kpiActualResult.get(0));
				}
			}
		}
		
		return result;
	}
}
