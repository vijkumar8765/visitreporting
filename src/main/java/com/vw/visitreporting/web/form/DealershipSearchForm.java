package com.vw.visitreporting.web.form;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

import com.vw.visitreporting.dto.PagingDTO;
import com.vw.visitreporting.entity.referencedata.Dealership;


/**
 * Form bean to encapsulate data for the dealership search page.
 * Stores query criteria and search results.
 */
public class DealershipSearchForm {

	@Pattern(regexp="\\d{0,5}", message="must be a number between 0 - 99999")
	private String dealershipNumber;
	private String dealershipName;
	private String franchiseGroupName;
	private List<Dealership> searchResults;
	private PagingDTO paging;
	
	/**
	 * Constructor. Creates an instance with empty search parameters and reaults.
	 */
	public DealershipSearchForm() {
		paging = new PagingDTO();
	}

	/**
	 * Gets the filter to apply to the dealer number column.
	 * Cannot include wildcards.
	 */
	public String getDealershipNumber() {
		return dealershipNumber;
	}

	/**
	 * Sets the filter to apply to the dealer number column.
	 * Cannot include wildcards.
	 */
	public void setDealershipNumber(String dealershipNumber) {
		this.dealershipNumber = dealershipNumber;
	}

	/**
	 * Gets the filter to apply to the dealer name column.
	 * Can include wildcard * characters.
	 */
	public String getDealershipName() {
		return dealershipName;
	}

	/**
	 * Sets the filter to apply to the dealer name column.
	 * Can include wildcard * characters.
	 */
	public void setDealershipName(String dealershipName) {
		this.dealershipName = dealershipName;
	}

	/**
	 * Gets the filter to apply to the dealer franchise group name column.
	 * Can include wildcard * characters.
	 */
	public String getFranchiseGroupName() {
		return franchiseGroupName;
	}

	/**
	 * Sets the filter to apply to the dealer franchise group name column.
	 * Can include wildcard * characters.
	 */
	public void setFranchiseGroupName(String franchiseGroupName) {
		this.franchiseGroupName = franchiseGroupName;
	}

	/**
	 * Gets the results of the query for the criteria specified in this bean.
	 */
	public List<Dealership> getSearchResults() {
		return searchResults;
	}

	/**
	 * Sets the results of the query for the criteria specified in this bean.
	 */
	public void setSearchResults(List<Dealership> searchResults) {
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
