package com.vw.visitreporting.entity.accesscontrolled;

import java.util.Collection;

import com.vw.visitreporting.entity.referencedata.GeographicalStructureCategory;

public interface GeographicalStructureCategoryCollectionAccessControlledEntity extends AccessControlled{

	public Collection<GeographicalStructureCategory> getAccessControlledGeographicalStructureCategories();
}
