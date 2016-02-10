package com.vw.visitreporting.dao.referencedata;

import java.util.List;

import com.vw.visitreporting.dao.VRDao;
import com.vw.visitreporting.entity.referencedata.Positions;

public interface PositionsDao extends VRDao<Positions>{

	/**
	 * Find all positions in the database which are not disabled.
	 */
	List<Positions> findEnabledPositions();
	
	/**
	 * Returns true only if the desired position has any users associated with it.
	 */
	boolean isPositionUsersAssociated(Positions entity);
	
	/**
	 * Returns true only if the desired position is already exists in the database.
	 */
	boolean isPositionExists(Positions entity);
	
	/**
	 * Returns true only if the desired position sequence is already exists in the system for a given Organisation.
	 */
	boolean isPositionSequenceExists(Positions entity);
	
	/**
	 * Returns true only if the desired position description is being changed during a Position Update.
	 * The below check is required to fulfil one of the business requirement of replacing position name and/or position sequence.
	 */
	boolean isPositionDescriptionBeingChanged(Positions entity);

}
