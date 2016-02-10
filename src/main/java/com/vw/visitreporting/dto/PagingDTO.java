package com.vw.visitreporting.dto;

/**
 * Bean to be used by data tables on web-pages to store the current
 * paging/sorting state of the table. This bean can be passed down to
 * the DAO layer to supply query arguments. 
 */
public class PagingDTO {

	private int start;
	private int count;
	private String sortProperty;
	private boolean sortAscending;
	
	/**
	 * Constructor. Creates an instance with default paging parameters:
	 * start=0, count=100, sortProperty=null, sortAscending=true
	 */
	public PagingDTO() {
		start = 0;
		count = 100;
		sortProperty = null;
		sortAscending = true;
	}

	/**
	 * Gets the row number of the query result to display first in the table.
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Sets the row number of the query result to display first in the table.
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * Gets the number of results to display in the table.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Sets the number of results to display in the table.
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Gets the name of the property to be sorted. Only one property can be sorted in this query.
	 * If this property is null, then no sorting will be applied to the query.
	 */
	public String getSortProperty() {
		return sortProperty;
	}

	/**
	 * Sets the name of the property to be sorted. Only one property can be sorted in this query.
	 * If this property is null, then no sorting will be applied to the query.
	 */
	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}

	/**
	 * Gets whether to sort the sort-property of the query resutl in ascending or descending order.
	 * If the sortProperty property of this object is null then this property will not be used.
	 */
	public boolean isSortAscending() {
		return sortAscending;
	}

	/**
	 * Sets whether to sort the sort-property of the query resutl in ascending or descending order.
	 * If the sortProperty property of this object is null then this property will not be used.
	 */
	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}
}
