package com.vw.visitreporting.web.controller.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.vw.visitreporting.entity.referencedata.DraftDocumentDeletionRule;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.DraftDocumentDeletionRuleService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.validator.DoNothingValidator;


@Controller
@RequestMapping("/draftdocumentdeletionrule")
public class DraftDocumentDeletionRuleController extends AbstractVRController<DraftDocumentDeletionRule> {

	private DraftDocumentDeletionRuleService draftDocumentDeletionRuleService;

	@Autowired
	public void setDraftDocumentDeletionRuleService(DraftDocumentDeletionRuleService draftDocumentDeletionRuleService) {
		this.draftDocumentDeletionRuleService = draftDocumentDeletionRuleService;
	}

	@Override
	public VRService<DraftDocumentDeletionRule> getTService() {
		return draftDocumentDeletionRuleService;
	}	private DoNothingValidator validator;
	
	@Autowired
	public void setValidator(DoNothingValidator validator){
		this.validator = validator;
	}
	
	public DoNothingValidator getValidator(){
		return validator;
	}
}

