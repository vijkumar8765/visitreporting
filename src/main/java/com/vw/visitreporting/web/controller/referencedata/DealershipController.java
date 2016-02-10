package com.vw.visitreporting.web.controller.referencedata;

import java.util.List;
import javax.validation.Valid;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.Kpi;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.DealershipService;
import com.vw.visitreporting.service.referencedata.StandardTopicService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.form.DealershipSearchForm;
import com.vw.visitreporting.web.validator.DoNothingValidator;


@Controller
@RequestMapping("/dealership")
public class DealershipController extends AbstractVRController<Dealership> {

	private DealershipService dealershipService;
	
	private StandardTopicService standardTopicService;

	private DoNothingValidator validator;

	
	/**
	 * Landing page for the dealer search screen.
	 * @param model - a new empty instance of DealershipSearchForm will be added with name "command"
	 * @return always returns to view: dealership/search
	 */
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String searchLandingPage(Model model) {
		model.addAttribute("command", new DealershipSearchForm());
		return "dealership/search";
	}

	/**
	 * Performs a dealer search based on various criteria such as dealer number/name or franchise group name.
	 * @param formBean - a form bean populated with search criteria from the dealer search page
	 * @param bindingResult - any binding/jsr validation errors found when binding the request content
	 * @param model - no attributes will be added (the submitted form bean is already present in the model)
	 * @return always returns to view: dealership/search
	 */
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String searchForDealerships(@ModelAttribute("command") @Valid DealershipSearchForm formBean, BindingResult bindingResult, Model model) {
		
        //return to form if there were validation errors
        if(bindingResult.hasErrors()) {
            return "dealership/search";
        }
		
		Integer number = null;
		if( ! StringUtil.isEmpty(formBean.getDealershipNumber())) {
			number = Integer.valueOf(formBean.getDealershipNumber());
		}
		
		List<Dealership> results = dealershipService.findFor(number, formBean.getDealershipName(), formBean.getFranchiseGroupName());
		
		formBean.setSearchResults(results);
		
		return "dealership/search";
	}


	/**
	 * Landing page for the dealer information screens.
	 * This request handler will simply redirect to the page for the default tab - contacts 
	 * @param number - the dealer number of the dealership to display the information of
	 * @param model - no attributes will get added to the model
	 * @return redirect to default dealer information tab page
	 */
	@RequestMapping(value="/{number}/view", method=RequestMethod.GET)
	public String viewDealershipDetails(@PathVariable Integer number, Model model) {
		return "redirect:/content/dealership/"+number+"/contacts";
	}

	/**
	 * Displays the dealer information page with the contacts tab selected.
	 * @param number - the dealer number of the dealership to display the information of
	 * @param model - dealership matching the dealer number, selectedTab and list of contacts (User entities) will be added to the model
	 * @return always display view name: dealership/contacts
	 */
	@RequestMapping(value="/{number}/contacts", method=RequestMethod.GET)
	public String viewDealershipContacts(@PathVariable Integer number, Model model) {
		model.addAttribute("dealership", dealershipService.findByNumber(number));
		model.addAttribute("contacts", dealershipService.getDealershipContacts(number));
		model.addAttribute("selectedTab", "CONTACTS");
		return "dealership/contacts";
	}

	/**
	 * Displays the dealer information page with the information tab selected.
	 * @param number - the dealer number of the dealership to display the information of
	 * @param model - dealership matching the dealer number and selectedTab will be added to the model
	 * @return always display view name: dealership/info
	 */
	@RequestMapping(value="/{number}/info", method=RequestMethod.GET)
	public String viewDealershipInformation(@PathVariable Integer number, Model model) {
		model.addAttribute("dealership", dealershipService.findByNumber(number));
		model.addAttribute("selectedTab", "INFO");
		return "dealership/info";
	}

	/**
	 * Displays the dealer information page with the meetings tab selected, and showing
	 * only meetings related to the currently logged in user.
	 * @param number - the dealer number of the dealership to display the information of
	 * @param model - dealership matching the dealer number, selectedTab, moreMeetings=false, and
	 *                list of meetings (Agenda entities) will be added to the model
	 * @return always display view name: dealership/meetings
	 */
	@RequestMapping(value="/{number}/mymeetings", method=RequestMethod.GET)
	public String viewLessDealershipMeetings(@PathVariable Integer number, Model model) {
		return viewDealershipMeetings(number, false, model);
	}
	
	/**
	 * Displays the dealer information page with the meetings tab selected, and showing
	 * all meetings related to this dealership.
	 * @param number - the dealer number of the dealership to display the information of
	 * @param model - dealership matching the dealer number, selectedTab, moreMeetings=true, and
	 *                list of meetings (Agenda entities) will be added to the model
	 * @return always display view name: dealership/meetings
	 */
	@RequestMapping(value="/{number}/meetings", method=RequestMethod.GET)
	public String viewMoreDealershipMeetings(@PathVariable Integer number, Model model) {
		return viewDealershipMeetings(number, true, model);
	}
	
