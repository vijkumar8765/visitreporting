package com.vw.visitreporting.web.controller.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vw.visitreporting.entity.referencedata.StandardTopic;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.StandardTopicService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.validator.DoNothingValidator;

@Controller
@RequestMapping("/standardtopic")
public class StandardTopicController extends AbstractVRController<StandardTopic> {

	private StandardTopicService standardTopicService;

	@Autowired
	public void setStandardTopicService(StandardTopicService standardTopicService) {
		this.standardTopicService = standardTopicService;
	}

	@Override
	public VRService<StandardTopic> getTService() {
		return standardTopicService;
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
