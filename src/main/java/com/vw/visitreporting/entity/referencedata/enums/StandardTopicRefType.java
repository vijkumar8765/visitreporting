package com.vw.visitreporting.entity.referencedata.enums;


/**
 * The type of a standard topic.
 */
public enum StandardTopicRefType implements EnumEntity {
	
	KPI            (1, "Kpi"),
	STANDARD_TOPIC (2, "Standard Topic");

	
	private final Integer id;
	private final String name;

	
	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private StandardTopicRefType(int id, String name) {
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

