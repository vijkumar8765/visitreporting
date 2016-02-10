package com.vw.visitreporting.web.controller.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vw.visitreporting.entity.referencedata.BusinessArea;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.BusinessAreaService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.validator.BusinessAreaValidator;

@Controller
@RequestMapping("/businessarea")
public class BusinessAreaController extends AbstractVRController<BusinessArea> {

	private BusinessAreaService businessAreaService;
	
	private BusinessAreaValidator validator;

	@Autowired
	public void setBusinessAreaService(BusinessAreaService businessAreaService) {
		this.businessAreaService = businessAreaService;
	}
	
	@Autowired
	public void setValidator(BusinessAreaValidator businessAreaValidator){
		this.validator = businessAreaValidator;
	}
	
	public BusinessAreaValidator getValidator(){
		return validator;
	}

	@Override
	public VRService<BusinessArea> getTService() {
		return businessAreaService;
	}
}
