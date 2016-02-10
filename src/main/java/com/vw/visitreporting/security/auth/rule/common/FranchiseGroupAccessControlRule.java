package com.vw.visitreporting.security.auth.rule.common;

import java.util.Collection;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vw.visitreporting.common.CollectionUtil;
import com.vw.visitreporting.entity.accesscontrolled.AccessControlled;
import com.vw.visitreporting.entity.accesscontrolled.DealershipAccessControlledEntity;
import com.vw.visitreporting.entity.accesscontrolled.DealershipCollectionAccessControlledEntity;
import com.vw.visitreporting.entity.referencedata.BusinessArea;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.FranchiseGroup;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.security.auth.rule.AccessControlRule;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;

public class FranchiseGroupAccessControlRule implements AccessControlRule<FranchiseGroup> {

	@Override
	public boolean supports(Object entity) {
		if( entity instanceof FranchiseGroup){
			return true;
		}
		else{
			return false;
		}
	}


	public Boolean invokeRule(FranchiseGroup entity, Function function){
		if(entity == null){
			return null;	
		}
		
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();

		
		//TODO
		//if the user has any brand which the franchise group supports then return true
		//else return false;
		
		return null;
		
	}

}
