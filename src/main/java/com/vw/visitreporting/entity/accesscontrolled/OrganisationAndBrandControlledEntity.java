package com.vw.visitreporting.entity.accesscontrolled;

import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.enums.Brand;

public interface OrganisationAndBrandControlledEntity extends AccessControlled{
	
	public Organisation getOrganisation();
	public Brand getBrand();
}
