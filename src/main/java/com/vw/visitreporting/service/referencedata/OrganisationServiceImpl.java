package com.vw.visitreporting.service.referencedata;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vw.visitreporting.dao.referencedata.OrganisationDao;
import com.vw.visitreporting.dao.referencedata.UserDao;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.SharingRule;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Function;


@Service
public class OrganisationServiceImpl implements OrganisationService{

	@Autowired
	private OrganisationDao organisationDao;
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * Gets the collection of system functions that users belonging to this organisation and brand have access to.
	 */
	/* (non-Javadoc)
	 * @see com.vw.visitreporting.service.referencedata.OrganisationService#getPermissions
	 * (java.lang.Integer, com.vw.visitreporting.entity.referencedata.enums.Brand)
	 */
	@Override
	public Set<Function> getPermissions(Integer organisationId, Brand brand) {
		Organisation org = organisationDao.getOrganisationById(organisationId);
		return organisationDao.getFunctionAccess(org, brand);
	}

	
	/**
	 * Sets the collection of system functions that users belonging to this organisation and brand have access to.
	 * @return the update instance of Organisation that has been persisted to the database
	 */
	/* (non-Javadoc)
	 * @see com.vw.visitreporting.service.referencedata.OrganisationService#updateOrganisationPermissions
	 * (java.lang.Integer, com.vw.visitreporting.entity.referencedata.enums.Brand, java.util.Set)
	 */
	@Override
	public Organisation updateOrganisationPermissions(Integer organisationId, Brand brand, Set<Function> allowedFunctions) {
		Organisation org = organisationDao.getOrganisationById(organisationId);
		return organisationDao.updateFunctionAccess(org, brand, allowedFunctions);
	}

	
	/**
	 * Gets the collection of rules that dictate which target organisations and brands can view information
	 * retated to a given root organisation and brand.
	 */
	/* (non-Javadoc)
	 * @see com.vw.visitreporting.service.referencedata.OrganisationService#organisationAndBrandSharingRules
	 * (java.lang.Integer, com.vw.visitreporting.entity.referencedata.enums.Brand)
	 */
	@Override
	public List<SharingRule> organisationAndBrandSharingRules(Integer rootOrgId, Brand rootBrand) {
		Organisation org = organisationDao.getOrganisationById(rootOrgId);
		return organisationDao.getSharingRules(org, rootBrand);
	}
	
	
	/**
	 * Sets which target organisations and brands can view information
	 * retated to a given root organisation and brand.
	 * @return the updated instance of Organisation that has been persisted to the database
	 */
	/* (non-Javadoc)
	 * @see com.vw.visitreporting.service.referencedata.OrganisationService#updateSharingRules
	 * (java.lang.Integer, com.vw.visitreporting.entity.referencedata.enums.Brand, java.lang.Integer[], 
	 * com.vw.visitreporting.entity.referencedata.enums.Brand[])
	 */
	@Override
	public Organisation updateSharingRules(Integer rootOrgId, Brand rootBrand, Integer[] targetOrgIds, Brand[] targetBrands) {
		Organisation rootOrg = organisationDao.getOrganisationById(rootOrgId);
		return organisationDao.updateSharingRules(rootOrg, rootBrand, targetOrgIds, targetBrands);
	}
	
	
	/**
	 * Get an organisation from the database that matches the given id.
	 * @param id - the primary key property of the organisation
	 */
	/* (non-Javadoc)
	 * @see com.vw.visitreporting.service.referencedata.OrganisationService#getOrganisation(java.lang.Integer)
	 */
	@Override
	public Organisation getOrganisation(Integer id) {
		return this.organisationDao.getOrganisationById(id);
	}
	
	/**
	 * Get an organisation from the database that matches the given name.
	 * @param name - the name of the organisation
	 */
	/* (non-Javadoc)
	 * @see com.vw.visitreporting.service.referencedata.OrganisationService#findByName(java.lang.String)
	 */
	@Override
	public Organisation findByName(String name) {
		return this.organisationDao.getOrganisationByName(name);
	}
	
	/**
	 * Get all Organisation entities in the database.
	 */
	/* (non-Javadoc)
	 * @see com.vw.visitreporting.service.referencedata.OrganisationService#list()
	 */
	@Override
	public List<Organisation> list() {
		return this.organisationDao.list();
	}


	/* (non-Javadoc)
	 * @see com.vw.visitreporting.service.referencedata.OrganisationService#getOrganisationContacts(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<User> getOrganisationContacts(Organisation name) {
		System.out.println("In getOrganisationContacts method in OrganisationService");
		return userDao.getOrganisationContacts(name);
	}
	
	/* (non-Javadoc)
	 * @see com.vw.visitreporting.service.referencedata.OrganisationService#getOrganisationContacts(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<User> getOrganisationContacts(Integer orgId,
			Integer dealershipNumber, String businessArea, String jobRole,
			String geographicalArea, Integer level) {
		
		System.out.println("In getOrganisationContacts method in OrganisationService");
		
		// check if organisation is a Dealership.
        Organisation organisation = getOrganisation(orgId);
        
        System.out.println("organisation ========= " + organisation);
        
        System.out.println("orgId ==== " + orgId);
        System.out.println("organistion Name ==== " + organisation.getName());
        System.out.println("dealershipNumber ==== " + dealershipNumber);
        System.out.println("businessArea ==== " + businessArea);
        System.out.println("jobRole ==== " + jobRole);
        System.out.println("geographicalArea ==== " + geographicalArea);
        System.out.println("levels ==== " + level);
        
		return userDao.getOrganisationContacts(organisation, dealershipNumber,
    			businessArea, jobRole, geographicalArea, level);
	}

	
}
