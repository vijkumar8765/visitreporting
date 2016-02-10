package com.vw.visitreporting.dao.referencedata;

import java.util.List;
import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.referencedata.Action;
import com.vw.visitreporting.entity.referencedata.Dealership;


public interface ActionDao extends VRDao<Action> {

	/**
	 * Finds all the actions related to a dealership for a given
	 * dealer number. Extra criteria are also applied to filter out closed actions and
	 * actions that the current user should not have access to. Supplying a false
	 * moreActions parameter will filter out actions that are not directly related
	 * to the current user. 
	 * @param dealer - the dealership to find actions for.
	 * @param moreActions - if true will return all actions associated with the dealership, otherwise, only the actions associated also with the currently logged in user.
	 */
	List<Action> getDealershipActions(Dealership dealer, boolean moreActions);
}
