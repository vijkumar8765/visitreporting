package com.vw.visitreporting.entity.referencedata.enums;


/**
 * A dealership can have several postal addresses for different business areas or
 * different office locations, etc. This enum lists the possible categories of address type.
 */
public enum AddressType implements EnumEntity {
	
	MAIN_TRADING      (1, "Main Trading Address"),
	OTHER_SALES       (2, "Other Sales Outlet"),
	SERVICES          (3, "Services Outlet"),
	REGISTERED_OFFICE (4, "Registered Office"),
	DELIVERY          (5, "Delivery Address"),
	NOTICES           (6, "Service of Notices");
	

	private final Integer id;
	private final String name;

	
	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private AddressType(int id, String name) {
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
	 * Gets the name of this address type.
	 */
	public String getName() {
		return name;
	}
}

