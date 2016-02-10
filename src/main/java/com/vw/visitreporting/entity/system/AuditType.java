package com.vw.visitreporting.entity.system;

import com.vw.visitreporting.entity.referencedata.enums.EnumEntity;


/**
 * Possible types of event that can be audited, e.g. entity creation of property modification.
 */
public enum AuditType implements EnumEntity {

	ADD (1, "created"),		//create new entity
	MOD (2, "modified"),	//modify a property value of an existing entity 
	DEL (3, "removed");		//delete an existing entity
	
	
	private final Integer id;
	private final String name;
	

	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private AuditType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	
	/**
	 * Gets the id that will be stored in the database to reference this instance.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Gets the user-friendly string representation of this enum entry
	 */
	public String getName() {
		return name;
	}
}
