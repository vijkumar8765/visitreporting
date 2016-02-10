package com.vw.visitreporting.entity.referencedata.enums;

public enum ActionStatus implements EnumEntity {

	OPEN            (1, "Open"),
	PENDING_CLOSURE (2, "Pending Closure"),
	CLOSED          (3, "Closed");
	

	private final Integer id;
	private final String name;


	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private ActionStatus(int id, String name) {
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
	 * Gets the name of this document type.
	 */
	public String getName() {
		return name;
	}
}
