package com.vw.visitreporting.web.form;

import java.util.List;

import com.vw.visitreporting.dto.PagingDTO;
import com.vw.visitreporting.entity.referencedata.User;


/**
 * Form bean to encapsulate data for the organisation search page.
 * Stores query criteria and search results.
 */
/**
 * @author FOX0BVI
 *
 */
public class OrganisationSearchForm {

	private Integer organisationId;
	private Integer dealershipNumber;
	private String businessArea;
	private String jobRole;
	private String geographicalArea;
	private Integer level;
	
	private List<User> searchResults;
	private PagingDTO paging;
	

	/**
	 * @return the organisationId
	 */
	public Integer getOrganisationId() {
		return organisationId;
	}

	/**
	 * @param organisationId the organisation to set
	 */
	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	/**
	 * @return the businessArea
	 */
	public String getBusinessArea() {
		return businessArea;
	}

	/**
	 * @param businessArea the businessArea to set
	 */
	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}

	/**
	 * @return the jobRole
	 */
	public String getJobRole() {
		return jobRole;
	}

	/**
	 * @param jobRole the jobRole to set
	 */
	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}

	/**
	 * @return the brandGeographicalArea
	 */
	public String getGeographicalArea() {
		return geographicalArea;
	}

	/**
	 * @param brandGeographicalArea the brandGeographicalArea to set
	 */
	public void setGeographicalArea(String geographicalArea) {
		this.geographicalArea = geographicalArea;
	}

	/**
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * Constructor. Creates an instance with empty search parameters and reaults.
	 */
	public OrganisationSearchForm() {
		paging = new PagingDTO();
	}

	/**
	 * Gets the filter to apply to the dealer number column.
	 * Cannot include wildcards.
	 */
	public Integer getDealershipNumber() {
		return dealershipNumber;
	}

	/**
	 * Sets the filter to apply to the dealer number column.
	 * Cannot include wildcards.
	 */
	public void setDealershipNumber(Integer dealershipNumber) {
		this.dealershipNumber = dealershipNumber;
	}

	/**
	 * Gets the results of the query for the criteria specified in this bean.
	 */
	public List<User> getSearchResults() {
		return searchResults;
	}

	/**
	 * Sets the results of the query for the criteria specified in this bean.
	 */
	public void setSearchResults(List<User> searchResults) {
		this.searchResults = searchResults;
	}

	/**
	 * Gets the paging/sorting criteria to apply to the query.
	 */
	public PagingDTO getPaging() {
		return paging;
	}

	/**
	 * Sets the paging/sorting criteria to apply to the query.
	 */
	public void setPaging(PagingDTO paging) {
		this.paging = paging;
	}
}
