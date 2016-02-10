package com.vw.visitreporting.service.referencedata;

import java.util.List;
import java.util.Set;

import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.SharingRule;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Function;


public interface OrganisationService {

	/**
	 * Gets the collection of system functions that users belonging to this organisation and brand have access to.
	 */
	Set<Function> getPermissions(Integer organisationId, Brand brand);
	
	/**
	 * Sets the collection of system functions that users belonging to this organisation and brand have access to.
	 * @return the updated instance of Organisation that has been persisted to the database
	 */
	Organisation updateOrganisationPermissions(Integer organisationId, Brand brand, Set<Function> allowedFunctions);

	/**
	 * Gets the collection of rules that dictate which target organisations and brands can view information
	 * retated to a given root organisation and brand.
	 */
	List<SharingRule> organisationAndBrandSharingRules(Integer rootOrgId, Brand rootBrand);
	
	/**
	 * Sets which target organisations and brands can view information
	 * retated to a given root organisation and brand.
	 * @return the updated instance of Organisation that has been persisted to the database
	 */
	Organisation updateSharingRules(Integer rootOrgId, Brand rootBrand, Integer[] targetOrgIds, Brand[] targetBrands);
	
	/**
	 * Get an organisation from the database that matches the given id.
	 * @param id - the primary key property of the organisation
	 */
	Organisation getOrganisation(Integer id);
	
	/**
	 * Get an organisation from the database that matches the given name.
	 * @param name - the name of the organisation
	 */
	Organisation findByName(String name);
	
	/**
	 * Get all Organisation entities in the database.
	 */
	List<Organisation> list();
	
	/**
	 * @param name - the name of the organisation
	 * @return
	 */
	List<User> getOrganisationContacts(Organisation name);
	
	/**
	 * @param orgId
	 * @param dealershipNumber
	 * @param businessArea
	 * @param jobRole
	 * @param geographicalArea
	 * @param level
	 * @return
	 */
	List<User> getOrganisationContacts(Integer orgId, Integer dealershipNumber, String businessArea, String jobRole, String geographicalArea, Integer level);
	
}
