package com.vw.visitreporting.entity.accesscontrolled;

import com.vw.visitreporting.entity.referencedata.Dealership;

public interface DealershipAccessControlledEntity extends AccessControlled{

	public Dealership getAccessControlledDealership();

}