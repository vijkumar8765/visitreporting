package com.vw.visitreporting.entity.referencedata.enums;


/**
 * The reason for generation of a message board notification.
 * Could be generated automatically by a scheduled event, or could be caused by a user action. 
 */
public enum MessageBoardNotificationRuleType  implements EnumEntity {
	
	SYSTEM(1, "System"),
	USER  (2, "User");

	
	private final Integer id;
	private final String name;

	
	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private MessageBoardNotificationRuleType(int id, String name) {
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