	private String viewDealershipMeetings(Integer number, boolean moreMeetings, Model model) {
		model.addAttribute("dealership", dealershipService.findByNumber(number));
		model.addAttribute("meetings", dealershipService.getDealershipMeetings(number, moreMeetings));
		model.addAttribute("moreMeetings", moreMeetings);
		model.addAttribute("selectedTab", "MEETINGS");
		return "dealership/meetings";
	}
	
	/**
	 * Displays the dealer information page with the performance tab selected.
	 * @param number - the dealer number of the dealership to display the information of
	 * @param model - dealership matching the dealer number, selectedTab and list of KPIs (Kpi entities) will be added to the model
	 * @return always display view name: dealership/performance
	 */
	@RequestMapping(value="/{number}/performance", method=RequestMethod.GET)
	public String viewDealershipKPIs(@PathVariable Integer number, Model model) {
		List<Kpi> kpis = dealershipService.getDealershipKpis(number);
		model.addAttribute("dealership", dealershipService.findByNumber(number));
		model.addAttribute("kpis", kpis);
		if( ! kpis.isEmpty() && kpis.get(0).getDealershipActual() != null) {
			model.addAttribute("totalActualScore", kpis.get(0).getDealershipActual().getTotalActualScore());
			model.addAttribute("nationalLeaguePosition", kpis.get(0).getDealershipActual().getNationalLeaguePosition());
		}
		model.addAttribute("selectedTab", "PERFORMANCE");
		return "dealership/performance";
	}

	/**
	 * Displays the dealer information page with the actions tab selected, and showing
	 * only actions related to the currently logged in user.
	 * @param number - the dealer number of the dealership to display the information of
	 * @param model - dealership matching the dealer number, selectedTab, moreActions=false, and
	 *                list of actions (Action entities) will be added to the model
	 * @return always display view name: dealership/actions
	 */
	@RequestMapping(value="/{number}/myactions", method=RequestMethod.GET)
	public String viewLessDealershipActions(@PathVariable Integer number, Model model) {
		return viewDealershipActions(number, false, model);
	}
	
	/**
	 * Displays the dealer information page with the actions tab selected, and showing
	 * all actions related to this dealership.
	 * @param number - the dealer number of the dealership to display the information of
	 * @param model - dealership matching the dealer number, selectedTab, moreActions=true, and
	 *                list of actions (Action entities) will be added to the model
	 * @return always display view name: dealership/actions
	 */
	@RequestMapping(value="/{number}/actions", method=RequestMethod.GET)
	public String viewMoreDealershipActions(@PathVariable Integer number, Model model) {
		return viewDealershipActions(number, true, model);
	}
	
	private String viewDealershipActions(Integer number, boolean moreActions, Model model) {
		model.addAttribute("dealership", dealershipService.findByNumber(number));
		model.addAttribute("actions", dealershipService.getDealershipActions(number, moreActions));
		model.addAttribute("moreActions", moreActions);
		model.addAttribute("selectedTab", "ACTIONS");
		return "dealership/actions";
	}

	/**
	 * Displays the dealer information page with the topics tab selected.
	 * @param number - the dealer number of the dealership to display the information of
	 * @param model - dealership matching the dealer number, selectedTab and list of topics (StandardTopic entities) will be added to the model
	 * @return always display view name: dealership/topics
	 */
	@RequestMapping(value="/{number}/topics", method=RequestMethod.GET)
	public String viewDealershipTopics(@PathVariable Integer number, Model model) {
		model.addAttribute("dealership", dealershipService.findByNumber(number));
		model.addAttribute("topics", dealershipService.getDealershipTopics(number));
		model.addAttribute("selectedTab", "TOPICS");
		return "dealership/topics";
	}	

/*	@RequestMapping(value="/{number}/topics/{id}/delete", method=RequestMethod.GET)
	public String deleteDealershipTopic(@PathVariable Integer number, @PathVariable Integer id, Model model) {
		standardTopicService.deleteTopic(id);
        return "redirect:/content/dealership/"+number+"/topics";
	}	
*/
    
	public VRService<Dealership> getTService() {
		return dealershipService;
	}
	
	@Autowired
	public void setDealershipService(DealershipService dealershipService) {
		this.dealershipService = dealershipService;
	}
	
	@Autowired
	public void setStandardTopicService(StandardTopicService standardTopicService) {
		this.standardTopicService = standardTopicService;
	}

	protected Validator getValidator() {
		return validator;
	}

	@Autowired
	public void setValidator(DoNothingValidator validator) {
		this.validator = validator;
	}
}
