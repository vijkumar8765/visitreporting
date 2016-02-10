package com.vw.visitreporting.entity.referencedata.enums;


public enum DocumentType implements EnumEntity {
	
	AGENDA (1, "Agenda"),
	VISIT_REPORT (2, "Visit report");

	
	private final Integer id;
	private final String name;


	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private DocumentType(int id, String name) {
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

