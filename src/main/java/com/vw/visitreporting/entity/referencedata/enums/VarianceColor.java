package com.vw.visitreporting.entity.referencedata.enums;


/**
 * The colour associated with a KPI actual value based on the variance
 * colour rules that determine the performance of the KPI subject based on the value. 
 */
public enum VarianceColor implements EnumEntity {
	
	RED   (1, "Red"),
	GREEN (2, "Green"),
	AMBER (3, "Amber");

	
	private final Integer id;
	private final String name;


	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private VarianceColor(int id, String name) {
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

