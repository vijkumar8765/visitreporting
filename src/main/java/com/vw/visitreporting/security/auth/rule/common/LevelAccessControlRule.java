package com.vw.visitreporting.security.auth.rule.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vw.visitreporting.common.CollectionUtil;
import com.vw.visitreporting.entity.accesscontrolled.AccessControlled;
import com.vw.visitreporting.entity.accesscontrolled.DealershipAccessControlledEntity;
import com.vw.visitreporting.entity.accesscontrolled.DealershipCollectionAccessControlledEntity;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.GeographicalArea;
import com.vw.visitreporting.entity.referencedata.GeographicalRegion;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.referencedata.enums.Level;
import com.vw.visitreporting.security.auth.rule.AccessControlRule;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;

//TODO this class has not been tested and may need to be worked upon.

//VERY important - if the user has access to any of the passed in dealerships, this class will allow access
//This rule should not be used if access to all delaerships needs to be checked
public class LevelAccessControlRule implements AccessControlRule<AccessControlled>{

	@Override
	public boolean supports(Object entity) {
		if( entity instanceof Dealership
			|| CollectionUtil.isObjectCollectionOfClazzObjects(entity,Dealership.class)
			|| entity instanceof DealershipAccessControlledEntity
			|| entity instanceof DealershipCollectionAccessControlledEntity){
			return true;
		}
		else{
			return false;
		}
	}


	
	public Boolean invokeRule(AccessControlled entity, Function function){
		
		//get the user first
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();
		
		Level userLevel =  user.getPositions().getLevel();
		//user with level 1 can access any dealer, agenda,visit,report,action
		if(userLevel.equals(Level.ONE)){
			return true;
		}
		if(userLevel.equals(Level.TWO)){
			return checkLevel2AccessToDealership(user, entity);
		}
		if(userLevel.equals(Level.THREE)){
			return checkLevel3AccessToDealership(user, entity);
		}
		if(userLevel.equals(Level.FOUR)){
			return checkLevel4AccessToDealership(user, entity);
		}
		if(userLevel.equals(Level.FIVE)){
			return checkLevel5AccessToDealership(user, entity);
		}

		return null;

	}
	
	private boolean checkLevel2AccessToDealership(User user, AccessControlled entity){
		if(entity instanceof Dealership){
			return canUserAccessDealershipBasedOnGeographicalRegions(user, ((Dealership)entity));
		}
		if(CollectionUtil.isObjectCollectionOfClazzObjects(entity,Dealership.class)){
			for(Dealership dealer: (Collection<Dealership>)entity ){
				if(canUserAccessDealershipBasedOnGeographicalRegions(user, dealer)){
					return true;
				}
			}
			return false;
		}
		if(entity instanceof DealershipAccessControlledEntity){
			return canUserAccessDealershipBasedOnGeographicalRegions(user, ((DealershipAccessControlledEntity) entity).getAccessControlledDealership());
		}
		if(entity instanceof DealershipCollectionAccessControlledEntity){
			for(Dealership dealer: ((DealershipCollectionAccessControlledEntity)entity).getAccessControlledDealerships() ){
				if(canUserAccessDealershipBasedOnGeographicalRegions(user, dealer)){
					return true;
				}
			}
			return false;
		}
		else{
			//should not come here
			return false;
		}
		
	}
	
	private boolean checkLevel3AccessToDealership(User user, AccessControlled entity){
		if(entity instanceof Dealership){
			return canUserAccessDealershipBasedOnGeographicalAreas(user, ((Dealership)entity));
		}
		if(CollectionUtil.isObjectCollectionOfClazzObjects(entity,Dealership.class)){
			for(Dealership dealer: (Collection<Dealership>)entity ){
				if(canUserAccessDealershipBasedOnGeographicalAreas(user, dealer)){
					return true;
				}
			}
			return false;
		}
		if(entity instanceof DealershipAccessControlledEntity){
			return canUserAccessDealershipBasedOnGeographicalAreas(user, ((DealershipAccessControlledEntity) entity).getAccessControlledDealership());
		}
		if(entity instanceof DealershipCollectionAccessControlledEntity){
			for(Dealership dealer: ((DealershipCollectionAccessControlledEntity)entity).getAccessControlledDealerships() ){
				if(canUserAccessDealershipBasedOnGeographicalAreas(user, dealer)){
					return true;
				}
			}
			return false;
		}
		else{
			//should not come here
			return false;
		}
		
	}
	
