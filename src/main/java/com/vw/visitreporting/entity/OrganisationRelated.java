package com.vw.visitreporting.entity;

import com.vw.visitreporting.entity.referencedata.Organisation;


/**
 * Provides an interface for entities to implement when they are related to
 * an organisation to allow these relations to be quickly obtained.
 */
public interface OrganisationRelated {

	/**
	 * Gets the Organisation that this entity instance is related to.
	 */
	Organisation getRelatedOrganisation();
}
