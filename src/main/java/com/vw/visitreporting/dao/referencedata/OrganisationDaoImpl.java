package com.vw.visitreporting.dao.referencedata;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import com.google.common.base.Preconditions;
import com.vw.visitreporting.auditing.AuditManager;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.OrganisationFunction;
import com.vw.visitreporting.entity.referencedata.SharingRule;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Function;


/**
 * Data access object to access data about the Organisation entity in the database.
 * This DAO does not extend the AbstractVRDao class because it does not follow enough
 * of the CRUD pattern.
 */
@Repository
public class OrganisationDaoImpl implements OrganisationDao {

	@Autowired
	private AuditManager auditManager;
	
	@Autowired
	private SessionFactory sessionFactory;

	
	/**
	 * Get an organisation from the database that matches the given id.
	 * @param id - the primary key property of the organisation
	 */
	public Organisation getOrganisationById(Integer organisationId) {
		return (Organisation)getSession().get(Organisation.class, organisationId);
	}

	/**
	 * Get an organisation from the database that matches the given name.
	 * @param name - the name of the organisation
	 */
	public Organisation getOrganisationByName(String name) {
		Criteria crit = createBaseCriteria(Organisation.class);
		crit.add(Restrictions.eq("name", name));
		return (Organisation)crit.uniqueResult();
	}
	
	/**
	 * Gets the collection of system functions that users belonging to the given organisation and brand have access to.
	 */
	public Set<Function> getFunctionAccess(Organisation organisation, Brand brand) {
		List<OrganisationFunction> orgFuncs = getOrganisationFunctionsFor(organisation, brand);
		
		EnumSet<Function> allowedFunctions = EnumSet.noneOf(Function.class);
		for(OrganisationFunction orgFunc : orgFuncs) {
			allowedFunctions.add(orgFunc.getFunction());
		}
		return allowedFunctions;
	}
	
	/**
	 * Sets the collection of system functions that users belonging to this organisation and brand have access to.
	 * Access to Function.EDIT_ORGANISATION_FUNCTIONS required to perform this operation.
	 * @return the updated instance of Organisation that has been persisted to the database
	 */
	@PreAuthorize("hasPermission(#brand, 'EDIT_ORGANISATION_FUNCTIONS')")
	public Organisation updateFunctionAccess(Organisation organisation, Brand brand, Set<Function> allowedFunctions) {
		
		List<OrganisationFunction> prevOrgFuncs = this.getOrganisationFunctionsFor(organisation, brand);
		for(Function allowedFunc : allowedFunctions) {

			OrganisationFunction orgFunc = null;
			for(OrganisationFunction prevOrgFunc : prevOrgFuncs) {
				if(prevOrgFunc.getFunction() == allowedFunc) {
					orgFunc = prevOrgFunc;
					break;
				}
			}
			//if grant was not found in previous list then create new entity
			if(orgFunc == null) {
				orgFunc = new OrganisationFunction();
				orgFunc.setFunction(allowedFunc);
				orgFunc.setOrganisation(organisation);
				orgFunc.setBrand(brand);
				organisation.getAllowedFunctions().add(orgFunc);

			//if grant was found then remove this from prevOrgFuncs list - the remaining elements in this list will be deleted
			} else {
				prevOrgFuncs.remove(orgFunc);
			}
		}
		//delete remaining grants in previous list that have not been found in new list
		for(OrganisationFunction remainingGrant : prevOrgFuncs) {
			organisation.getAllowedFunctions().remove(remainingGrant);
		}

		//update the modifiedBy/modifiedDate properties of the organisation entity
		return this.update(organisation);
	}
	
	@SuppressWarnings("unchecked")
	private List<OrganisationFunction> getOrganisationFunctionsFor(Organisation organisation, Brand brand) {
		Criteria crit = createBaseCriteria(OrganisationFunction.class);
		crit.add(Restrictions.eq("organisation", organisation));
		if(brand != null) {
			crit.add(Restrictions.eq("brand", brand.getId()));
		}
		return crit.list();		
	}
	
