package com.vw.visitreporting.service.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.FranchiseGroupDao;
import com.vw.visitreporting.entity.referencedata.FranchiseGroup;
import com.vw.visitreporting.service.AbstractVRService;


@Service
public class FranchiseGroupServiceImpl extends AbstractVRService<FranchiseGroup> implements FranchiseGroupService {

	private FranchiseGroupDao franchiseGroupDao;
	
	@Autowired
	public void setFranchiseGroupDao(FranchiseGroupDao franchiseGroupDao){
		this.franchiseGroupDao = franchiseGroupDao;
	}
	
	public VRDao<FranchiseGroup> getTDao() {
		return franchiseGroupDao;
	}

	public FranchiseGroup findByName(String name) {
		return franchiseGroupDao.findByName(name);
	}
}
