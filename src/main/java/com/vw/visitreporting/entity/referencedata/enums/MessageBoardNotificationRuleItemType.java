package com.vw.visitreporting.entity.referencedata.enums;


/**
 * The type of item that a message board notification rule reports on.
 */
public enum MessageBoardNotificationRuleItemType implements EnumEntity {
	
	ACTION(1, "Action"),
	VISIT_REPORT (2, "Visit Report");

	
	private final Integer id;
	private final String name;


	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private MessageBoardNotificationRuleItemType(int id, String name) {
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