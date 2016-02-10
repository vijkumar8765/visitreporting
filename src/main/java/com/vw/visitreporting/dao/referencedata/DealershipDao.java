package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.dto.PagingDTO;
import com.vw.visitreporting.entity.referencedata.Dealership;


/**
 * Contract for a type-safe implementation of AbstractVRDao that adds additional search functionality.
 */
public interface DealershipDao extends VRDao<Dealership>{

	/**
	 * Provide special find functionality to return an Dealer entity by its number.
	 * @param number - the number of the Dealer
	 * @return a single instance matching the number
	 * @throws NoResultException if less than one result is found
	 * @throws NonUniqueResultException if more than one result is found
	 */
	Dealership findByNumber(Integer number);

	
	/**
	 * Search for all dealerhips satisfiying all of the following search criteria.
	 * @param dealerNumber - the dealer number (if null, no filter applied)
	 * @param dealerName - the name of the dealership, can contain * wildcards (if null, no filter applied)
	 * @param franchiseGroupName - the name of the franchise group of the dealership, can contain * wildcards (if null, no filter applied)
	 * @param paging - the paging/sorting settings for the query result
	 * @return a list of dealers (in the order specified in the paging settings) satifying the search criteria.
	 */
	List<Dealership> findfor(Integer dealerNumber, String dealerName, String franchiseGroupName, PagingDTO paging);
}