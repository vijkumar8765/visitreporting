package com.vw.visitreporting.entity.referencedata.enums;


/**
 * A level of authority that a user can have within the company.
 */
public enum Level implements EnumEntity{
	
	ONE   (1, "1"),
	TWO   (2, "2"),
	THREE (3, "3"),
	FOUR  (4, "4"),
	FIVE  (5, "5");

	
	private final Integer id;
	private final String name;

	
	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private Level(int id, String name) {
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

	/**
	 * Gets the integer representation of this level within the company.
	 */
	public int getValue() {
		return this.id;
	}
}

