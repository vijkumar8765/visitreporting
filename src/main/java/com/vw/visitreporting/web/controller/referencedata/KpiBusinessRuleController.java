package com.vw.visitreporting.web.controller.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vw.visitreporting.entity.referencedata.KpiBusinessRule;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.KpiBusinessRuleService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.validator.DoNothingValidator;

@Controller
@RequestMapping("/kpibusinessrule")
public class KpiBusinessRuleController extends AbstractVRController<KpiBusinessRule> {

	private KpiBusinessRuleService kpiBusinessRuleService;

	@Autowired
	public void setKpiBusinessRuleService(KpiBusinessRuleService kpiBusinessRuleService) {
		this.kpiBusinessRuleService = kpiBusinessRuleService;
	}

	@Override
	public VRService<KpiBusinessRule> getTService() {
		return kpiBusinessRuleService;
	}
	
	private DoNothingValidator validator;
	
	@Autowired
	public void setValidator(DoNothingValidator validator){
		this.validator = validator;
	}
	
	public DoNothingValidator getValidator(){
		return validator;
	}
}

