package com.vw.visitreporting.migration.opsplan;


/**
 * Provides an interface for classes that will provide functionality to migrate
 * data into the Visit Reporting database. This may be throught he generation of
 * a SQL script to insert the data, or may use the Hibernate entity model and DAO
 * layer for example. 
 */
public interface Migrator {

	/**
	 * Closes any open resources that were used in the migration.
	 */
	void close();

	/**
	 * Create a new row in the Visit Reporting database for the entity coreesponding
	 * to the given table name.
	 * @param tableName - the table name of the entity to insert
	 * @param srcKey - an identifying key that can be used to lookup the Visit Reporting foreign key of this new row - case sensitive
	 * @param values - collection of property name-vaqlue pairs to insert in the format <colName>=<value>
	 *                 no quotes are required and toDate: and toTime: can be used to convert to date/time formats in the database
	 * @return the primary key of the newly inserted row
	 */
	String newRow(String tableName, String srcKey, String ... values);

	/**
	 * Create a new row in the Visit Reporting database for the entity coreesponding
	 * to the given table name.
	 * @param tableName - the table name of the entity to insert
	 * @param srcKey - an identifying key that can be used to lookup the Visit Reporting foreign key of this new row - case sensitive
	 * @param idCol - if the primary key of this table is not a single column called ID, this should be set to false so that an ID is not auto generated
	 * @param values - collection of property name-vaqlue pairs to insert in the format <colName>=<value>
	 *                 no quotes are required and toDate: and toTime: can be used to convert to date/time formats in the database
	 * @return the primary key of the newly inserted row
	 */
	String newRow(String tableName, String srcKey, boolean idCol, String ... values);
	
	/**
	 * Update an existing row in the Visit Reporting database that was inserted with the newrow method.
	 * @param tableName - the table name of the entity to insert
	 * @param idConstraint - the expression to use in the WHERE clause of the update statement
	 * @param values - the expression to use in the SET section of the update statement
	 */
	void updateRow(String tableName, String idConstraint, String ... values);
	
	/**
	 * Lookup the foreigh key to a row in the visit reporting database that has previously been inserted by this class.
	 * @param tableName - the table name of the entity to type to lookup
	 * @param srcKey - the identifying key that was supplied in the newRow method of this class - case sensitive
	 * @return the primary key of the entity being searched for - never null
	 * @throws NoResultException if the row cannot be found
	 */
	String lookup(String tableName, String srcKey);
}
