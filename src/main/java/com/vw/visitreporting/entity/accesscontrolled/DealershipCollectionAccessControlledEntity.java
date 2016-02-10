package com.vw.visitreporting.entity.accesscontrolled;

import java.util.Collection;

import com.vw.visitreporting.entity.referencedata.Dealership;

public interface DealershipCollectionAccessControlledEntity extends AccessControlled{

	public Collection<Dealership> getAccessControlledDealerships();


}