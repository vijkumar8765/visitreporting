package com.vw.visitreporting.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.vw.visitreporting.entity.referencedata.DraftDocumentDeletionRule;

public class DraftDocumentDeletionRuleValidator implements Validator{


    @SuppressWarnings("rawtypes")
	public boolean supports(Class draftDocumentDeletionRuleClass) {
        return DraftDocumentDeletionRule.class.equals(draftDocumentDeletionRuleClass);
    }

    public void validate(Object draftDocumentDeletionRule, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name","xyzcvcvcvdcdidu");
    }

}