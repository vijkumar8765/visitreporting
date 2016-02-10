package com.vw.visitreporting.web.controller.referencedata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.Positions;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Level;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.PositionsService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.validator.PositionsValidator;

@Controller
@RequestMapping("/positions")
public class PositionsController extends AbstractVRController<Positions> {
	
	@Autowired
	private PositionsService positionsService;
	
	@Autowired
	private PositionsValidator validator;
	
	public PositionsController(){
	}

    @ModelAttribute("level_list")
    public List<Level> levels(WebRequest request) {
    	List<Level> level_list;
    	Collections.sort(level_list = new ArrayList<Level>(User.getLoginUser().getPossibleLevels()));
    	return level_list;
    }
 
    @ModelAttribute("brand_list")
    public Set<Brand> brands(WebRequest request) {
    	return User.getLoginUser().getBrands();
    }
 
    @ModelAttribute("org_list")
    public Set<Organisation> organisations(WebRequest request) {
    	return User.getLoginUser().getAllowedOrganisations();
    }

    public String create(ModelMap model, WebRequest request) {
    	throw new AccessDeniedException("This operation is not available");
    }
    public String update(ModelMap model) {
    	throw new AccessDeniedException("This operation is not available");
    }

	@Override
	public VRService<Positions> getTService() {
		return positionsService;
	}

	@Override
	public PositionsValidator getValidator(){
		return validator;
	}
}
