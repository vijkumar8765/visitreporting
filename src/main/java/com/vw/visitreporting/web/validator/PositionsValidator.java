package com.vw.visitreporting.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.vw.visitreporting.common.validator.VRCommonValidator;
import com.vw.visitreporting.entity.referencedata.Positions;
import com.vw.visitreporting.service.referencedata.OrganisationService;
import com.vw.visitreporting.service.referencedata.PositionsService;

/**
 * Provides validation rules for the Positions that cannot be expressed (or do
 * not give readable error messages) by jsr-303 and also business validation
 */
@Component
public class PositionsValidator implements Validator {

	@Autowired
	private PositionsService positionsService;
	
	@Autowired
	private OrganisationService organisationService;


	@Override
	public boolean supports(Class<?> positionsClass) {
		return Positions.class.equals(positionsClass);
	}

	@Override
	public void validate(Object positionEntity, Errors errors) {

		Positions position = (Positions) positionEntity;
		
		// JSR-303 poorly handles empty and length validation checks, so to avoid multiple error messages below validation is required.
		if(position.getDescription().trim().isEmpty() || position.getDescription().trim().length() < 2 || position.getDescription().length() > 200){
			errors.rejectValue("description", null, "Please enter a value between 2 - 200 characters.");
		}else 
		
			//	The below validation is to check for any Position that already exists with organisation and Brands values. This check is only for Create New item, not for update.
			if(positionsService.isPositionExists(position) && position.getId() == null){
			errors.rejectValue("description", null, "This position already exists in the system for selected Organisation and Brand(s). Please give another name.");
		}else 
			
			// Before update/create position, we must validate if the User selected Brand(s) are valid for selected Organisation.
			if(!VRCommonValidator.isOrgBrandCombinationValid(organisationService.getOrganisation(position.getOrganisation().getId())   , position.getBrands())){
			errors.rejectValue("brands", null, "The selected Brand(s) is/are not valid for the selected Organisation. Please change Brand.");
		} else 
			
			// Before update/create position, we must validate if the User selected Level is valid for selected Organisation.
			if(!VRCommonValidator.isOrgLevelCombinationValid(organisationService.getOrganisation(position.getOrganisation().getId()), position.getLevel())){
			//errors.rejectValue("level", null, "The selected Level is not valid for the selected Organisation. Please change Level.");
		} else

		// If the field position sequence has any binding errors (e.g. type mismatch), bypass the mandatory check and let the user correct the error
		// before doing business validations. If the field has no binding errors then only check for business validation.
		// If the field 'Visible within contact screen' is selected, then only position sequence becomes mandatory. Requirement Ref - 6.1.6.4.4
		if(!errors.hasFieldErrors("positionSequence") && position.getVisibleWithinContactScreen() && position.getPositionSequence() == null){
			errors.rejectValue("positionSequence", null, "Position sequence is required since this position is visible within Contact Screen.");
		}else 
			
			// Below check is to validate if the user supplied position sequence already exists in the system for given Organisation.  Requirement Ref - 6.1.6.4.7 
			if(!errors.hasFieldErrors("positionSequence") && position.getVisibleWithinContactScreen() && 
					positionsService.isPositionSequenceExists(position)&& position.getId() == null){
			errors.rejectValue("positionSequence", null, "The sequence exists for selected Organisation. Please change it");
		} else
			if( !errors.hasFieldErrors("positionSequence") && position.getVisibleWithinContactScreen() && 
					positionsService.isPositionSequenceExists(position)&& position.getId() != null && !positionsService.isPositionDescriptionBeingChanged(position)){
				errors.rejectValue("positionSequence", null, "The sequence exists for selected Organisation. Please change it");
			}
	}

}
