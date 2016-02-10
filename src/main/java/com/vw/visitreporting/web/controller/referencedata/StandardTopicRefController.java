package com.vw.visitreporting.web.controller.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vw.visitreporting.entity.referencedata.StandardTopicRef;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.StandardTopicRefService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.validator.DoNothingValidator;

@Controller
@RequestMapping("/standardtopicref")
public class StandardTopicRefController extends AbstractVRController<StandardTopicRef> {

	private StandardTopicRefService standardTopicRefService;

	@Autowired
	public void setStandardTopicRefService(StandardTopicRefService standardTopicRefService) {
		this.standardTopicRefService = standardTopicRefService;
	}

	@Override
	public VRService<StandardTopicRef> getTService() {
		return standardTopicRefService;
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
