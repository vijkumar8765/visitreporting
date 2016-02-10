package com.vw.visitreporting.service.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.DraftDocumentDeletionRuleDao;
import com.vw.visitreporting.entity.referencedata.DraftDocumentDeletionRule;
import com.vw.visitreporting.service.AbstractVRService;

@Service
public class DraftDocumentDeletionRuleServiceImpl extends AbstractVRService<DraftDocumentDeletionRule> implements DraftDocumentDeletionRuleService{

	
	private DraftDocumentDeletionRuleDao draftDocumentDeletionRuleDao;
	
	@Autowired
	public void setDraftDocumentDeletionRuleDao(DraftDocumentDeletionRuleDao draftDocumentDeletionRuleDao){
		this.draftDocumentDeletionRuleDao = draftDocumentDeletionRuleDao;
	}
	
	@Override
	public VRDao<DraftDocumentDeletionRule> getTDao() {
		// TODO Auto-generated method stub
		return draftDocumentDeletionRuleDao;
	}


}
