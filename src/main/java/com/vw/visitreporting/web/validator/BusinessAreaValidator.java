package com.vw.visitreporting.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.vw.visitreporting.entity.referencedata.BusinessArea;

@Component
public class BusinessAreaValidator implements Validator{


	    @SuppressWarnings("rawtypes")
		public boolean supports(Class businessAreaClass) {
	        return BusinessArea.class.equals(businessAreaClass);
	    }

	    public void validate(Object businessArea, Errors errors) {
	        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name","businessarea.name");
	    }

	}
