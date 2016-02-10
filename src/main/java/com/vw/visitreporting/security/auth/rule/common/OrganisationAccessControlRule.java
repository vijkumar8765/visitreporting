package com.vw.visitreporting.security.auth.rule.common;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vw.visitreporting.entity.accesscontrolled.AccessControlled;
import com.vw.visitreporting.entity.accesscontrolled.OrganisationAndBrandControlledEntity;
import com.vw.visitreporting.entity.accesscontrolled.OrganisationControlledEntity;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.SharingRule;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.security.auth.rule.AccessControlRule;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;

public class OrganisationAccessControlRule implements AccessControlRule<AccessControlled>{

	@Override
	public boolean supports(Object entity) {
		if( entity instanceof Organisation 
			|| entity instanceof OrganisationAndBrandControlledEntity
			|| entity instanceof OrganisationControlledEntity){
			return true;
		}
		else{
			return false;
		}
	}


	public Boolean invokeRule(AccessControlled entity, Function function){
		if(entity == null){
			return null;	
		}
		
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();

		
		if(entity instanceof Organisation){
			if(user.getOrganisation().equals(entity) ){
				return true;
			}
		}
		
		
		else if(entity instanceof OrganisationControlledEntity){
			//the following code relies on sharing rules assumption
			if(user.getAllowedOrganisations().contains(   ((OrganisationControlledEntity)entity).getOrganisation()   )    ){
				return true;
			}
			else{
				return false;
			}
		}


		else if(entity instanceof OrganisationAndBrandControlledEntity){
			if(isOrganisationAndBrandShared(user, (OrganisationAndBrandControlledEntity)entity)){
				return true;
			}
			else{
				return false;
			}
		}
		
		return null;
		
	}
	
	private boolean isOrganisationAndBrandShared(User user, OrganisationAndBrandControlledEntity entity){
		for(SharingRule aSharingRule: entity.getOrganisation().getSharingRules()){
			if(aSharingRule.getRootBrand().equals(entity.getBrand())){
				//lets find any matching target organisation and brand pair
				if(user.getOrganisation().equals(aSharingRule.getTargetOrganisation()) && user.getBrands().contains(aSharingRule.getTargetBrand())){
					return true;
				}
			}
		}
		return false;	
	}

}
