package com.vw.visitreporting.entity;

import java.util.Collection;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.FranchiseGroup;


/**
 * Provides an interface for entities to implement when they are related to one
 * or more dealers to allow these relations to be quickly obtained.
 */
public interface DealerRelated {

	/**
	 * Gets a collection of dealer instances that this entity instance is related to.
	 */
	Collection<Dealership> getRelatedDealers();

	/**
	 * Gets a collection of franchise group instances that this entity instance is related to.
	 */
	Collection<FranchiseGroup> getRelatedFranchiseGroups();
}
