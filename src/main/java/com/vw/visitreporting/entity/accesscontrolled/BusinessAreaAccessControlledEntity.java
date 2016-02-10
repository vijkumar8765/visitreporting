package com.vw.visitreporting.entity.accesscontrolled;

import com.vw.visitreporting.entity.referencedata.BusinessArea;

public interface BusinessAreaAccessControlledEntity extends AccessControlled{

	public BusinessArea getAccessControlledBusinessArea();

}
