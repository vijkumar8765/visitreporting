package com.vw.visitreporting.web.controller.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vw.visitreporting.entity.referencedata.MessageBoardNotificationRule;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.MessageBoardNotificationRuleService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.validator.DoNothingValidator;

@Controller
@RequestMapping("/messageboardnotificationrule")
public class MessageBoardNotificationRuleController extends AbstractVRController<MessageBoardNotificationRule> {

	private MessageBoardNotificationRuleService messageBoardNotificationRuleService;

	@Autowired
	public void setMessageBoardNotificationRuleService(MessageBoardNotificationRuleService messageBoardNotificationRuleService) {
		this.messageBoardNotificationRuleService = messageBoardNotificationRuleService;
	}

	@Override
	public VRService<MessageBoardNotificationRule> getTService() {
		return messageBoardNotificationRuleService;
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