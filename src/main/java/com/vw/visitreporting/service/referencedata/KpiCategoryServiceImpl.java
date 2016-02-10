package com.vw.visitreporting.service.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.KpiCategoryDao;
import com.vw.visitreporting.entity.referencedata.KpiCategory;
import com.vw.visitreporting.service.AbstractVRService;

@Service
public class KpiCategoryServiceImpl extends AbstractVRService<KpiCategory> implements KpiCategoryService{

	private KpiCategoryDao kpiCategoryDao;
	
	@Autowired
	public void setKpiCategoryDao(KpiCategoryDao kpiCategoryDao){
		this.kpiCategoryDao = kpiCategoryDao;
	}
	
	@Override
	public VRDao<KpiCategory> getTDao() {
		// TODO Auto-generated method stub
		return kpiCategoryDao;
	}

}