	/**
	 * Gets the collection of rules that dictate which target organisations and brands can view information
	 * retated to a given root organisation and brand.
	 */
	@SuppressWarnings("unchecked")
	public List<SharingRule> getSharingRules(Organisation organisation, Brand brand) {
		Criteria crit = createBaseCriteria(SharingRule.class);
		crit.add(Restrictions.eq("rootOrganisation", organisation));
		if(brand != null) {
			crit.add(Restrictions.eq("rootBrand", brand.getId()));
		}
		return crit.list();
	}

	/**
	 * Sets which target organisations and brands can view information
	 * retated to a given root organisation and brand.
	 * Access to Function.EDIT_ORGANISATION_SHARING_RULES required to perform this operation.
	 * @return the updated instance of Organisation that has been persisted to the database
	 */
	@PreAuthorize("hasPermission(#rootBrand, 'EDIT_ORGANISATION_SHARING_RULES')")
	public Organisation updateSharingRules(Organisation rootOrg, Brand rootBrand, Integer[] targetOrgIds, Brand[] targetBrands) {
		Preconditions.checkArgument(targetOrgIds.length == targetBrands.length, "targetOrgIds and targetBrands arrays must be parallel");
		
		List<SharingRule> prevRules = this.getSharingRules(rootOrg, rootBrand);
		for(int i=0; i<targetOrgIds.length; i++) {
		
			SharingRule rule = null;
			for(SharingRule prevRule : prevRules) {
				if(targetOrgIds[i] == prevRule.getTargetOrganisation().getId() && targetBrands[i] == prevRule.getTargetBrand()) {
					rule = prevRule;
					break;
				}
			}
			//if rule was not found in previous list then create new entity
			if(rule == null) {
				rule = new SharingRule();
				rule.setRootOrganisation(rootOrg);
				rule.setRootBrand(rootBrand);
				rule.setTargetOrganisation(this.getOrganisationById(targetOrgIds[i]));
				rule.setTargetBrand(targetBrands[i]);
				rootOrg.getSharingRules().add(rule);
			
			//if rule was found then remove this from prevRules list - the remaining elements in this list will be deleted
			} else {
				prevRules.remove(rule);
			}
		}
		//delete remaining rules in previous list that have not been found in new list
		for(SharingRule remainingRule : prevRules) {
			rootOrg.getSharingRules().remove(remainingRule);
		}

		//update the modifiedBy/modifiedDate properties of the organisation entity
		return this.update(rootOrg);
	}
	
	/**
	 * Get all Organisation entities in the database.
	 */
	@SuppressWarnings("unchecked")
	public List<Organisation> list() {
		return createBaseCriteria(Organisation.class).list();
	}	


	/**
	 * Updates an Organisation entity in the database.
	 * This method must be PRIVATE as it is not access controlled.
	 */
	private Organisation update(Organisation organisation) {
		auditManager.onEntityModification(organisation);
		organisation.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
		organisation.setModifiedDate(new Date());
		return (Organisation)getSession().merge(organisation);
	}



	/**
	 * Gets the currently open Hibernate session in the current transaction.
	 * This this method should be used instead of createSession() or getCurrentSession()
	 * as it will ensure that the current transactional context is used.
	 */
	public Session getSession() {
		return SessionFactoryUtils.getSession(this.sessionFactory, true);
	}
	
	
	/**
	 * Create a new Criteria instance attached to the current session and
	 * correctly configured for caching.
	 * @param entityClass - the type of entity that is being queries for.
	 */
	public Criteria createBaseCriteria(Class<? extends BaseVREntity> entityClass) {
		
		//determine when to cache queries, based on Cache attribute of entity
		boolean cacheQueries = false;
		Cache cacheAnnotation = entityClass.getAnnotation(Cache.class);
		if(cacheAnnotation != null) {
			switch(cacheAnnotation.usage()) {
				case READ_ONLY:  cacheQueries = true;  break;
				case READ_WRITE: cacheQueries = true;  break;
				default:         cacheQueries = false; break;
			}
		}

		Criteria crit = getSession().createCriteria(entityClass);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		crit.setCacheable(cacheQueries);
		//searchCriteria.setCacheRegion("query.myQueryRegion");
		//crit.setMaxResults(DEFAULT_MAX_RESULTS);
		
		return crit;
	}
}
