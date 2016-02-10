package com.vw.visitreporting.web.controller;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.google.common.base.CaseFormat;
import com.vw.visitreporting.common.constants.VRCommonConstants;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.service.VRService;

//Note to users of this class
//Only the classes which follow a pattern of jsps classes should use this class
//Other are free to code all the methods without using this abstract class

public abstract class AbstractVRController<T extends BaseVREntity> {

	protected AbstractVRController() {
	}

	public abstract VRService<T> getTService();

    abstract protected Validator getValidator();

    
    @SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
        return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected T getEntityInstance() {
		try {
			return (T)getEntityClass().newInstance();
		} catch (Exception err) {
			throw new IllegalArgumentException("Entity class "+getEntityClass().getSimpleName()+" does not parameterless constructor", err);
		}
    }

	/**
	 * Gets the name of the primary key property of this entity.
	 * Assumes the primary key property is going to be the id, so this method must be overridden if the primary key of entity is not id.
	 */          
    protected String getPkParam() {
		return "id";           
	}

    /**
	 * Gets the type of the primary key property of this entity.
	 * Assumes the primary key property is going to be the id, so this method must be overridden if the primary key of entity is not id.
	 */
	@SuppressWarnings("unchecked")
	protected Class<? extends Serializable> getPkType() {
		try {
			String pk = getPkParam();
			return (Class<? extends Serializable>)this.getEntityClass().getMethod("get"+pk.substring(0,1).toUpperCase()+pk.substring(1)).getReturnType();
		} catch(NoSuchMethodException err) {
			throw new RuntimeException("unable to determine type of primary key property", err);
		}
	}


	/**
	 * Landing page for a page that lists all current entities of this type in the database.
	 * @param model - adds a parameter "list" to the model containing all entities in the database
	 * @return list view name
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("list", getTService().list());
		return ClassUtils.getShortName(getEntityClass()).toLowerCase() + "/list";
	}

	/**
	 * Landing page for a page that creates a new entity of this type.
	 * @return createEdit view name
	 */
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String create(Model model) {

		model.addAttribute("crudObj", getEntityInstance());
		
		return ClassUtils.getShortName(getEntityClass()).toLowerCase() + "/createEdit";
	}

	/**
	 * Landing page for a page that updates an existing entity of this type.
	 * @param id - the primary key of the entity to start editing
	 * @return createEdit view name
	 */
	@RequestMapping(value="/{id}/update", method=RequestMethod.GET)
	public String update(@PathVariable String id, Model model) {

    	T entity = getCrudEntity(id);
    	model.addAttribute("crudObj", entity);

		return ClassUtils.getShortName(getEntityClass()).toLowerCase() + "/createEdit";
	}


    /**
     * Creates or updates an entity in the database and redirects the user back to the list page.
     * The @Valid annotation validates the entity against JSR-303 validation and the getValidator validates against business rules.
     * @param entity - a new or exiting entity instance to persist in the database
     * @param bindingResult - any binding errors arising from JSR-303 validation. Further validations errors may get added to this object during business rule validation.
     */
    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("crudObj") @Valid T entity, BindingResult bindingResult, Model model) {

    	//validate entity (entity has already been validated again JSR-303 annotations - this validate against business rules)
        getValidator().validate(entity, bindingResult);

        //return to form if there were errors
        if(bindingResult.hasErrors()) {
        	this.setWarningMessage("Please correct the errors and submit the page again.", model);
            return ClassUtils.getShortName(getEntityClass()).toLowerCase() + "/createEdit";
        }
        
        //persist the entity in the database
        getTService().save(entity);

        String entityName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, ClassUtils.getShortName(getEntityClass()));
		this.setSuccessMessage("Saved "+entityName+": "+entity, model);

        //return to list
        return "redirect:/content/" + ClassUtils.getShortName(getEntityClass()).toLowerCase() + "/list";
    }

    /**
     * Ensure that a new or existing instance of the entity type for this controller is present in the model.
     * This will get called before any request-mapping methods to ensure that the crudObj attribute is present.
     * @return if a parameter is present in the request with name the same as the primary key property, the
     *         entity matching this key is retrieved from the database and returned, otherwise a new instance is created.
     */
    @ModelAttribute("crudObj")
    public T setupModel(WebRequest request) {
    	
		//find primary key parameter value from the request
		String pkStr = request.getParameter(getPkParam());
		
		//if primary key parameter is not present, create new instance
		if(pkStr == null || pkStr.trim().equals("")){
			return getEntityInstance();

		//if primary key is found, retrieve entity from database
		} else {
			return this.getCrudEntity(pkStr);
		}
    }
    
    /**
     * Deletes an entity from the database and redirects to the list page, in case of any business validation delete failure, show the error message
     * and redirects to the create edit page. 
	 * @param id - the primary key of the entity to delete
     * @return redirect to list or create edit page
     */
    @RequestMapping(value="/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable String id, Model model) {
    	
    	T entity = getCrudEntity(id);
    	
    	String result = getTService().delete(entity);
    	if(!result.equalsIgnoreCase(VRCommonConstants.SUCCESS)){
        	this.setErrorMessage(result, model);
        	return "redirect:/content/" + ClassUtils.getShortName(getEntityClass()).toLowerCase() + "/" + id + "/update";
    	}
    	String entityName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, ClassUtils.getShortName(getEntityClass()));
		this.setSuccessMessage("Deleted " + entityName + ": "+entity, model);
		
		// Mark the current handler's session processing as complete, allowing for cleanup of session attributes.
		return "redirect:/content/" + ClassUtils.getShortName(getEntityClass()).toLowerCase() + "/list";
    }

    /**
     * Ensure that a new or existing instance of the entity type for this controller is present in the model.
     * This will get called before any request-mapping methods to ensure that the crudObj attribute is present.
     * @return if a parameter is present in the request with name the same as the primary key property, the
     *         entity matching this key is retrieved from the database and returned, otherwise a new instance is created.
     */
    protected T getCrudEntity(String id) {
		Serializable pk = null;
		try {
			pk = getPkType().getConstructor(String.class).newInstance(id);
		} catch(Exception err) {
			throw new IllegalArgumentException("unable to convert "+id+" into the correct primary key type", err);
		}
		return (T)getTService().getById(pk);
    }


    protected void setWarningMessage(String msg, Model model) {
    	this.setMessage("warning", msg, model);
    }
    protected void setErrorMessage(String msg, Model model) {
    	this.setMessage("error", msg, model);
    }
    protected void setSuccessMessage(String msg, Model model) {
    	this.setMessage("success", msg, model);
    }
    protected void setInfoMessage(String msg, Model model) {
    	this.setMessage("info", msg, model);
    }
    //@SuppressWarnings("unchecked")
	private void setMessage(String type, String msg, Model model) {
    	String attrName = type + "Messages";
    	//if( ! model.asMap().containsKey(attrName)) {
    	//	model.addAttribute(attrName, new ArrayList<String>());
    	//}
    	//((ArrayList<String>)model.asMap().get(attrName)).add(msg);
    	model.addAttribute(attrName, msg);
    }
}