	private boolean checkLevel4AccessToDealership(User user, AccessControlled entity){
		//brand and franchise group control
		if(entity instanceof Dealership){
			return doesUserHaveAccessToDealershipBasedOnFranchiseGroupAndBrand(user,(Dealership)entity);
		}
		if(entity instanceof DealershipAccessControlledEntity){
			return doesUserHaveAccessToDealershipBasedOnFranchiseGroupAndBrand(user,((DealershipAccessControlledEntity)entity).getAccessControlledDealership());
		}	
		
		if(CollectionUtil.isObjectCollectionOfClazzObjects(entity,Dealership.class)){
			for(Dealership dealer: (Collection<Dealership>)entity ){
				if(doesUserHaveAccessToDealershipBasedOnFranchiseGroupAndBrand(user, dealer)){
					return true;
				}
			}
			return false;
		}
		if(entity instanceof DealershipCollectionAccessControlledEntity){
			for(Dealership dealer: ((DealershipCollectionAccessControlledEntity)entity).getAccessControlledDealerships() ){
				if(doesUserHaveAccessToDealershipBasedOnFranchiseGroupAndBrand(user, dealer)){
					return true;
				}
			}
			return false;
		}
		else{
			//should not come here
			return false;
		}
		

	}
	
	
	private boolean checkLevel5AccessToDealership(User user, AccessControlled entity){
		//brand and franchise group control
		if(entity instanceof Dealership){
			return doesUserBelongToDealership(user,(Dealership)entity);
		}
		if(entity instanceof DealershipAccessControlledEntity){
			return doesUserBelongToDealership(user,((DealershipAccessControlledEntity)entity).getAccessControlledDealership());
		}	
		
		if(CollectionUtil.isObjectCollectionOfClazzObjects(entity,Dealership.class)){
			for(Dealership dealer: (Collection<Dealership>)entity ){
				if(doesUserBelongToDealership(user, dealer)){
					return true;
				}
			}
			return false;
		}
		if(entity instanceof DealershipCollectionAccessControlledEntity){
			for(Dealership dealer: ((DealershipCollectionAccessControlledEntity)entity).getAccessControlledDealerships() ){
				if(doesUserBelongToDealership(user, dealer)){
					return true;
				}
			}
			return false;
		}
		else{
			//should not come here
			return false;
		}
		

	}
	
	private boolean doesUserBelongToDealership(User user, Dealership dealer){
		if(user.getDealership() == null){
			return false; // the user doesn't belong to a dealership
		}
		else{
			return (user.getDealership().equals(dealer));
		}
	}

	
	
	private boolean doesUserHaveAccessToDealershipBasedOnFranchiseGroupAndBrand(User user, Dealership dealer){
		return (user.getBrands().contains(dealer.getBrand())
				&& user.getFranchiseGroup().getDealerships().contains(dealer));
	}
	
	private boolean canUserAccessDealershipBasedOnGeographicalRegions(User user, Dealership dealer){
		
		Collection<GeographicalRegion> userGeographicalRegions = user.getGeographicalRegions();
		Collection<GeographicalArea> dealerGeographicalAreas = dealer.getGeographicalAreas();
		Set<GeographicalRegion> dealerGeographicalRegions = new HashSet<GeographicalRegion>();
		for(GeographicalArea aGA: dealerGeographicalAreas){
			dealerGeographicalRegions.add(aGA.getGeographicalRegion());
		}
		
		for(GeographicalRegion userGR: userGeographicalRegions){
			if(dealerGeographicalRegions.contains(userGR)){
				return true;
			}
				
		}
		return false;
	}
	
	
	private boolean canUserAccessDealershipBasedOnGeographicalAreas(User user, Dealership dealer){
		Collection<GeographicalArea> dealerGeographicalAreas = dealer.getGeographicalAreas();
		Collection<GeographicalArea> userGeographicalAreas = user.getGeographicalAreas();
		for(GeographicalArea userGA: userGeographicalAreas){
			if(dealerGeographicalAreas.contains(userGA)){
				return true;
			}
				
		}
		return false;
	}

}
