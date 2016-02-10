package com.vw.visitreporting.entity.accesscontrolled;

import java.util.Collection;

import com.vw.visitreporting.entity.referencedata.BusinessArea;

public interface BusinessAreaCollectionAccessControlledEntity {

	public Collection<BusinessArea> getAccessControlledBusinessAreas();

}
