package com.vw.visitreporting.service.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.KpiBusinessRuleDao;
import com.vw.visitreporting.entity.referencedata.KpiBusinessRule;
import com.vw.visitreporting.service.AbstractVRService;

@Service
public class KpiBusinessRuleServiceImpl extends AbstractVRService<KpiBusinessRule> implements KpiBusinessRuleService{

	private KpiBusinessRuleDao kpiBusinessRuleDao;
	
	@Autowired
	public void setKpiBusinessRuleDao(KpiBusinessRuleDao kpiBusinessRuleDao){
		this.kpiBusinessRuleDao = kpiBusinessRuleDao;
	}
	
	@Override
	public VRDao<KpiBusinessRule> getTDao() {
		// TODO Auto-generated method stub
		return kpiBusinessRuleDao;
	}

}
