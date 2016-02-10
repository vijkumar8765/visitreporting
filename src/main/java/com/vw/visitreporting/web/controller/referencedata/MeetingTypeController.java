package com.vw.visitreporting.web.controller.referencedata;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vw.visitreporting.entity.referencedata.MeetingType;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.MeetingTypeService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.validator.DoNothingValidator;


@Controller
@RequestMapping("/meetingtype")
public class MeetingTypeController extends AbstractVRController<MeetingType> {

	private MeetingTypeService meetingTypeService;

	private DoNothingValidator validator;

	
	/**
	 * The create page and the list page have been merged together for this simple entity.
	 * Therefore we extend the save method to ensure that it always uses the list view (never the createEdit view).
     * Restrict access to the list URL to users who have access to Function.EDIT_MEETING_TYPE
	 */
    @PreAuthorize("hasPermission(null, 'EDIT_MEETING_TYPE')")
    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("crudObj") @Valid MeetingType entity, BindingResult bindingResult, Model model) {
    	super.list(model);
    	return super.save(entity, bindingResult, model).replace("/createEdit", "/list");
    }
	
    
    /**
     * Deletes an entity from the database and redirects to the list page
     * Restrict access to the list URL to users who have access to Function.EDIT_MEETING_TYPE
	 * @param id - the primary key of the entity to delete
     * @return redirect to list page
     */
    @PreAuthorize("hasPermission(null, 'EDIT_MEETING_TYPE')")
    @RequestMapping(value="/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable String id, Model model) {

    	MeetingType entity = getCrudEntity(id);

        //disable the entity
    	entity.setDisabled(true);
    	getTService().save(entity);
        
        //return to list
        return "redirect:/content/" + ClassUtils.getShortName(getEntityClass()).toLowerCase() + "/list";
    }
    

    /**
     * Restrict access to the list URL to users who have access to Function.EDIT_MEETING_TYPE
     */
    @PreAuthorize("hasPermission(null, 'EDIT_MEETING_TYPE')")
	@RequestMapping(value="/list", method=RequestMethod.GET)
    public String list(Model model) {

    	//add new instance of MeetingType to the model in case someone wants to create a new item
		model.addAttribute("crudObj", getEntityInstance());

    	return super.list(model);
    }

    
    public String create(Model model) {
    	throw new UnsupportedOperationException("this operation is not available");
    }
    public String update(String id, Model model) {
    	throw new UnsupportedOperationException("this operation is not available");
    }

	
    
	public VRService<MeetingType> getTService() {
		return meetingTypeService;
	}
	
	@Autowired
	public void setMeetingTypeService(MeetingTypeService meetingTypeService) {
		this.meetingTypeService = meetingTypeService;
	}

	protected Validator getValidator() {
		return validator;
	}

	@Autowired
	public void setValidator(DoNothingValidator validator) {
		this.validator = validator;
	}
}
