package com.vw.visitreporting.service.referencedata;

import com.vw.visitreporting.entity.referencedata.Positions;
import com.vw.visitreporting.service.VRService;

public interface PositionsService extends VRService<Positions>{
	/**
	 * Returns true only if the desired position has any users associated with it.
	 */
	boolean isPositionUsersAssociated(Positions entity);
	
	/**
	 * Returns true only if the desired position is already exists in the system.
	 */
	boolean isPositionExists(Positions entity);

	/**
	 * Returns true only if the desired position sequence is already exists in the system for a given Organisation
	 */
	boolean isPositionSequenceExists(Positions entity);

	/**
	 * Returns true only if the desired position description is being changed during a Position Update.
	 * The below check is required to fulfil one of the business requirement of replacing position name and/or position sequence.
	 */
	boolean isPositionDescriptionBeingChanged(Positions entity);
}
