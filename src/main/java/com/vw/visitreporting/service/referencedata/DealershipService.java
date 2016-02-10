package com.vw.visitreporting.service.referencedata;

import java.util.List;
import com.vw.visitreporting.entity.referencedata.Action;
import com.vw.visitreporting.entity.referencedata.Agenda;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.Kpi;
import com.vw.visitreporting.entity.referencedata.StandardTopic;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.service.VRService;


public interface DealershipService extends VRService<Dealership>{

	Dealership findByNumber(Integer number);
	
	/**
	 * Search for all dealerships satisfying all of the following search criteria.
	 * Results will be sorted in dealerNumber ascending order.
	 * @param number - the dealer number (if null, no filter applied)
	 * @param name - the name of the dealership, can contain * wildcards (if null, no filter applied)
	 * @param franchiseGroupName - the name of the franchise group of the dealership, can contain * wildcards (if null, no filter applied)
	 * @return a list of dealers (in ascending dealerNumber order) satisfying the search criteria.
	 */
	List<Dealership> findFor(Integer number, String name, String franchiseGroupName);
	
	/**
	 * Get a list of all users who are associated to a given dealership as a contact.
	 * Results will be sorted by organisation, then by displaySequence.
	 * @param number - the dealer number (if null, no filter applied)
	 */
	List<User> getDealershipContacts(Integer number);
	
	/**
	 * Finds all the agendas and visit reports related to a dealership for a given
	 * dealer number. Extra criteria are also applied to filter out old meetings and
	 * meetings that the current user should not have access to. Supplying a false
	 * moreMeetings parameter will filter out meetings that are not directly related
	 * to the current user. 
	 * @param number - the dealer number (if null, no filter applied)
	 * @param moreMeetings - if true will return all meetings associated with the dealership, otherwise, only the meetings associated also with the currently logged in user.
	 */
	List<Agenda> getDealershipMeetings(Integer number, boolean moreMeetings);
	
	/**
	 * Get a list of all KPIs that are associated to a given dealership.
	 * Results will be sorted by organisation, then by displaySequence.
	 * @param number - the dealer number (if null, no filter applied)
	 */
	List<Kpi> getDealershipKpis(Integer number);

	/**
	 * Finds all the actions related to a dealership for a given
	 * dealer number. Extra criteria are also applied to filter out closed actions and
	 * actions that the current user should not have access to. Supplying a false
	 * moreActions parameter will filter out actions that are not directly related
	 * to the current user. 
	 * @param number - the dealer number (if null, no filter applied)
	 * @param moreActions - if true will return all actions associated with the dealership, otherwise, only the actions associated also with the currently logged in user.
	 */
	List<Action> getDealershipActions(Integer number, boolean moreActions);
	
	/**
	 * Gets a list of topics that are relevant to a given dealership.
	 * Results will be sorted by organisation, then by topic title.
	 * @param number - the dealer number (if null, no filter applied)
	 */
	List<StandardTopic> getDealershipTopics(Integer number);
}
