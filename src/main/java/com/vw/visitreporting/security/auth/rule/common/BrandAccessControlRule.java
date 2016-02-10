package com.vw.visitreporting.security.auth.rule.common;

import java.util.Collection;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vw.visitreporting.common.CollectionUtil;
import com.vw.visitreporting.entity.accesscontrolled.BrandAccessControlledEntity;
import com.vw.visitreporting.entity.accesscontrolled.BrandCollectionAccessControlledEntity;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;

public class BrandAccessControlRule extends AbstractAccessControlRule<Object,Brand> {

	@Override
	public boolean supports(Object entity){
		if(super.supports(entity)){
			return true;
		}
		else if(entity instanceof BrandAccessControlledEntity || entity instanceof BrandCollectionAccessControlledEntity){
			return true;
		}
		return false;
	}
	
	@Override
	protected Collection<Brand> getUserAllowedEntities() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();

		return user.getBrands();
	}

	@Override
	public Boolean specificAccessCheck(Object entity, Function function) {
		//TODO - this hasn't been tested yet
		if(entity instanceof BrandAccessControlledEntity){
			Brand toBeAccessCheckedBrand = ((BrandAccessControlledEntity)entity).getAccessControlledBrand();

			//is this brand available in the set of brands configured for the user.
			if( getUserAllowedEntities().contains(toBeAccessCheckedBrand) ){
				return true;
			}
			else{
				return false;
			}			
		}
		
		if(entity instanceof BrandCollectionAccessControlledEntity){
			Collection<Brand> toBeAccessCheckedBrands = ((BrandCollectionAccessControlledEntity)entity).getAccessControlledBrands();

			//is this brand available in the set of brands configured for the user.
			if( getUserAllowedEntities().contains(toBeAccessCheckedBrands) ){
				return true;
			}
			else{
				return false;
			}			
		}

		return null;
		
	}

}
