package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import java.util.Set;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.SharingRule;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Function;


/**
 * Data access object to access data about the Organisation entity in the database.
 * This DAO does not extend the AbstractVRDao class because it does not follow enough
 * of the CRUD pattern.
 */
public interface OrganisationDao {

	/**
	 * Get an organisation from the database that matches the given id.
	 * @param id - the primary key property of the organisation
	 */
	Organisation getOrganisationById(Integer organisationId);

	/**
	 * Get an organisation from the database that matches the given name.
	 * @param name - the name of the organisation
	 */
	Organisation getOrganisationByName(String name);

	/**
	 * Gets the collection of system functions that users belonging to the given organisation and brand have access to.
	 */
	Set<Function> getFunctionAccess(Organisation organisation, Brand brand);
	
	/**
	 * Sets the collection of system functions that users belonging to this organisation and brand have access to.
	 * Access to Function.EDIT_ORGANISATION_FUNCTIONS required to perform this operation.
	 * @return the updated instance of Organisation that has been persisted to the database
	 */
	Organisation updateFunctionAccess(Organisation organisation, Brand brand, Set<Function> allowedFunctions);
	
	/**
	 * Gets the collection of rules that dictate which target organisations and brands can view information
	 * retated to a given root organisation and brand.
	 */
	List<SharingRule> getSharingRules(Organisation organisation, Brand brand);

	/**
	 * Sets which target organisations and brands can view information
	 * retated to a given root organisation and brand.
	 * Access to Function.EDIT_ORGANISATION_SHARING_RULES required to perform this operation.
	 * @return the updated instance of Organisation that has been persisted to the database
	 */
	Organisation updateSharingRules(Organisation organisation, Brand brand, Integer[] targetOrgIds, Brand[] targetBrands);
	
	/**
	 * Get all Organisation entities in the database.
	 */
	List<Organisation> list();
}
