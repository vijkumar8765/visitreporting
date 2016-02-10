package com.vw.visitreporting.service.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.MessageBoardNotificationRuleDao;
import com.vw.visitreporting.entity.referencedata.MessageBoardNotificationRule;
import com.vw.visitreporting.service.AbstractVRService;

@Service
public class MessageBoardNotificationRuleServiceImpl extends AbstractVRService<MessageBoardNotificationRule> implements MessageBoardNotificationRuleService{

	private MessageBoardNotificationRuleDao messageBoardNotificationRuleDao;
	
	@Autowired
	public void setMessageBoardNotificationRuleDao(MessageBoardNotificationRuleDao messageBoardNotificationRuleDao){
		this.messageBoardNotificationRuleDao = messageBoardNotificationRuleDao;
	}
	
	@Override
	public VRDao<MessageBoardNotificationRule> getTDao() {
		// TODO Auto-generated method stub
		return messageBoardNotificationRuleDao;
	}

}
