package com.vw.visitreporting.security.auth.rule.common;

import java.util.Collection;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vw.visitreporting.entity.accesscontrolled.BusinessAreaAccessControlledEntity;
import com.vw.visitreporting.entity.accesscontrolled.BusinessAreaCollectionAccessControlledEntity;
import com.vw.visitreporting.entity.referencedata.BusinessArea;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;

public class BusinessAreaAccessControlRule extends AbstractAccessControlRule<Object,BusinessArea> {

	@Override
	protected Collection<BusinessArea> getUserAllowedEntities() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();

		return user.getBusinessAreas();
	}
	
	public boolean supports(Object entity){
		if( super.supports(entity)){
			return true;
		}
		if(entity instanceof BusinessAreaAccessControlledEntity ||  entity instanceof BusinessAreaCollectionAccessControlledEntity){
			return true;
		}
		return false;
	}

	@Override
	public Boolean specificAccessCheck(Object entity, Function function) {

		//TODO - this hasn't been tested yet
		if(entity instanceof BusinessAreaAccessControlledEntity){
			BusinessArea toBeAccessCheckedBusinessArea = ((BusinessAreaAccessControlledEntity)entity).getAccessControlledBusinessArea();

			//is this brand available in the set of brands configured for the user.
			if( getUserAllowedEntities().contains(toBeAccessCheckedBusinessArea) ){
				return true;
			}
			else{
				return false;
			}			
		}
		
		if(entity instanceof BusinessAreaCollectionAccessControlledEntity){
			Collection<BusinessArea> toBeAccessCheckedBusinessAreas = ((BusinessAreaCollectionAccessControlledEntity)entity).getAccessControlledBusinessAreas();

			//is this brand available in the set of brands configured for the user.
			if( getUserAllowedEntities().contains(toBeAccessCheckedBusinessAreas) ){
				return true;
			}
			else{
				return false;
			}			
		}

		return null;
		
	}

}
