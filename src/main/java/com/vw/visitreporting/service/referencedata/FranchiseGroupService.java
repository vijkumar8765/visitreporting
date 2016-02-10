package com.vw.visitreporting.service.referencedata;

import com.vw.visitreporting.entity.referencedata.FranchiseGroup;
import com.vw.visitreporting.service.VRService;


public interface FranchiseGroupService extends VRService<FranchiseGroup>{

	FranchiseGroup findByName(String name);
}
