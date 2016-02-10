package com.vw.visitreporting.service.referencedata;

import java.util.List;
import java.util.Map;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dao.referencedata.ActionDao;
import com.vw.visitreporting.dao.referencedata.AgendaDao;
import com.vw.visitreporting.dao.referencedata.DealerKpiActualDao;
import com.vw.visitreporting.dao.referencedata.DealerKpiTargetDao;
import com.vw.visitreporting.dao.referencedata.DealershipDao;
import com.vw.visitreporting.dao.referencedata.KpiDao;
import com.vw.visitreporting.dao.referencedata.StandardTopicDao;
import com.vw.visitreporting.dao.referencedata.UserDao;
import com.vw.visitreporting.dto.PagingDTO;
import com.vw.visitreporting.entity.referencedata.Action;
import com.vw.visitreporting.entity.referencedata.Agenda;
import com.vw.visitreporting.entity.referencedata.DealerKpiActual;
import com.vw.visitreporting.entity.referencedata.DealerKpiTarget;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.Kpi;
import com.vw.visitreporting.entity.referencedata.KpiBusinessRule;
import com.vw.visitreporting.entity.referencedata.StandardTopic;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.service.AbstractVRService;


@Service
public class DealershipServiceImpl extends AbstractVRService<Dealership> implements DealershipService {

	private DealershipDao dealershipDao;
	
	private UserDao userDao;
	
	private AgendaDao agendaDao;

	private KpiDao kpiDao;
	private DealerKpiTargetDao dealerKpiTargetDao;
	private DealerKpiActualDao dealerKpiActualDao;

	private ActionDao actionDao;
	
	private StandardTopicDao standardTopicDao;

	
	/**
	 * Search for all dealerhips satisfiying all of the following search criteria.
	 * Results will be sorted in dealerNumber ascending order.
	 * @param dealerNumber - the dealer number (if null, no filter applied)
	 * @param dealerName - the name of the dealership, can contain * wildcards (if null, no filter applied)
	 * @param franchiseGroupName - the name of the franchise group of the dealership, can contain * wildcards (if null, no filter applied)
	 * @return a list of dealers (in ascending dealerNumber order) satisfying the search criteria.
	 */
	public List<Dealership> findFor(Integer number, String name, String franchiseGroupName) {
		return this.dealershipDao.findfor(number, name, franchiseGroupName, new PagingDTO());
	}
	
	/**
	 * Get a list of all users who are associated to a given dealership as a contact.
	 * Results will be sorted by organisation, then by displaySequence.
	 * @param number - the dealer number (if null, no filter applied)
	 */
	public List<User> getDealershipContacts(Integer number) {
		return userDao.getDealershipContacts(this.dealershipDao.findByNumber(number));
	}

	/**
	 * Finds all the agendas and visit reports related to a dealership for a given
	 * dealer number. Extra criteria are also applied to filter out old meetings and
	 * meetings that the current user should not have access to. Supplying a false
	 * moreMeetings parameter will filter out meetings that are not directly related
	 * to the current user. 
	 * @param number - the dealer number (if null, no filter applied)
	 * @param moreMeetings - if true will return all meetings associated with the dealership, otherwise, only the meetings associated also with the currently logged in user.
	 */
	public List<Agenda> getDealershipMeetings(Integer number, boolean moreMeetings) {
		return agendaDao.getDealershipMeetings(this.dealershipDao.findByNumber(number), moreMeetings);
	}
	
	/**
	 * Get a list of all KPIs that are associated to a given dealership.
	 * Results will be sorted by organisation, then by displaySequence.
	 * @param number - the dealer number (if null, no filter applied)
	 */
	public List<Kpi> getDealershipKpis(Integer number) {
		Dealership dealer = this.dealershipDao.findByNumber(number);
		
		List<Kpi> kpis = kpiDao.getDealershipKpis(dealer);
		
		Map<Kpi, DealerKpiTarget> targets = dealerKpiTargetDao.getDealershipTargetKPIs(dealer);
		Map<Kpi, DealerKpiActual> actuals = dealerKpiActualDao.getDealershipActualKPIs(dealer, kpis);
		
		ExpressionParser parser = new SpelExpressionParser();
		for(Kpi kpi : kpis) {
			kpi.setDealershipTarget(targets.get(kpi));
			kpi.setDealershipActual(actuals.get(kpi));
			
			for(KpiBusinessRule rule : kpi.getKpiBusinessRules()) {
				if(dealer.getBrand() != rule.getBrand()) {
					continue;
				}
				
				Expression exp = parser.parseExpression(rule.getBusinessRule());
				boolean rulePassed = (Boolean)exp.getValue( kpi.getDealershipActual() );
				if(rulePassed) {
					kpi.setDealershipActualColour(rule.getVarianceColour());
					break;
				}
			}
			
			Hibernate.initialize(kpi.getBusinessAreas());
			Hibernate.initialize(kpi.getKpiCategory());
		}
		return kpis;
	}
	
	
	/**
	 * Finds all the actions related to a dealership for a given
	 * dealer number. Extra criteria are also applied to filter out closed actions and
	 * actions that the current user should not have access to. Supplying a false
	 * moreActions parameter will filter out actions that are not directly related
	 * to the current user. 
	 * @param number - the dealer number (if null, no filter applied)
	 * @param moreActions - if true will return all actions associated with the dealership, otherwise, only the actions associated also with the currently logged in user.
	 */
	public List<Action> getDealershipActions(Integer number, boolean moreActions) {
		return actionDao.getDealershipActions(this.dealershipDao.findByNumber(number), moreActions);
	}
	
	/**
	 * Gets a list of topics that are relevant to a given dealership.
	 * Results will be sorted by organisation, then by topic title.
	 * @param number - the dealer number (if null, no filter applied)
	 */
	public List<StandardTopic> getDealershipTopics(Integer number) {
		return standardTopicDao.getDealershipTopics(this.dealershipDao.findByNumber(number));
	}
	
	
	@Autowired
	public void setDealershipDao(DealershipDao dealershipDao){
		this.dealershipDao = dealershipDao;
	}

	@Autowired
	public void setUserDao(UserDao userDao){
		this.userDao = userDao;
	}
	
	@Autowired
	public void setAgendaDao(AgendaDao agendaDao){
		this.agendaDao = agendaDao;
	}

	@Autowired
	public void setKpiDao(KpiDao kpiDao){
		this.kpiDao = kpiDao;
	}

	@Autowired
	public void setDealerKpiTargetDao(DealerKpiTargetDao dealerKpiTargetDao){
		this.dealerKpiTargetDao = dealerKpiTargetDao;
	}

	@Autowired
	public void setDealerKpiActualDao(DealerKpiActualDao dealerKpiActualDao){
		this.dealerKpiActualDao = dealerKpiActualDao;
	}

	@Autowired
	public void setActionDao(ActionDao actionDao){
		this.actionDao = actionDao;
	}

	@Autowired
	public void setStandardTopicDao(StandardTopicDao standardTopicDao){
		this.standardTopicDao = standardTopicDao;
	}

	
	public VRDao<Dealership> getTDao() {
		return dealershipDao;
	}

	public Dealership findByNumber(Integer number) {
		Dealership dealer = dealershipDao.findByNumber(number);
		Hibernate.initialize(dealer.getFranchiseGroup().getDealerships());
		return dealer;
	}
}
