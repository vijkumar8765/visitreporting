package com.vw.visitreporting.web.controller.referencedata;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.vw.visitreporting.entity.referencedata.UserProfile;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.UserProfileService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.validator.DoNothingValidator;


/**
 * Controller bean for editing permissions to system functions by user profiles.
 */
@Controller
@RequestMapping("/userprofile")
public class UserProfileController extends AbstractVRController<UserProfile> {

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private DoNothingValidator validator;


    /**
     * Main landing page for the system-functions permissions editing pages.
     * Provides a list of available user profiles to edit permissions on.
     */
	@RequestMapping(value="/permissions/list", method=RequestMethod.GET)
	public String showPermissions(Model model) {
		return standardPermissionsResponse(model, null, null, null, null);
	}


    /**
     * Display the function permissions for a given user profile.
     * @param entityId - the id of the element to show the permissions for
     */
	@RequestMapping(value="/{profileId}/permissions/update", method=RequestMethod.GET)
	public String showPermissionsFor(@PathVariable Integer profileId, Model model) {
		UserProfile userProfile = userProfileService.getById(profileId);
		return standardPermissionsResponse(model, profileId, userProfile.getAllowedFunctions(), userProfile.getModifiedBy(), userProfile.getModifiedDate());
	}


    /**
     * Update the set of allowed system functions in the database for a given user profile.
     */
	@RequestMapping(value="/{profileId}/permissions/save", method=RequestMethod.POST, params={"allowedFunctions"})
	public String updatePermissions(@PathVariable Integer profileId, @RequestParam("allowedFunctions") Function[] allowedFunctions, Model model) {

		HashSet<Function> permissions = new HashSet<Function>();
		permissions.addAll(Arrays.asList(allowedFunctions));

		UserProfile userProfile = userProfileService.updatePermissions(profileId, permissions);
		
		super.setSuccessMessage("Permissions successfully updated for profile: "+userProfile, model);

		return "redirect:/content/userprofile/"+profileId+"/permissions/update";
	}

    /**
     * Special case where no functions are selected and the update button is clicked.
     */
	@RequestMapping(value="/{profileId}/permissions/save", method=RequestMethod.POST)
	public String updatePermissions(@PathVariable Integer profileId, Model model) {
		return this.updatePermissions(profileId, new Function[0], model);
	}

	
	private String standardPermissionsResponse(Model model, Integer profileId, Set<Function> allowedFunctions, String modifiedBy, Date modifiedDate) {
		model.addAttribute("userProfiles", userProfileService.list());
		model.addAttribute("profileId", profileId);
		model.addAttribute("allFunctions", Function.values());
		model.addAttribute("allowedFunctions", allowedFunctions);
    	model.addAttribute("modifiedBy", modifiedBy);
    	model.addAttribute("modifiedDate", modifiedDate);
		return "userprofile/permissions/list";
	}
	
	
	public VRService<UserProfile> getTService() {
		return userProfileService;
	}

	protected Validator getValidator() {
		return validator;
	}
}
