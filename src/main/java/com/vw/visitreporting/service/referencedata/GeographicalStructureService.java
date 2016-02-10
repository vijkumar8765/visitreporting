package com.vw.visitreporting.service.referencedata;

import com.vw.visitreporting.entity.referencedata.GeographicalArea;
import com.vw.visitreporting.entity.referencedata.GeographicalRegion;
import com.vw.visitreporting.entity.referencedata.GeographicalStructureCategory;
import com.vw.visitreporting.service.VRService;


public interface GeographicalStructureService {

	/**
	 * Create or update a GeographicalStructureCategory entity in the database.
	 * @return the (possibly different) hibernate entity instance for this entity.
	 */
	GeographicalStructureCategory saveCategory(GeographicalStructureCategory category);
	
	/**
	 * Create or update a GeographicalRegion entity in the database.
	 * @return the (possibly different) hibernate entity instance for this entity.
	 */
	GeographicalRegion saveRegion(GeographicalRegion region);
	
	/**
	 * Create or update a GeographicalArea entity in the database.
	 * @return the (possibly different) hibernate entity instance for this entity.
	 */
	GeographicalArea saveArea(GeographicalArea area);
}
