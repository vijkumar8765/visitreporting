package com.vw.visitreporting.service.referencedata;

import com.vw.visitreporting.entity.referencedata.Kpi;
import com.vw.visitreporting.service.VRService;

public interface KpiService extends VRService<Kpi>{

	public Kpi associateKpiBrands(Kpi entity);
	
	public Kpi loadDynamicEntities(Kpi entity);
	
}
