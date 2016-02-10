package com.vw.visitreporting.service.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.StandardTopicDao;
import com.vw.visitreporting.entity.referencedata.StandardTopic;
import com.vw.visitreporting.service.AbstractVRService;

@Service
public class StandardTopicServiceImpl extends AbstractVRService<StandardTopic> implements StandardTopicService{

	private StandardTopicDao standardTopicDao;
	
	
	public void deleteTopic(Integer id) {
		this.delete(this.getById(id));
	}
	
	
	@Autowired
	public void setStandardTopicDao(StandardTopicDao standardTopicDao){
		this.standardTopicDao = standardTopicDao;
	}
	
	@Override
	public VRDao<StandardTopic> getTDao() {
		// TODO Auto-generated method stub
		return standardTopicDao;
	}

}
