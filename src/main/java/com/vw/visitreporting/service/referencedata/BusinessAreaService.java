package com.vw.visitreporting.service.referencedata;

import com.vw.visitreporting.entity.referencedata.BusinessArea;
import com.vw.visitreporting.service.VRService;

public interface BusinessAreaService extends VRService<BusinessArea>{

	BusinessArea findByName(String name);
}
