package com.vw.visitreporting.entity.referencedata.enums;


/**
 * Provides an interface for all entity enums to implement.
 * This contract ensures that enum values are correctly stored in the database. 
 */
public interface EnumEntity {

	/**
	 * Gets the id that will be stored in the database to reference this instance.
	 */
	Integer getId();
	
	/**
	 * Gets the name of the enum entry that can be displayed to the user.
	 */
	String getName();
}
