package com.vw.visitreporting.web.controller.referencedata;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.vw.visitreporting.entity.referencedata.BusinessArea;
import com.vw.visitreporting.entity.referencedata.Kpi;
import com.vw.visitreporting.entity.referencedata.KpiCategory;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.KpiStatus;
import com.vw.visitreporting.entity.referencedata.enums.VarianceColor;
import com.vw.visitreporting.service.VRService;
import com.vw.visitreporting.service.referencedata.KpiCategoryService;
import com.vw.visitreporting.service.referencedata.KpiService;
import com.vw.visitreporting.web.controller.AbstractVRController;
import com.vw.visitreporting.web.validator.DoNothingValidator;

@Controller
@RequestMapping("/kpi")
public class KpiController extends AbstractVRController<Kpi> {

	@Autowired
	private KpiService kpiService;		
	
	@Autowired
	private DoNothingValidator validator;	
	
	@Autowired
	private KpiCategoryService kpiCategoryService;
	
    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("crudObj") @Valid Kpi entity, BindingResult bindingResult, Model model) {  //NOPMD    	
    	
    	//Associate Kpibrands
    	kpiService.associateKpiBrands(entity);
    	
    	//Set Status at create time
    	if(entity != null && entity.getId() == null && entity.getStatus() == null){
    		entity.setStatus(KpiStatus.ACTIVE);
    	}
    	
    	if(entity != null && entity.getId() != null && entity.getId().longValue() > 0 && entity.getNewStatus() == KpiStatus.ACTIVE){
    		entity.setNewStatus(null);
    		//entity.setNewStatusEffectiveDate(null);
    	}
    	
    	
    	return super.save(entity, bindingResult, model);
    }
    
    @Override
    @RequestMapping(value="/{id}/update", method=RequestMethod.GET)
    public String update(@PathVariable String id, Model model) {
    	
    	Kpi entity = getCrudEntity(id);    	
    		
    	
    	//Set all Transient DynamicKpiBrands and DynamicBusinessRules
    	kpiService.loadDynamicEntities(entity);
    	
    	model.addAttribute("crudObj", entity);

		return ClassUtils.getShortName(getEntityClass()).toLowerCase() + "/createEdit";
    }
    
    @RequestMapping(value="/create", method=RequestMethod.GET)
	public String create(Model model) {

    	Kpi kpi = getEntityInstance();    
    	
		model.addAttribute("crudObj", kpi);
		
		return ClassUtils.getShortName(getEntityClass()).toLowerCase() + "/createEdit";
	}
	
    @RequestMapping(value="/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable String id, Model model) {
    	throw new AccessDeniedException("This operation is not available");
    }
    
	@ModelAttribute("org_list")
    public Set<Organisation> organisations(WebRequest request) {
       	return User.getLoginUser().getAllowedOrganisations();
    }
	
	@ModelAttribute("kpiCategory_list")
    public List<KpiCategory> kpiCategories(WebRequest request) {
       	return kpiCategoryService.list();
    }
	
	@ModelAttribute("businessArea_list")
    public Set<BusinessArea> businessAreas(WebRequest request) {
       	return User.getLoginUser().getBusinessAreas();
    }
	
	@ModelAttribute("brand_list")
    public Set<Brand> brands(WebRequest request) {
    	return User.getLoginUser().getBrands();
    }
	
	@ModelAttribute("varianceColor_list")
    public VarianceColor[] varianceColors(WebRequest request) {
    	return VarianceColor.values();
    }
	
	@ModelAttribute("kpiStatus_list")
    public KpiStatus[] kpiStatus(WebRequest request) {
    	return KpiStatus.values();
    }
	
	@Override
	public VRService<Kpi> getTService() {
		return kpiService;
	}
	
	@Override
	public DoNothingValidator getValidator(){
		return validator;
	}
	
}
