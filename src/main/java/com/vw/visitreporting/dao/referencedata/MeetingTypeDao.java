package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.referencedata.MeetingType;

public interface MeetingTypeDao extends VRDao<MeetingType> {

	/**
	 * Find all meeting types in the database which are not disabled.
	 */
	List<MeetingType> findEnabledMeetingTypes();
}
