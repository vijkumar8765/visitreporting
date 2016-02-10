package com.vw.visitreporting.entity.referencedata.enums;

import com.vw.visitreporting.entity.accesscontrolled.AccessControlled;


/**
 * A Brand of vehicle owned by the Volkswagen Group.
 */
public enum Brand implements EnumEntity,AccessControlled {
	
	AUDI  (1, "Audi",  "Audi", "http://www.audi.co.uk"),
	SEAT  (2, "SEAT",  "SEAT", "http://www.seat.co.uk"),
	SKODA (3, "Skoda", "Skoda", "http://www.skoda.co.uk"),
	VWPC  (4, "VW PC", "Volkswagen Passenger Cars", "http://www.volkswagen.co.uk"),
	VWCV  (5, "VW CV", "Volkswagen Commercial Vehicles", "http://www.volkswagen-vans.co.uk");

	
	private final Integer id;
	private final String shortName;
	private final String name;
	private final String website;

	
	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private Brand(int id, String shortName, String name, String website) {
		this.id = id;
		this.shortName = shortName;
		this.name = name;
		this.website = website;
	}


	/**
	 * Gets the id that will be stored in the database to reference this instance.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Gets the short name of this brand
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Gets the full name of this brand.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the Internet web-site URL for this brand.
	 */
	public String getWebsite() {
		return website;
	}
}

