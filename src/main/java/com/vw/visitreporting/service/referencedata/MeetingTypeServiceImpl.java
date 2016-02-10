package com.vw.visitreporting.service.referencedata;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.MeetingTypeDao;
import com.vw.visitreporting.entity.referencedata.MeetingType;
import com.vw.visitreporting.service.AbstractVRService;


@Service
public class MeetingTypeServiceImpl extends AbstractVRService<MeetingType> implements MeetingTypeService{

	@Autowired
	private MeetingTypeDao meetingTypeDao;
	
	public VRDao<MeetingType> getTDao() {
		return meetingTypeDao;
	}
	
	
	/**
	 * Override list function to only return non-disabled meeting types.
	 */
	public List<MeetingType> list(){
		return meetingTypeDao.findEnabledMeetingTypes();
	}

	/**
	 * Meeting types must be marked as diabled instead of deleted to maintain referential
	 * integrity in the database.
	 */
	public String delete(MeetingType t) {
		throw new UnsupportedOperationException("set disabled=true and use the save method instead");
	}
}
