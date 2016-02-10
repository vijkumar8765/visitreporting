package com.vw.visitreporting.migration.opsplan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import com.google.common.base.Preconditions;


/**
 * Provides an implementation of the Migrator interface that creates a SQL script to
 * insert data into the Visit Reporting database.
 */
public class SqlScriptMigrator implements Migrator {

    private static final Logger LOG = LoggerFactory.getLogger(SqlScriptMigrator.class);
    private static final DateFormat SRC1_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final DateFormat SRC2_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final DateFormat DST_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final DateFormat DST_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    
    private static final Set<String> TABLES_WITH_AUDIT_INFO = new HashSet<String>();
    static {
    	TABLES_WITH_AUDIT_INFO.addAll(Arrays.asList(new String[] {
    		"BUSINESS_AREA","DRAFT_DOCUMENT_DELETION_RULE","GEOGRAPHICAL_AREA","GEOGRAPHICAL_REGION","GEOGRAPHICAL_STRUCTURE_CATEGORY","KPI","KPI_BUSINESS_RULE","KPI_CATEGORY","MESSAGE_BOARD_NOTIFICATION_RULE","ORGANISATION","POSITIONS","STANDARD_TOPIC","STANDARD_TOPIC_REF","USER_PROFILE","DEALERSHIP","FRANCHISE_GROUP","KPI_SCORE_FILE","USER","ACTION","ACTION_PROGRESS","ACTION_DUE_DATE_CHANGE","ACTION_OWNERSHIP_CHANGE","MEETING_TYPE","DOCUMENT_CONTEXT","DOCUMENT_DETAILS","GENERIC_ACCESS_GRANT"
    	}));
    }

    private final PrintWriter script;
    private final Map<String, Integer> rowCounts;
	private final Map<String, String> rowLookup;
	private final int idOffset;
	private final String auditUserId;
	private final String createDate;
	//private int nextRevisionId;

    
	/**
	 * Constructor. Creates an instance that is ready to start the migration process.
	 * @param migrationScriptFile - the path to the new SQL script file that will be generated (overwriting any existing file at this location)
	 * @param idOffset - the first ID to use for each row in the visit reporting database to avoid clashes with existing data
	 * @param auditUserId - the username of the user that should be specified as the creator of all migrated data
	 * @param auditCreateDate - the timestamp to be used as the creation data of all migrated data
	 */
	public SqlScriptMigrator(File migrationScriptFile, int idOffset, String auditUserId, Date auditCreateDate) {
		try {
			this.script = new PrintWriter(new FileOutputStream(migrationScriptFile));
		} catch(IOException err) {
			throw new IllegalArgumentException("Could not open file ("+migrationScriptFile.getPath()+") for writing: "+err.getMessage(), err);
		}
		this.rowCounts = new HashMap<String, Integer>();
		this.rowLookup = new HashMap<String, String>();
		this.idOffset = idOffset;
		//this.nextRevisionId = 1;
		this.auditUserId = auditUserId;
		this.createDate = DST_DATE_FORMAT.format(auditCreateDate);
	}
	
	/**
	 * Closes any open resources that were used in the migration.
	 */
	public void close() {
		try {
			this.script.close();
		} catch(Exception err) {
			LOG.warn("unable to close SQL script output stream: "+err.getMessage());
		}
	}
	
	/*
	 * Not using Envers anymore
	public final int newAutitRecord(String timestamp, String user) {
		Date date = parseDate(timestamp);
		int rev = this.nextRevisionId++;
		script.print  ("INSERT INTO UserRevisionEntity (REV, TIMESTAMP, USER_ID) ");
		script.println("VALUES("+rev+", "+date.getTime()+", '"+user+"')");
		return rev;
	}
	*/
	
