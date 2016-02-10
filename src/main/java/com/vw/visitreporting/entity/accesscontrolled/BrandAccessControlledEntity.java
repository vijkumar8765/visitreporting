package com.vw.visitreporting.entity.accesscontrolled;

import com.vw.visitreporting.entity.referencedata.enums.Brand;

public interface BrandAccessControlledEntity extends AccessControlled{

	public Brand getAccessControlledBrand();

}
