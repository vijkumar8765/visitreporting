package com.vw.visitreporting.service.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.StandardTopicRefDao;
import com.vw.visitreporting.entity.referencedata.StandardTopicRef;
import com.vw.visitreporting.service.AbstractVRService;

@Service
public class StandardTopicRefServiceImpl extends AbstractVRService<StandardTopicRef> implements StandardTopicRefService{

	private StandardTopicRefDao standardTopicRefDao;
	
	@Autowired
	public void setStandardTopicRefDao(StandardTopicRefDao standardTopicRefDao){
		this.standardTopicRefDao = standardTopicRefDao;
	}
	
	@Override
	public VRDao<StandardTopicRef> getTDao() {
		// TODO Auto-generated method stub
		return standardTopicRefDao;
	}

}
