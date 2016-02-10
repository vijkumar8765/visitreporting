package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.referencedata.Agenda;
import com.vw.visitreporting.entity.referencedata.Dealership;


/**
 * Contract for a type-safe implementation of AbstractVRDao that adds additional search functionality.
 */
public interface AgendaDao extends VRDao<Agenda> {

	/**
	 * Finds all the meetings related to a given dealership. Extra criteria are
	 * also applied to filter out old meetings and meetings that the current user
	 * should not have access to. Supplying a false moreMeetings parameter will
	 * filter out meetings that are not directly related to the current user.
	 * By default, result is sorted by dateOfMeeting (descending)
	 * @param dealer - the dealership to find contacts for.
	 * @param moreMeetings - if false, only meetings relevant to the current user
	 *                       are returned, otherwise, all meetings are returned.
	 * @return a sorted list of agendas related to the given dealership
	 */
	List<Agenda> getDealershipMeetings(Dealership dealer, boolean moreMeetings);
}
