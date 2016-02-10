package com.vw.visitreporting.web.controller.referencedata;

import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.SharingRule;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.referencedata.enums.Level;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.BusinessAreaService;
import com.vw.visitreporting.service.referencedata.OrganisationService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.form.OrganisationBrand;
import com.vw.visitreporting.web.form.OrganisationSearchForm;
import com.vw.visitreporting.web.validator.DoNothingValidator;


/**
 * Controller for all operations concerned with the Organisation entity, including:
 * - edit function permissions
 * - edit information sharing rules
 * - view levels
 */
@Controller
@RequestMapping("/organisation")
public class OrganisationController extends AbstractVRController<Organisation> {//NOPMD

	private static final Logger LOG = LoggerFactory.getLogger(OrganisationController.class); 
			
	@Autowired
	private OrganisationService organisationService;
	
	@Autowired
	private BusinessAreaService businessAreaService;
	
	@Autowired
	private DoNothingValidator validator;


	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String reportSearchLandingPage(Model model) {
		
		model.addAttribute("command", new OrganisationSearchForm());
		model.addAttribute("organisations", organisationService.list());
		model.addAttribute("businessArea", businessAreaService.list());
		model.addAttribute("levels", Level.values());
		
		LOG.debug("Level values === " + Level.values());
		
		return "organisation/search";
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String searchForOrganisationContacts(@ModelAttribute("command") @Valid OrganisationSearchForm formBean, BindingResult bindingResult, Model model) {
		
		LOG.debug("in searc method =========== ");
		
		System.out.println("org name === " + formBean.getOrganisationId());
		System.out.println("dealership id === " + formBean.getDealershipNumber());
		System.out.println("business area ===" + formBean.getBusinessArea());
		System.out.println("job role === " + formBean.getJobRole());
		System.out.println("brand geographical area === " + formBean.getGeographicalArea());
		System.out.println("level === " + formBean.getLevel());
		System.out.println("bindingResult.hasErrors() === " + bindingResult.hasErrors());
		
		//validate form bean (form bean has already been validated again JSR-303 annotations - this validates against business rules)
        getValidator().validate(formBean, bindingResult);

        //return to form if there were errors
        if(bindingResult.hasErrors()) {
            return "organisation/search";
        }
        
        List<User> results = organisationService.getOrganisationContacts(formBean.getOrganisationId(), formBean.getDealershipNumber(), 
        		formBean.getBusinessArea(), formBean.getJobRole(), formBean.getGeographicalArea(), formBean.getLevel());
        
        model.addAttribute("command", new OrganisationSearchForm());
		model.addAttribute("organisations", organisationService.list());
		model.addAttribute("businessArea", businessAreaService.list());
		model.addAttribute("levels", Level.values());
		model.addAttribute("searchResults", results);
		
		formBean.setSearchResults(results);
		
		return "organisation/search";
	}
	
	 /**
     * Main landing page for the system-functions permissions editing pages.
     * Provides a list of available organisations to edit permissions on.
     */
	@RequestMapping(value="/permissions/list", method=RequestMethod.GET)
	public String searchLandingPage(Model model) {
		return standardPermissionsResponse(model, null, null, null, null);
	}

	
    /**
     * Main landing page for the system-functions permissions editing pages.
     * Provides a list of available organisations to edit permissions on.
     */
	@RequestMapping(value="/permissions/list", method=RequestMethod.GET)
	public String accessToFunctionsPage(Model model) {
		return standardPermissionsResponse(model, null, null, null, null);
	}


    /**
     * Display the function permissions for a given organisation and brand.
	 * Access to Function.EDIT_ORGANISATION_FUNCTIONS required to perform this operation.
     */
    @PreAuthorize("hasPermission(#orgBrand.brand, 'EDIT_ORGANISATION_FUNCTIONS')")
	@RequestMapping(value="/{orgBrand}/permissions/update", method=RequestMethod.GET)
	public String organisationAndBrandAccessToFunctions(@PathVariable OrganisationBrand orgBrand, Model model) {

		Organisation org = organisationService.getOrganisation(orgBrand.getOrganisationId());
		orgBrand = new OrganisationBrand(org, orgBrand.getBrand());
		
		Set<Function> allowedFunctions = organisationService.getPermissions(orgBrand.getOrganisationId(), orgBrand.getBrand());

		return standardPermissionsResponse(model, orgBrand, allowedFunctions, org.getModifiedBy(), org.getModifiedDate());
	}


    /**
     * Update the set of allowed system functions in the database for a given organisation and brand.
	 * Access to Function.EDIT_ORGANISATION_FUNCTIONS required to perform this operation.
     */
	@PreAuthorize("hasPermission(#orgBrand.brand, 'EDIT_ORGANISATION_FUNCTIONS')")
	@RequestMapping(value="/{orgBrand}/permissions/save", method=RequestMethod.POST, params={"allowedFunctions"})
	public String updateAccess(@PathVariable OrganisationBrand orgBrand, @RequestParam("allowedFunctions") Function[] allowedFunctions, Model model) {

		Set<Function> permissions = EnumSet.noneOf(Function.class);
		permissions.addAll(Arrays.asList(allowedFunctions));

		Organisation organisation = organisationService.updateOrganisationPermissions(orgBrand.getOrganisationId(), orgBrand.getBrand(), permissions);
		orgBrand = new OrganisationBrand(organisation, orgBrand.getBrand());

		super.setSuccessMessage("Permissions successfully updated for organisation: "+orgBrand, model);

		return "redirect:/content/organisation/"+orgBrand.getId()+"/permissions/update";
	}

    /**
     * Special case where no functions are selected and the update button is clicked.
     */
	@RequestMapping(value="/{orgBrand}/permissions/save", method=RequestMethod.POST)
	public String updateAccess(@PathVariable OrganisationBrand orgBrand, Model model) {
		return this.updateAccess(orgBrand, new Function[0], model);
	}


	private String standardPermissionsResponse(Model model, OrganisationBrand organisationBrand, Set<Function> allowedFunctions, String modifiedBy, Date modifiedDate) {
		model.addAttribute("organisationBrands", OrganisationBrand.listFrom(organisationService.list()));
		model.addAttribute("organisationBrand", organisationBrand);
		model.addAttribute("allFunctions", Function.values());
		model.addAttribute("allowedFunctions", allowedFunctions);
    	model.addAttribute("modifiedBy", modifiedBy);
    	model.addAttribute("modifiedDate", modifiedDate);
    	
    	//LOG.debug("1 ---- " + model);
    	
    	
		return "organisation/permissions/list";
	}



    /**
     * Main landing page for the sharing rules editing pages.
     * Provides a list of available organisations to edit permissions on.
     */
	@RequestMapping(value="/sharingrules/list", method=RequestMethod.GET)
	public String sharingRulesPage(Model model) {
		return standardSharingRulesResponse(model, null, null, null, null);
	}
	
    /**
     * Display the sharing rules for a given organisation and brand.
	 * Access to Function.EDIT_ORGANISATION_SHARING_RULES required to perform this operation.
     */
	@PreAuthorize("hasPermission(#rootOrgBrand.brand, 'EDIT_ORGANISATION_SHARING_RULES')")
	@RequestMapping(value="/{rootOrgBrand}/sharingrules/update", method=RequestMethod.GET)
	public String organisationAndBrandSharingRules(@PathVariable OrganisationBrand rootOrgBrand, Model model) {

		Organisation org = organisationService.getOrganisation(rootOrgBrand.getOrganisationId());
		rootOrgBrand = new OrganisationBrand(org, rootOrgBrand.getBrand());

		List<SharingRule> rules = organisationService.organisationAndBrandSharingRules(rootOrgBrand.getOrganisationId(), rootOrgBrand.getBrand());
		Set<OrganisationBrand> targetOrgBrands = new HashSet<OrganisationBrand>();
		for(SharingRule rule : rules) {
			targetOrgBrands.add(new OrganisationBrand(rule.getTargetOrganisation(), rule.getTargetBrand()));
		}

		return standardSharingRulesResponse(model, rootOrgBrand, targetOrgBrands, org.getModifiedBy(), org.getModifiedDate());
	}


    /**
     * Update the set of sharing rules in the database for a given organisation and brand.
	 * Access to Function.EDIT_ORGANISATION_SHARING_RULES required to perform this operation.
     */
	@PreAuthorize("hasPermission(#rootOrgBrand.brand, 'EDIT_ORGANISATION_SHARING_RULES')")
	@RequestMapping(value="/{rootOrgBrand}/sharingrules/save", method=RequestMethod.POST, params={"targetOrgBrands"})
	public String updateSharingRules(@PathVariable OrganisationBrand rootOrgBrand, @RequestParam("targetOrgBrands") OrganisationBrand[] targetOrgBrands, Model model) {

		Integer[] targetOrgIds = new Integer[targetOrgBrands.length];
		for(int i=0; i<targetOrgBrands.length; i++) {
			targetOrgIds[i] = targetOrgBrands[i].getOrganisationId();
		}
		Brand[] targetBrands = new Brand[targetOrgBrands.length];
		for(int i=0; i<targetOrgBrands.length; i++) {
			targetBrands[i] = targetOrgBrands[i].getBrand();
		}

		Organisation org = organisationService.updateSharingRules(rootOrgBrand.getOrganisationId(), rootOrgBrand.getBrand(), targetOrgIds, targetBrands);
		rootOrgBrand = new OrganisationBrand(org, rootOrgBrand.getBrand());

		super.setSuccessMessage("Information sharing rules successfully updated for organisation: "+new OrganisationBrand(org, rootOrgBrand.getBrand()), model);

		return "redirect:/content/organisation/"+rootOrgBrand.getId()+"/sharingrules/update";
	}

    /**
     * Special case where no target organisation/brands are selected and the update button is clicked.
     */
	@RequestMapping(value="/{rootOrgBrand}/sharingrules/save", method=RequestMethod.POST)
	public String updateSharingRules(@PathVariable OrganisationBrand rootOrgBrand, Model model) {
		return this.updateSharingRules(rootOrgBrand, new OrganisationBrand[0], model);
	}

	private String standardSharingRulesResponse(Model model, OrganisationBrand rootOrgBrand, Set<OrganisationBrand> targetOrgBrands, String modifiedBy, Date modifiedDate) {
		model.addAttribute("organisationBrands", OrganisationBrand.listFrom(organisationService.list()));
		model.addAttribute("rootOrgBrand", rootOrgBrand);
		model.addAttribute("targetOrgBrands", targetOrgBrands);
		model.addAttribute("modifiedBy", modifiedBy);
		model.addAttribute("modifiedDate", modifiedDate);
		return "organisation/sharingrules/list";
	}

    /**
     * Main landing page for the organisational levels page.
     * Provides a map of organisations (mapped by organisation name).
     */
	@RequestMapping(value="/levels/list", method=RequestMethod.GET)
	public String organisationLevelPage(Model model) {
		model.addAttribute("organisations", organisationService.list());
		model.addAttribute("levels", Level.values());
		return "organisation/levels/list";
	}
	
	public VRService<Organisation> getTService() {
		return null;
	}

	public DoNothingValidator getValidator(){
		return validator;
	}
}