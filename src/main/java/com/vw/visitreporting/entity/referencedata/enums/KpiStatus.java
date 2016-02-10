package com.vw.visitreporting.entity.referencedata.enums;


/**
 * The current status of a KPI entity. 
 */
public enum KpiStatus implements EnumEntity {

	//TODO: Later we will choose to use separate status enums for statuses of different entitites

	ACTIVE     (1, "Active"),
	EXPIRED    (2, "Expired"),
	TERMINATED (3, "Terminated");

	
	private final Integer id;
	private final String name;


	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private KpiStatus(int id, String name) {
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
	 * Gets the name of the enum entry to display to the user.
	 */
	public String getName() {
		return name;
	}
}

