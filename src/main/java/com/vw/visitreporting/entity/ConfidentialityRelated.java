package com.vw.visitreporting.entity;

/**
 * Provides an interface for entities to implement when they can
 * be marked as confidential.
 */
public interface ConfidentialityRelated {

	/**
	 * Gets whether or not this entity has been marked as confidential
	 */
	boolean isConfidential();
}
