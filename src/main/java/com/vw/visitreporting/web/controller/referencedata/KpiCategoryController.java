package com.vw.visitreporting.web.controller.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vw.visitreporting.entity.referencedata.KpiCategory;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.KpiCategoryService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.validator.DoNothingValidator;

@Controller
@RequestMapping("/kpicategory")
public class KpiCategoryController extends AbstractVRController<KpiCategory> {

	private KpiCategoryService kpiCategoryService;

	@Autowired
	public void setKpiCategoryService(KpiCategoryService kpiCategoryService) {
		this.kpiCategoryService = kpiCategoryService;
	}

	@Override
	public VRService<KpiCategory> getTService() {
		return kpiCategoryService;
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