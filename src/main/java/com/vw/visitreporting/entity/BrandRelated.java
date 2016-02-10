package com.vw.visitreporting.entity;

import java.util.EnumSet;
import com.vw.visitreporting.entity.referencedata.enums.Brand;


/**
 * Provides an interface for entities to implement when they are related to one
 * or more brands to allow these relations to be quickly obtained.
 */
public interface BrandRelated {

	/**
	 * Gets a collection of brands that this entity instance is related to.
	 */
	EnumSet<Brand> getRelatedBrands();
}
