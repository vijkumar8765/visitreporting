package com.vw.visitreporting.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DoNothingValidator implements Validator{


	public boolean supports(Class<?> clazz) {
        return true;
    }

    public void validate(Object draftDocumentDeletionRule, Errors errors) {
        //do nothing
    }

}