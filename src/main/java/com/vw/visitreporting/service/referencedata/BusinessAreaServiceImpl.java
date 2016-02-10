package com.vw.visitreporting.service.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.BusinessAreaDao;
import com.vw.visitreporting.entity.referencedata.BusinessArea;
import com.vw.visitreporting.service.AbstractVRService;

@Service
public class BusinessAreaServiceImpl extends AbstractVRService<BusinessArea> implements BusinessAreaService{

	private BusinessAreaDao businessAreaDao;
	
	@Autowired
	public void setBusinessAreaDao(BusinessAreaDao businessAreaDao){
		this.businessAreaDao = businessAreaDao;
	}
	
	@Override
	public VRDao<BusinessArea> getTDao() {
		// TODO Auto-generated method stub
		return businessAreaDao;
	}

	public BusinessArea findByName(String name) {
		return businessAreaDao.findByName(name);
	}
}
