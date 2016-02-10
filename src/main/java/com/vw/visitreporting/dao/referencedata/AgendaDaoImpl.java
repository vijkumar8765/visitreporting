package com.vw.visitreporting.dao.referencedata;

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import com.vw.visitreporting.dao.AbstractVRDao;
import com.vw.visitreporting.entity.referencedata.Agenda;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.enums.DocumentStatus;
import com.vw.visitreporting.security.authentication.UserDetailsImpl;


/**
 * Data access object for the Agenda entity.
 * Provides a type-safe implementation of AbstractVRDao and adds additional search functionality.
 */
@Repository
public class AgendaDaoImpl extends AbstractVRDao<Agenda> implements AgendaDao {

	@Value("#{${config.meetingDispayPeriodInDays}}")
	private int meetingDispayPeriodInDays;
	
	
	/**
	 * Finds all the meetings related to a given dealership. Extra criteria are
	 * also applied to filter out old meetings and meetings that the current user
	 * should not have access to. Supplying a false moreMeetings parameter will
	 * filter out meetings that are not directly related to the current user.
	 * The result is sorted by dateOfMeeting (descending)
	 * @param dealer - the dealership to find meetings for.
	 * @param moreMeetings - if false, only meetings relevant to the current user
	 *                       are returned, otherwise, all meetings are returned.
	 * @return a sorted list of agendas related to the given dealership
	 */
	public List<Agenda> getDealershipMeetings(Dealership dealer, boolean moreMeetings) {//NOPMD
		
		//see 6.1.2.4.1 , 6.1.2.4.2 , 6.1.2.4.3 , 6.1.2.4.4 , 6.1.2.4.5 , 6.1.2.4.8 for detail

		//get date of earliest meeting to return
		Date meetingDisplayDate = new Date(new Date().getTime() -  meetingDispayPeriodInDays * 24*60*60*1000);
		
		//get currently logged in user
		String userId = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && auth.getPrincipal() != null) {
			userId = ((UserDetailsImpl)auth.getPrincipal()).getUser().getUserId();
		}
		if(userId == null) {
			throw new AuthenticationCredentialsNotFoundException("currently logged-in user could not be determined");
		}
		
		Criteria crit = createBaseCriteria();
		crit.createAlias("visitReport", "report", Criteria.LEFT_JOIN);

		// agenda.dealers->exists(dealer)
		crit.createAlias("dealerships", "dealer", Criteria.LEFT_JOIN);
		crit.add(Restrictions.eq("dealer.id", dealer.getId()));
		
		// agenda.createdDate > (now - MEETING_DISPLAY_PERIOD)
		crit.add(Restrictions.ge("createdDate", meetingDisplayDate));
		
		Disjunction or1 = Restrictions.disjunction();
		
			//if user is creator/organiser
			or1.add(Restrictions.eq("createdBy", userId));
			or1.add(Restrictions.eq("meetingOrganiserUserId", userId));
			
			//or if the agenda/visit report is published and user is creator/organiser/invitee/..
			Conjunction and1_2 = Restrictions.conjunction();

				// (agenda.status = PUBLISHED | agenda.visitReport.status = PUBLISHED)
				Disjunction or1_2_1 = Restrictions.disjunction();
				
					or1_2_1.add(Restrictions.eq("statusId", DocumentStatus.PUBLISHED.getId()));
					or1_2_1.add(Restrictions.eq("report.statusId", DocumentStatus.PUBLISHED.getId()));

				and1_2.add(or1_2_1);
				
				//    ! agenda.confidential
			    //  | agenda.creator = user
			    //  | agenda.meetingOrganiser = user
			    //  | agenda.invitees->exists(user)
			    //  | agenda.visitReport.attendees->exists(user)
			    //  | agenda.visitReport.actions.owners->exists(user)
			    //  | agenda.accessGrants.user->exists(user)
			    //  | agenda.visitReport.accessGrants.user->exists(user)
				Disjunction or1_2_2 = Restrictions.disjunction();
				
					crit.createAlias("invitees", "invitee", Criteria.LEFT_JOIN);
					crit.createAlias("report.attendees", "attendee", Criteria.LEFT_JOIN);
					crit.createAlias("report.actions", "action", Criteria.LEFT_JOIN);
					crit.createAlias("action.owners", "actionOwner", Criteria.LEFT_JOIN);
//					crit.createAlias("accessGrants", "grant", Criteria.LEFT_JOIN);
//					crit.createAlias("report.accessGrants", "vrGrant", Criteria.LEFT_JOIN);

				if(moreMeetings) {
					or1_2_2.add(Restrictions.eq("confidential", Byte.valueOf((byte)0)));
				}
					or1_2_2.add(Restrictions.eq("createdBy", userId));
					or1_2_2.add(Restrictions.eq("meetingOrganiserUserId", userId));
					or1_2_2.add(Restrictions.eq("invitee.userId", userId));
					or1_2_2.add(Restrictions.eq("attendee.userId", userId));
					or1_2_2.add(Restrictions.eq("actionOwner.userId", userId));
//					or1_2_2.add(Restrictions.eq("grant", userId));
//					or1_2_2.add(Restrictions.eq("vrGrant", userId));
					
				and1_2.add(or1_2_2);
			
			or1.add(and1_2);
		
		crit.add(or1);
		
		crit.addOrder(Order.desc("dateOfMeeting"));
		
		return super.findMany(crit);
	}
}
