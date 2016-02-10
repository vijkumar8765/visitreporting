package com.vw.visitreporting.entity.referencedata.enums;


/**
 * The status of a document, i.e. whether it is published or not
 */
public enum DocumentStatus implements EnumEntity {
	
	DRAFT     (1, "Draft"),
	PUBLISHED (2, "Published");

	
	private final Integer id;
	private final String name;


	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private DocumentStatus(int id, String name) {
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
	 * Gets the name of this document status.
	 */
	public String getName() {
		return name;
	}
}