	/**
	 * Create a new row in the Visit Reporting database for the entity coreesponding
	 * to the given table name.
	 * @param tableName - the table name of the entity to insert
	 * @param srcKey - an identifying key that can be used to lookup the Visit Reporting foreign key of this new row - case sensitive
	 * @param values - collection of property name-vaqlue pairs to insert in the format <colName>=<value>
	 *                 no quotes are required and toDate: and toTime: can be used to convert to date/time formats in the database
	 * @return the primary key of the newly inserted row
	 */
	public String newRow(String tableName, String srcKey, String ... values) {
		return newRow(tableName, srcKey, true, values);
	}

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
	public String newRow(String tableName, String srcKey, boolean idCol, String ... values) {//NOPMD
		Preconditions.checkNotNull(tableName, "table name cannot be empty");
		
		//get next id for this table
		String idStr = null;
		if(idCol) {
			if( ! this.rowCounts.containsKey(tableName)) {
				this.rowCounts.put(tableName, 0);
			}
			int id = this.rowCounts.get(tableName) + idOffset;
			this.rowCounts.put(tableName, this.rowCounts.get(tableName) + 1);
			idStr = Integer.toString(id);
		}		
		
		String[][] keyVals = this.getKeyValuePairs(values);
		
		//decide whather to insert createdBy and createdDate entries
		boolean insertCreatedInfo = TABLES_WITH_AUDIT_INFO.contains(tableName);
		
		//construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO "+tableName+" (");
		if(idCol) {
			sql.append("ID, ");
		}
		for(int i=0; i<keyVals.length; i++) {
			if(i>0) { sql.append(", "); }
			sql.append(keyVals[i][0]);
		}
		if(insertCreatedInfo) {
			sql.append(", CREATED_BY, CREATED_DATE");
		}
		sql.append(") values (");
		if(idCol) {
			sql.append(idStr+", ");
		}
		for(int i=0; i<keyVals.length; i++) {
			if(i>0) { sql.append(", "); }
			sql.append(strToSql(keyVals[i][1]));
		}
		if(insertCreatedInfo) {
			sql.append(", '"+auditUserId+"', '"+createDate+"'");
		}
		sql.append(");");
		
		//write SQL insert statement for row in destination database
		script.println(sql);
		
		//add the id of this row to a lookup table
		if(srcKey != null && srcKey.length() > 0) {
			this.rowLookup.put(tableName+":"+srcKey, idStr);
		}
		
		//return the id of the new row generated
		return idStr;
	}
	
	/**
	 * Fixes a string value (possible a representation of a date or time) into a SQL compliant value.
	 */
	private String strToSql(String str) {

		if(str == null || str.trim().length() == 0 || "null".equals(str)) {//NOPMD
			return "null";
		} else if(str.startsWith("toDate:")) {
			Date date = parseDate(str.substring(7));
			if(date == null) {
				return "null";
			} else {
				synchronized(DST_DATE_FORMAT) {
					return "'"+DST_DATE_FORMAT.format(date)+"'";
				}
			}
		} else if(str.startsWith("toTime:")) {
			Date date = parseDate(str.substring(7));
			if(date == null) {
				return "null";
			} else {
				synchronized(DST_TIME_FORMAT) {
					return "'"+DST_TIME_FORMAT.format(date)+"'";
				}
			}
		} else {
			return "'"+str.replace("'", "").replace("\r", "").replace("\n", "  ")+"'";
		}
	}
	
	/**
	 * Update an existing row in the Visit Reporting database that was inserted with the newrow method.
	 * @param tableName - the table name of the entity to insert
	 * @param idConstraint - the expression to use in the WHERE clause of the update statement
	 * @param values - the expression to use in the SET section of the update statement
	 */
	public String lookup(String tableName, String srcKey) {
		String key = tableName+":"+srcKey;
		if( ! this.rowLookup.containsKey(key)) {
			throw new NoResultException("row not found for key "+key);
		}
		return this.rowLookup.get(key);
	}
	
	/**
	 * Lookup the foreigh key to a row in the visit reporting database that has previously been inserted by this class.
	 * @param tableName - the table name of the entity to type to lookup
	 * @param srcKey - the identifying key that was supplied in the newRow method of this class - case sensitive
	 * @return the primary key of the entity being searched for - never null
	 * @throws NoResultException if the row cannot be found
	 */
	public void updateRow(String tableName, String idConstraint, String ... values) {
		
		//construct SQL statement
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE "+tableName+" SET ");
		
		for(int i=0; i<values.length; i++) {
			if(i>0) { sql.append(", "); }
			sql.append(values[i]);
		}
		sql.append(" WHERE ");
		sql.append(idConstraint);
		sql.append(";");

		script.println(sql);
	}
	
	/**
	 * Splits an array os strings in the format <colName>=<value> into a collection of key value pairs.
	 */
	private String[][] getKeyValuePairs(String[] values) {
		String[][] keyVals = new String[values.length][];
		for(int i=0; i<values.length; i++) {
			String val = values[i].trim();
			int pos = val.indexOf('=');
			if(pos < 0) {
				throw new IllegalArgumentException("expected key-value pair from: "+val);
			}
			keyVals[i] = new String[] {
				val.substring(0, pos),
				val.substring(pos + 1)
			};
		}
		return keyVals;
	}
	
	/**
	 * Parse a string representation of a date from the OpsPlan database and return a Date instance.
	 * @throws IllegalArgumentException if the date cannot be parsed
	 */
	private Date parseDate(String strDate) {
		if(strDate == null || strDate.length() == 0 || "null".equals(strDate)) {
			return new Date();
		}
		try {
			synchronized(SRC1_DATE_FORMAT) {
				return SRC1_DATE_FORMAT.parse(strDate);
			}
		} catch(ParseException err1) {
			try {
				synchronized(SRC2_DATE_FORMAT) {
					return SRC2_DATE_FORMAT.parse(strDate);
				}
			} catch(ParseException err) {
				throw new IllegalArgumentException("Could not parse date: "+strDate, err);//NOPMD
			}
		}
	}
}
