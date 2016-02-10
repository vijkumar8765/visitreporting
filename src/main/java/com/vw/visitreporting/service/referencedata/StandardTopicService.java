package com.vw.visitreporting.service.referencedata;

import com.vw.visitreporting.entity.referencedata.StandardTopic;
import com.vw.visitreporting.service.VRService;

public interface StandardTopicService extends VRService<StandardTopic>{

	void deleteTopic(Integer id);
}
