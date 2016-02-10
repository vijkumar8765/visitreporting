package com.vw.visitreporting.security.auth.rule.common;

import java.util.Collection;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vw.visitreporting.entity.accesscontrolled.GeographicalStructureCategoryAccessControlledEntity;
import com.vw.visitreporting.entity.accesscontrolled.GeographicalStructureCategoryCollectionAccessControlledEntity;
import com.vw.visitreporting.entity.referencedata.GeographicalStructureCategory;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;

public class GeographicalStructureAccessControlRule extends AbstractAccessControlRule<Object,GeographicalStructureCategory>{

	@Override
	protected Collection<GeographicalStructureCategory> getUserAllowedEntities() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();

		return user.getGeographicalStructureCategories();
	}
	
	public boolean supports(Object entity){
		if( super.supports(entity)){
			return true;
		}
		if(entity instanceof GeographicalStructureCategoryAccessControlledEntity ||  entity instanceof GeographicalStructureCategoryCollectionAccessControlledEntity){
			return true;
		}
		return false;
	}

	@Override
	public Boolean specificAccessCheck(Object entity, Function function) {

		//TODO - this hasn't been tested yet
		if(entity instanceof GeographicalStructureCategoryAccessControlledEntity){
			GeographicalStructureCategory toBeAccessCheckedGeographicalStructureCategory = ((GeographicalStructureCategoryAccessControlledEntity)entity).getAccessControlledGeographicalStructureCategory();

			//is this brand available in the set of brands configured for the user.
			if( getUserAllowedEntities().contains(toBeAccessCheckedGeographicalStructureCategory) ){
				return true;
			}
			else{
				return false;
			}			
		}
		
		if(entity instanceof GeographicalStructureCategoryCollectionAccessControlledEntity){
			Collection<GeographicalStructureCategory> toBeAccessCheckedGeographicalStructureCategories = ((GeographicalStructureCategoryCollectionAccessControlledEntity)entity).getAccessControlledGeographicalStructureCategories();

			//is this brand available in the set of brands configured for the user.
			if( getUserAllowedEntities().contains(toBeAccessCheckedGeographicalStructureCategories) ){
				return true;
			}
			else{
				return false;
			}			
		}

		return null;
		
	}

}
