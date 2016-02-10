package com.vw.visitreporting.entity.accesscontrolled;

import java.util.Collection;

import com.vw.visitreporting.entity.referencedata.enums.Brand;

public interface BrandCollectionAccessControlledEntity extends AccessControlled{
	
	public Collection<Brand> getAccessControlledBrands();

}
