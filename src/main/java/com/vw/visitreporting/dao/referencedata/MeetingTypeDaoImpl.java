package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.entity.referencedata.MeetingType;


/**
 * Data access object for the MeetingType entity.
 * Provides a type-safe implementation of AbstractVRDao that adds additional search functionality
 * and restricts access to some of the CRUD operations based on user's privileges.
 */
@Repository
public class MeetingTypeDaoImpl extends AbstractVRDao<MeetingType> implements MeetingTypeDao {

	/**
	 * Find all meeting types in the database which are not disabled.
	 */
	@SuppressWarnings("unchecked")
	public List<MeetingType> findEnabledMeetingTypes() {
		Criteria crit = createBaseCriteria();
		crit.add(Restrictions.eq("disabled", Byte.valueOf((byte)0)));
		return crit.list();
	}
	
	/**
	 * Access to Function.EDIT_MEETING_TYPE required to perform this operation.
	 */
	@PreAuthorize("hasPermission(null, 'EDIT_MEETING_TYPE')")
	public MeetingType save(MeetingType meetingType) {
		return super.save(meetingType);
	}

	/**
	 * Meeting types must be marked as diabled instead of deleted to maintain referential
	 * integrity in the database].
	 */
	public String delete(MeetingType t) {
		throw new UnsupportedOperationException("use the save method instead");
	}
}
