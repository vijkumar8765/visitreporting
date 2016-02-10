package com.vw.visitreporting.migration.opsplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.vw.visitreporting.dao.system.PersonDao;
import com.vw.visitreporting.dto.PersonDTO;
import com.vw.visitreporting.entity.referencedata.enums.ActionStatus;
import com.vw.visitreporting.entity.referencedata.enums.AddressType;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.StandardTopicRefType;


/**
 * Documents and performs the mapping between column in the OpsPlan database and the Visit Reporting database.
 * Each public method in this class corresponds to a table in the OpsPlan database that must be migrated. Each one
 * will be invoked from the OpsPlanDataMigration class, in the order in which they appear in this class, with
 * a SqlRowSet instance that can be used traverse all data in that table.
 * An instance of the Migrator interface will be used to generate the SQL to insert the data into the Visit Reporting
 * database (or maybe add the data directly, depending on the Migrator implementation).
 */
public class OpsPlanMapping {//NOPMD
	
    private static final Logger LOG = LoggerFactory.getLogger(OpsPlanMapping.class);

    private static final boolean CREATE_ACTIONS_FOR_MEETING_OUTCOMES = true;	//if false no actions will be created in Visit Reporting - all action data will be migrated as topic dicussions
	private static final boolean MIGRATE_TERMINATED_DEALERS = false;	//if false, any dealerships or franchise groups marked as terminated in the OpsPlan database will not be migrated into the Visit Reporting database

	//define mappings and constants for foreign keys in the visit reporting database
	private static final String ORG_ID_DEALER = "0";
	private static final String ORG_ID_VWGUK  = "1";
	private static final String ORG_ID_VWFS   = "2";
	private static final Map<String, String> ORG_TYPES = new HashMap<String, String>();
	static {
		ORG_TYPES.put("Dealerships", ORG_ID_DEALER);
		ORG_TYPES.put("VWG UK", ORG_ID_VWGUK);
		ORG_TYPES.put("VWFS", ORG_ID_VWFS);
	}
	
	private static final Map<String, String> USER_BUS_AREA = new HashMap<String, String>();
	private static final Map<String, Integer> BUS_AREA_CODES = new HashMap<String, Integer>();
	static {
		//from tbl_MeetingMaster.MeetingCode
		BUS_AREA_CODES.put("ADH", 6);	//targeted
		BUS_AREA_CODES.put("ADI", 1);	//Drop In Visit - Aftersales
		BUS_AREA_CODES.put("AFR", 1);	//Aftersales Review
		BUS_AREA_CODES.put("AFS", 1);	//Aftersales Review
		BUS_AREA_CODES.put("Drop", 0);	//Drop In Visit
		BUS_AREA_CODES.put("MTH", 0);	//Review (+Questionnaire)
		BUS_AREA_CODES.put("OPS", 0);	//Operations review
		BUS_AREA_CODES.put("QTR", 0);	//Quarterly Review
		BUS_AREA_CODES.put("RFM", 6);	//Franchise
		BUS_AREA_CODES.put("FRV", 6);	//Franchise
		BUS_AREA_CODES.put("RSE", 0);	//RSE Meeting
		BUS_AREA_CODES.put("TEL", 0);	//Telephone/Email
		BUS_AREA_CODES.put("USED", 3);
		BUS_AREA_CODES.put("Used", 3);
		BUS_AREA_CODES.put("VQM", 6);	//targeted

		//from tbl_Common_Agenda.UserGroup and from PersonDao
		BUS_AREA_CODES.put("Sales", 0);
		BUS_AREA_CODES.put("Operations", 0);
		BUS_AREA_CODES.put("Aftersales", 1);
		BUS_AREA_CODES.put("Franchising", 6);
		BUS_AREA_CODES.put("Admin", 7);
		BUS_AREA_CODES.put("Finance & Legal", 8);
		BUS_AREA_CODES.put("Fleet", 4);
		BUS_AREA_CODES.put("Learning Services", 9);
		BUS_AREA_CODES.put("Marketting", 5);
		BUS_AREA_CODES.put("Parts", 2);
		BUS_AREA_CODES.put("Planning", 10);
		BUS_AREA_CODES.put("Servicing", 11);
		BUS_AREA_CODES.put("Service", 11);
		BUS_AREA_CODES.put("Used Cars", 3);
		BUS_AREA_CODES.put("UsedCars", 3);
	}
	
	private static final Map<String, AddressType> ADDR_TYPES = new HashMap<String, AddressType>();
	static {
		ADDR_TYPES.put("AFRL Site",          AddressType.REGISTERED_OFFICE);
		ADDR_TYPES.put("Chief Exec / MD",    AddressType.REGISTERED_OFFICE);
		ADDR_TYPES.put("Delivery",           AddressType.DELIVERY);
		ADDR_TYPES.put("Main Trading",       AddressType.MAIN_TRADING);
		ADDR_TYPES.put("Main Sales Outlet",  AddressType.MAIN_TRADING);
		ADDR_TYPES.put("Sales Manager",      AddressType.MAIN_TRADING);
		ADDR_TYPES.put("Other Sales Outlet", AddressType.OTHER_SALES);
		ADDR_TYPES.put("Partner",            AddressType.REGISTERED_OFFICE);
		ADDR_TYPES.put("Registered Office",  AddressType.REGISTERED_OFFICE);
		ADDR_TYPES.put("Service of Notices", AddressType.NOTICES);
		ADDR_TYPES.put("Service Outlet",     AddressType.SERVICES);
	}
	
	private static final String ACTION_STATUS_OPEN = ActionStatus.OPEN.getId().toString();
	private static final String ACTION_STATUS_CLOSED = ActionStatus.CLOSED.getId().toString();
	
	//private static final String SYS_START_DATE = "toDate:01/01/2010";
	
	/*
	private static final String KPI_STATUS_CURRENT = "0";
	private static final String KPI_END_DATE   = "toDate:01/01/2015";
	*/

	private final Migrator vr;
	private final PersonDao personDao;
	private String brandId;
	private String annoymousUserId, opsGeogStruct, aftGeogStruct, frnGeogStruct, stdUserProfileId, adminUserProfileId;
	private String posAdmin, posAreaAft, posRegAft, posFran, posFranArea, posArea, posReg, posUsed;
	private final Map<String, String> dealerRemapping = new HashMap<String, String>();
	private final Map<String, Integer> terminatedFranchiseGroupRevisionIds = new HashMap<String, Integer>();
	private final Map<String, Integer> terminatedDealerRevisionIds = new HashMap<String, Integer>();
	private final Map<String, Set<String>> franchiseGroupDealers = new HashMap<String, Set<String>>(); 
	private final Map<String, String> tbl_Common_AgendaLookup = new HashMap<String, String>();
	private final Map<String, List<String>> agendaDealers = new HashMap<String, List<String>>();
	private final Map<String, String> agendaItemToTopic = new HashMap<String, String>();
	private final Map<String, String> agendaItemToMeetingNumber = new HashMap<String, String>();
	private final Map<String, String> recomendationLookup = new HashMap<String, String>();
	private final Map<String, String> nameToUserId = new HashMap<String, String>();


	public OpsPlanMapping(Migrator vr, PersonDao personDao) {
		this.vr = vr;
		this.personDao = personDao;
	}
	
	public void setBrand(Brand brand) {
		this.brandId = brand.getId().toString();
		this.terminatedFranchiseGroupRevisionIds.clear();
		this.terminatedDealerRevisionIds.clear();
		this.franchiseGroupDealers.clear(); 
		this.tbl_Common_AgendaLookup.clear();
		this.agendaDealers.clear();
		this.agendaItemToTopic.clear();
		this.agendaItemToMeetingNumber.clear();
		this.recomendationLookup.clear();
	}
	
	
	public void unmapped() {
		if(this.brandId == null) {
			throw new RuntimeException("Brand has not been set yet");
		}
		
		opsGeogStruct = vr.newRow("GEOGRAPHICAL_STRUCTURE_CATEGORY", "", "NAME=Operations",  "IS_DEFAULT=1", "ORGANISATION_ID="+ORG_ID_VWGUK, "BRAND_ID="+brandId);
		aftGeogStruct = vr.newRow("GEOGRAPHICAL_STRUCTURE_CATEGORY", "", "NAME=Aftersales",  "IS_DEFAULT=1", "ORGANISATION_ID="+ORG_ID_VWGUK, "BRAND_ID="+brandId);
		frnGeogStruct = vr.newRow("GEOGRAPHICAL_STRUCTURE_CATEGORY", "", "NAME=Franchising", "IS_DEFAULT=1", "ORGANISATION_ID="+ORG_ID_VWGUK, "BRAND_ID="+brandId);
		//vr.newRow("GEOGRAPHICAL_STRUCTURE_CATEGORY", "", "NAME=Other",       "IS_DEFAULT=1", "ORGANISATION_ID=VWG UK", "BRAND_ID="+brandId);

		posAdmin   = vr.newRow("POSITIONS", "Admin",                  "DESCRIPTION=Admin",                  "LEVEL=3", "POSITION_SEQUENCE=0", "VISIBLE_WITHIN_CONTACT_SCREEN=0", "ORGANISATION_ID="+ORG_ID_VWGUK);
		posAreaAft = vr.newRow("POSITIONS", "Area Aftersales",        "DESCRIPTION=Area Aftersales",        "LEVEL=3", "POSITION_SEQUENCE=1", "VISIBLE_WITHIN_CONTACT_SCREEN=0", "ORGANISATION_ID="+ORG_ID_VWGUK);
		posRegAft  = vr.newRow("POSITIONS", "Regional Aftersales",    "DESCRIPTION=Regional Aftersales",    "LEVEL=4", "POSITION_SEQUENCE=1", "VISIBLE_WITHIN_CONTACT_SCREEN=0", "ORGANISATION_ID="+ORG_ID_VWGUK);
		posFran    = vr.newRow("POSITIONS", "Franchise Manager",      "DESCRIPTION=Franchise Manager",      "LEVEL=4", "POSITION_SEQUENCE=2", "VISIBLE_WITHIN_CONTACT_SCREEN=0", "ORGANISATION_ID="+ORG_ID_VWGUK);
		posFranArea= vr.newRow("POSITIONS", "Franchise Area Manager", "DESCRIPTION=Franchise Area Manager", "LEVEL=2", "POSITION_SEQUENCE=3", "VISIBLE_WITHIN_CONTACT_SCREEN=0", "ORGANISATION_ID="+ORG_ID_VWGUK);
		posArea    = vr.newRow("POSITIONS", "Area Manager",           "DESCRIPTION=Area Manager",           "LEVEL=3", "POSITION_SEQUENCE=4", "VISIBLE_WITHIN_CONTACT_SCREEN=0", "ORGANISATION_ID="+ORG_ID_VWGUK);
		posReg     = vr.newRow("POSITIONS", "Regional Manager",       "DESCRIPTION=Regional Manager",       "LEVEL=2", "POSITION_SEQUENCE=5", "VISIBLE_WITHIN_CONTACT_SCREEN=0", "ORGANISATION_ID="+ORG_ID_VWGUK);
		posUsed    = vr.newRow("POSITIONS", "Used Car Supply Manager","DESCRIPTION=Used Car Supply Manager","LEVEL=2", "POSITION_SEQUENCE=6", "VISIBLE_WITHIN_CONTACT_SCREEN=0", "ORGANISATION_ID="+ORG_ID_VWGUK);

		stdUserProfileId   = vr.newRow("USER_PROFILE", "", "NAME=OpsPlan Standard User");
		adminUserProfileId = vr.newRow("USER_PROFILE", "", "NAME=OpsPlan Admin User");
		
		annoymousUserId = "opsPlanUnknown";
		vr.newRow("USER", "", false,
			"USER_ID="+annoymousUserId,
			"EMAIL_ADDRESS=OpsPlanUnknown@vwg.co.uk",
			"LANDLINE=0",
			"MOBILE=0",
			"TYPE_OF_ORGANISATION="+ORG_ID_VWGUK,
			"USER.ORGANISATION_ID="+ORG_ID_VWGUK,
			"POSITION_ID=0",
			"FIRST_NAME=OpsPlan",
			"SUR_NAME=Unknown",
			"IS_DISABLED=1"
		);
	
		/*
		 * Not migrated as these are not properly used in OpsPlan
		 * 
		vr.newRow("KPI_CATEGORY", "", "NAME=New Volume",   "DESCRIPTION=New Volume");
		vr.newRow("KPI_CATEGORY", "", "NAME=Used Volume",  "DESCRIPTION=Used Volume");
		vr.newRow("KPI_CATEGORY", "", "NAME=Quality",      "DESCRIPTION=Quality");
		vr.newRow("KPI_CATEGORY", "", "NAME=Repairs",      "DESCRIPTION=Repairs");
		vr.newRow("KPI_CATEGORY", "", "NAME=Customer Satisfaction", "DESCRIPTION=Customer Satisfaction");
		vr.newRow("KPI_CATEGORY", "", "NAME=Parts",        "DESCRIPTION=Parts");
		vr.newRow("KPI_CATEGORY", "", "NAME=Retail Hours", "DESCRIPTION=Retail Hours");
		vr.newRow("KPI_CATEGORY", "", "NAME=Service Hours","DESCRIPTION=Service Hours");
		vr.newRow("KPI_CATEGORY", "", "NAME=Other",        "DESCRIPTION=Other");
	
		vr.newRow("KPI", "", "KPI_CATEGORY_ID=0", "KPI_DESCRIPTION=New Car Target Hit (%)",           "EFFECTIVE_START_DATE="+SYS_START_DATE, "EFFECTIVE_END_DATE="+KPI_END_DATE, "STATUS="+KPI_STATUS_CURRENT, "ORGANISATION_ID="+ORG_ID_VWGUK);
		vr.newRow("KPI", "", "KPI_CATEGORY_ID=1", "KPI_DESCRIPTION=Used Car Target v VBIS Sales (%)", "EFFECTIVE_START_DATE="+SYS_START_DATE, "EFFECTIVE_END_DATE="+KPI_END_DATE, "STATUS="+KPI_STATUS_CURRENT, "ORGANISATION_ID="+ORG_ID_VWGUK);
		vr.newRow("KPI", "", "KPI_CATEGORY_ID=5", "KPI_DESCRIPTION=Parts Target (%)",                 "EFFECTIVE_START_DATE="+SYS_START_DATE, "EFFECTIVE_END_DATE="+KPI_END_DATE, "STATUS="+KPI_STATUS_CURRENT, "ORGANISATION_ID="+ORG_ID_VWGUK);
		vr.newRow("KPI", "", "KPI_CATEGORY_ID=7", "KPI_DESCRIPTION=Service Hours Achievement (%)",    "EFFECTIVE_START_DATE="+SYS_START_DATE, "EFFECTIVE_END_DATE="+KPI_END_DATE, "STATUS="+KPI_STATUS_CURRENT, "ORGANISATION_ID="+ORG_ID_VWGUK);
		vr.newRow("KPI", "", "KPI_CATEGORY_ID=8", "KPI_DESCRIPTION=CEM Aftersales Score (%)",         "EFFECTIVE_START_DATE="+SYS_START_DATE, "EFFECTIVE_END_DATE="+KPI_END_DATE, "STATUS="+KPI_STATUS_CURRENT, "ORGANISATION_ID="+ORG_ID_VWGUK);
		vr.newRow("KPI", "", "KPI_CATEGORY_ID=8", "KPI_DESCRIPTION=CEM Sales Score (%)",              "EFFECTIVE_START_DATE="+SYS_START_DATE, "EFFECTIVE_END_DATE="+KPI_END_DATE, "STATUS="+KPI_STATUS_CURRENT, "ORGANISATION_ID="+ORG_ID_VWGUK);
		vr.newRow("KPI", "", "KPI_CATEGORY_ID=8", "KPI_DESCRIPTION=Return on Sales (MAT) (%)",        "EFFECTIVE_START_DATE="+SYS_START_DATE, "EFFECTIVE_END_DATE="+KPI_END_DATE, "STATUS="+KPI_STATUS_CURRENT, "ORGANISATION_ID="+ORG_ID_VWGUK);
		vr.newRow("KPI", "", "KPI_CATEGORY_ID=8", "KPI_DESCRIPTION=PCP (%)",                          "EFFECTIVE_START_DATE="+SYS_START_DATE, "EFFECTIVE_END_DATE="+KPI_END_DATE, "STATUS="+KPI_STATUS_CURRENT, "ORGANISATION_ID="+ORG_ID_VWGUK);
		vr.newRow("KPI", "", "KPI_CATEGORY_ID=2", "KPI_DESCRIPTION=Douglas Stafford Walk Ins",        "EFFECTIVE_START_DATE="+SYS_START_DATE, "EFFECTIVE_END_DATE="+KPI_END_DATE, "STATUS="+KPI_STATUS_CURRENT, "ORGANISATION_ID="+ORG_ID_VWGUK);
	
		vr.newRow("KPI_BUSINESS_AREA", "", "KPI_ID=0", "BUSINESS_AREA_ID="+BUS_AREA_SALES);
		vr.newRow("KPI_BUSINESS_AREA", "", "KPI_ID=1", "BUSINESS_AREA_ID="+BUS_AREA_SALES);
		vr.newRow("KPI_BUSINESS_AREA", "", "KPI_ID=2", "BUSINESS_AREA_ID="+BUS_AREA_PARTS);
		vr.newRow("KPI_BUSINESS_AREA", "", "KPI_ID=3", "BUSINESS_AREA_ID="+BUS_AREA_SERVI);
		vr.newRow("KPI_BUSINESS_AREA", "", "KPI_ID=4", "BUSINESS_AREA_ID="+BUS_AREA_AFTER);
		vr.newRow("KPI_BUSINESS_AREA", "", "KPI_ID=5", "BUSINESS_AREA_ID="+BUS_AREA_SALES);
		vr.newRow("KPI_BUSINESS_AREA", "", "KPI_ID=6", "BUSINESS_AREA_ID="+BUS_AREA_SALES);
		vr.newRow("KPI_BUSINESS_AREA", "", "KPI_ID=7", "BUSINESS_AREA_ID="+BUS_AREA_SALES);
		vr.newRow("KPI_BUSINESS_AREA", "", "KPI_ID=8", "BUSINESS_AREA_ID="+BUS_AREA_SALES);
	
		for(int i=0; i<9; i++) {
			for(String brand : BRANDS) {
				vr.newRow("KPI_BRAND", "", "KPI_ID="+i, "BRAND_ID="+brand);
			}
		}
		*/
	}
	

	public void tbl_Event(SqlRowSet row) {//NOPMD
		
		if(row.getString("EventTypeClass") == null || row.getString("EventTypeClass").contains("Share")) {
			return;		//if event type is share then it is not a change of ownership, just a share purchase, so skip row
		}
		int eventType = row.getInt("EventTypeID");
		if(eventType == 13 || eventType == 8 || eventType == 9) {
			return;		//if event was a relocation, new dealer or new franchise group then skip
		}
		
		int revisionId = 1;
		/*
		if(MIGRATE_AUDIT_HISTORY) {
			revisionId = vr.newAutitRecord(row.getString("EventDate"), "OpsPlan");
		}
		*/
		
		switch(eventType) {
			case 3:		//Change of Ownership
				dealerRemapping.put(fixDealerNumber(row.getString("KeyBefore")), fixDealerNumber(row.getString("KeyAfter")));
				break;
			case 10:	//Terminated UPC
				terminatedFranchiseGroupRevisionIds.put(row.getString("KeyAfter"), revisionId);
				break;
			case 14:	//Terminated Retailer
				terminatedDealerRevisionIds.put(fixDealerNumber(row.getString("KeyAfter")), revisionId);
				break;
			default:
				//throw new IllegalArgumentException("unrecognised event type id: "+row.getInt("EventTypeID"));
		}
	}

	public void tbl_UPC(SqlRowSet row) {//NOPMD
		
		String upcKey = row.getString("UPCKey");
		
		//if this franchise group has been terminated and we are not migrating terminated dealers then skip this group
		if( ! MIGRATE_TERMINATED_DEALERS && terminatedFranchiseGroupRevisionIds.containsKey(upcKey)) {
			return;
		}

		String franchise_group_id = vr.newRow("FRANCHISE_GROUP", upcKey,
			"NAME="+row.getString("UPC")
		);
		
		vr.newRow("FRANCHISE_GROUP_BRAND", "", false,
			"FRANCHISE_GROUP_ID="+franchise_group_id,
			"BRAND_ID="+this.brandId
		);			
		
		//if there is an audit revision for the deletion of this entity then migrate it
		if(terminatedFranchiseGroupRevisionIds.containsKey(upcKey)) {
			
			vr.updateRow("FRANCHISE_GROUP", "ID="+franchise_group_id, "TERMINATED=1");

			//int revId = terminatedFranchiseGroupRevisionIds.get(upcKey);
			
			//vr.newAuditRow("FRANCHISE_GROUP", franchise_group_id, revId, "MOD", "TERMINATED=1");
		}
	}
	
	public void tbl_RetailerFranchising(SqlRowSet row) {//NOPMD		
		String number = fixDealerNumber(row.getString("RetailerNumber"));
		if("00000".equals(number)) {
			return;			//this is a dummy entry
		}
		if(row.getString("RetailerName") == null || row.getString("RetailerName").trim().length() == 0) {
			return;	//skip dealers without a name
		}

		//if this dealer's franchise group has been terminated and we are not migrating terminated dealers then skip this dealer
		if( ! MIGRATE_TERMINATED_DEALERS && terminatedFranchiseGroupRevisionIds.containsKey(row.getString("UPCKey"))) {
			return;
		}
		
		//if this row is for a previous version of a dealer then skip it
		if(dealerRemapping.containsKey(number)) {
			return;
		}

		
		//if this dealer has no franchise group associated with it then create one with the same name
		String franchise_group_id = null;
		if(row.getString("UPCKey") == null || row.getString("UPCKey").trim().length() == 0 || "NONE".equals(row.getString("UPCKey"))) {
			franchise_group_id = vr.newRow("FRANCHISE_GROUP", "",
				"NAME="+row.getString("RetailerName")
			);
			vr.newRow("FRANCHISE_GROUP_BRAND", "", false,
				"FRANCHISE_GROUP_ID="+franchise_group_id,
				"BRAND_ID="+this.brandId
			);
			
		//otherwise, lookup the existing franchise group
		} else {
			franchise_group_id = vr.lookup("FRANCHISE_GROUP", row.getString("UPCKey"));
		}


		//if this dealer has been terminated and we are not migrating terminated dealers then skip this dealer
		if( ! MIGRATE_TERMINATED_DEALERS && terminatedDealerRevisionIds.containsKey(number)) {
			return;
		}
		
		//insert dealer row
		String dealer_id = vr.newRow("DEALERSHIP", number,
			"BRAND_ID="+brandId,
			"DEALER_NUMBER="+number,
			"DEALER_NAME="+row.getString("RetailerName"),
			"FRANCHISE_GROUP_ID="+franchise_group_id
		);
		
		
		//if there is an audit revision for the deletion of this entity then migrate it
		if(terminatedDealerRevisionIds.containsKey(number)) {

			vr.updateRow("DEALERSHIP", "ID="+dealer_id, "TERMINATED=1");

			//int revId = terminatedDealerRevisionIds.get(number);

			//vr.newAuditRow("DEALERSHIP", dealer_id, revId, "MOD", "TERMINATED=1");
		}
		
		
		//map OpsPlan franchise key to collection of dealer_ids in VR
		if( ! franchiseGroupDealers.containsKey(row.getString("UPCKey"))) {
			franchiseGroupDealers.put(row.getString("UPCKey"), new HashSet<String>());
		}
		franchiseGroupDealers.get(row.getString("UPCKey")).add(dealer_id);
	}
	
	public void tbl_RetailerDept(SqlRowSet row) {//NOPMD
		String number = fixDealerNumber(row.getString("RetailerNumber"));
		if(dealerRemapping.containsKey(number)) {
			return;
		}
		String dealer_id = lookupDealer(number);
		if(dealer_id == null) {
			return;
		}
		String dept = row.getString("DeptName");
		if("New".equals(dept)) {
			return;
		}
		vr.newRow("DEALERSHIP_BUSINESS_AREA", "", false,
			"DEALERSHIP_ID="+dealer_id,
			"BUSINESS_AREA_ID="+toBusArea(null, dept)
		);
	}
	
	public void tbl_RetailerHierarchy(SqlRowSet row) {//NOPMD

		String dealer_id = lookupDealer(row.getString("RetailerNumber"));
		if(dealer_id == null) {
			return;
		}
		
		String opsRegnId = vr.newRow("GEOGRAPHICAL_REGION", Integer.parseInt(row.getString("RegionOperations"))+":"+opsGeogStruct, "DESCRIPTION="+row.getString("RegionOperations"), "GEOGRAPHICAL_STRUCTURE_CATEGORY_ID="+opsGeogStruct);
		String opsAreaId = vr.newRow("GEOGRAPHICAL_AREA",   Integer.parseInt(row.getString("AreaOperations"))+":"+opsGeogStruct,   "DESCRIPTION="+row.getString("AreaOperations"),   "GEOGRAPHICAL_REGION_ID="+opsRegnId);
		vr.newRow("DEALERSHIP_GEOGRAPHICAL_STRUCTURE_AREA", "", false, "GEOGRAPHICAL_AREA_ID="+opsAreaId, "DEALERSHIP_ID="+dealer_id);

		String aftRegnId = vr.newRow("GEOGRAPHICAL_REGION", Integer.parseInt(row.getString("RegionAftersales"))+":"+aftGeogStruct, "DESCRIPTION="+row.getString("RegionAftersales"), "GEOGRAPHICAL_STRUCTURE_CATEGORY_ID="+aftGeogStruct);
		String aftAreaId = vr.newRow("GEOGRAPHICAL_AREA",   Integer.parseInt(row.getString("AreaAftersales"))+":"+aftGeogStruct,   "DESCRIPTION="+row.getString("AreaAftersales"),   "GEOGRAPHICAL_REGION_ID="+aftRegnId);
		vr.newRow("DEALERSHIP_GEOGRAPHICAL_STRUCTURE_AREA", "", false, "GEOGRAPHICAL_AREA_ID="+aftAreaId, "DEALERSHIP_ID="+dealer_id);
	
		String frnRegnId = vr.newRow("GEOGRAPHICAL_REGION", Integer.parseInt(row.getString("RegionFranchising"))+":"+frnGeogStruct, "DESCRIPTION="+row.getString("RegionFranchising"), "GEOGRAPHICAL_STRUCTURE_CATEGORY_ID="+frnGeogStruct);
		String frnAreaId = vr.newRow("GEOGRAPHICAL_AREA",   Integer.parseInt(row.getString("AreaFranchising"))+":"+frnGeogStruct,   "DESCRIPTION="+row.getString("AreaFranchising"),   "GEOGRAPHICAL_REGION_ID="+frnRegnId);
		vr.newRow("DEALERSHIP_GEOGRAPHICAL_STRUCTURE_AREA", "", false, "GEOGRAPHICAL_AREA_ID="+frnAreaId, "DEALERSHIP_ID="+dealer_id);

		/*
		 * Don't migrate "other" geographic category because most retailers are not part of this structure
		 * 
		String othRegnId = vr.newRow("GEOGRAPHICAL_REGION", Integer.parseInt(row.getString("RegionOther"))+":3", "DESCRIPTION="+row.getString("RegionOther"), "GEOGRAPHICAL_STRUCTURE_CATEGORY_ID=3");
		String othAreaId = vr.newRow("GEOGRAPHICAL_AREA",   Integer.parseInt(row.getString("AreaOther"))+":3",   "DESCRIPTION="+row.getString("AreaOther"),   "GEOGRAPHICAL_REGION_ID="+othRegnId);
		vr.newRow("DEALERSHIP_GEOGRAPHICAL_STRUCTURE_AREA", "", false, "GEOGRAPHICAL_AREA_ID="+othAreaId, "DEALERSHIP_ID="+dealer_id);
		*/
		
		//loop through franchise groups and find mapping from dealer to area - then update with this dealer franchise area
		for(String franchiseGroup : franchiseGroupDealers.keySet()) {
			Set<String> dealerSet = franchiseGroupDealers.get(franchiseGroup);
			if(dealerSet.contains(dealer_id) && ! franchiseGroup.contains(" (Area ")) {
				String key = franchiseGroup+" (Area "+row.getString("AreaFranchising")+")";
				if( ! franchiseGroupDealers.containsKey(key)) {
					franchiseGroupDealers.put(key, new HashSet<String>());
				}
				franchiseGroupDealers.get(key).add(dealer_id);
				break;
			}
		}
	}
	
	public void tbl_AppUser(SqlRowSet row) {//NOPMD
		
		if(nameToUserId.containsKey(row.getString("Salutation").toLowerCase())) {
			return;		//don't add the same person twice
		}
		
		PersonDTO userDetails = this.personDao.getPersonByUsername(row.getString("UserID"));
		if(userDetails == null) {
			LOG.warn("Person information not found for user ID: "+row.getString("UserID")+" - this user will not be added.");
			return;
		}
		if(nameToUserId.containsValue(userDetails.getUsername().toLowerCase())) {
			return;		//don't add the same person twice
		}

		String userType = row.getString("UserType");
		String position_id = null;
		String gs_category_id = null;
		if("ADMIN".equals(userType)) {
			position_id = posAdmin;		gs_category_id = opsGeogStruct;
		} else if("AFTERSALES".equals(userType)) {
			position_id = posAreaAft;	gs_category_id = aftGeogStruct;
		} else if("AFTERSALER".equals(userType)) {
			position_id = posRegAft;	gs_category_id = aftGeogStruct;
		} else if("AREA".equals(userType)) {
			position_id = posArea;		gs_category_id = opsGeogStruct;//frnGeogStruct;
		} else if("FRANAM".equals(userType)) {
			position_id = posFranArea;	gs_category_id = opsGeogStruct;//frnGeogStruct;
		} else if("FRANCHISE".equals(userType)) {
			position_id = posFran;		gs_category_id = frnGeogStruct;
		} else if("REGION".equals(userType)) {
			position_id = posReg;		gs_category_id = opsGeogStruct;
		} else if("USEDCARS".equals(userType)) {
			position_id = posUsed;		gs_category_id = opsGeogStruct;
		} else if("GUEST".equals(userType)) {
			return;
		} else {
			throw new IllegalArgumentException("unrecognised user type: "+userType);
		}
		
		//String fullname = row.getString("ReportName");
		
		String org = ORG_TYPES.get(userDetails.getOrganisation());
		
		String user_id = userDetails.getUsername().toLowerCase();
		nameToUserId.put(row.getString("Salutation").toLowerCase(), user_id);
		
		vr.newRow("USER", row.getString("Salutation"), false,
			"USER_ID="+user_id,
			"EMAIL_ADDRESS="+userDetails.getEmail(),
			"LANDLINE="+userDetails.getLandline(),
			"MOBILE="+userDetails.getMobile(),
			"TYPE_OF_ORGANISATION="+org,
			"ORGANISATION_ID="+org,
			"POSITION_ID="+position_id,
			"FIRST_NAME="+userDetails.getFirstname(),	//fullname.split(",")[1].trim(),
			"SUR_NAME="+userDetails.getSurname(),	//fullname.split(",")[0].trim(),
			"IS_DISABLED="+ (isTrue(row, "Active") ? "0" : "1")
		);
		vr.newRow("USER_BRAND", "", false,
			"BRAND_ID="+this.brandId,
			"USER_ID="+user_id
		);
		vr.newRow("USER_GS_CATEGORY", "", false,
			"GS_CATEGORY_ID="+gs_category_id,
			"USER_ID="+user_id
		);
		vr.newRow("USER_USER_PROFILE", "", false,
			"USER_PROFILE_ID="+ (isTrue(row, "Administrator") ? adminUserProfileId : stdUserProfileId),
			"USER_ID="+user_id
		);

		int accessLevel = row.getInt("Access_Level");
		if(accessLevel > 0) {
			if(accessLevel < 10  &&  ! this.brandId.equals(Brand.SKODA.getId().toString())) {
				
				vr.newRow("USER_GEOGRAPHICAL_REGION", "", false,
					"REGION_ID="+vr.lookup("GEOGRAPHICAL_REGION", accessLevel+":"+gs_category_id),
					"USER_ID="+user_id
				);
			} else {
				
				vr.newRow("USER_GEOGRAPHICAL_AREA", "", false,
					"AREA_ID="+vr.lookup("GEOGRAPHICAL_AREA", accessLevel+":"+gs_category_id),
					"USER_ID="+user_id
				);
			}
		}

		//remember business area of this user
		USER_BUS_AREA.put(row.getString("UserID"), userDetails.getBusinessArea());
	}
	
	public void tbl_Contact(SqlRowSet row) {//NOPMD
		
		if(row.getString("contact_name") == null) {
			return;		//if contact does not have a name then skip
		}
		
		boolean isFranchiseGroupContact = "U".equals(row.getString("ContactScope"));

		String association = null;
		if(isFranchiseGroupContact) {
			//if this contacts's franchise group has been terminated and we are not migrating terminated dealers then skip this contact
			if( ! MIGRATE_TERMINATED_DEALERS && terminatedFranchiseGroupRevisionIds.containsKey(row.getString("ContactKey"))) {
				return;
			}
			association = "FRANCHISE_GROUP_ID="+vr.lookup("FRANCHISE_GROUP", row.getString("ContactKey"));
		} else {
			String dealer_id = lookupDealer(row.getString("retailer_number"));
			if(dealer_id == null) {
				return;		//skip this contact if we can't find the dealer that it is associated to
			}
			association = "DEALERSHIP_ID="+dealer_id;
		}

		
		String org = null;
		if(isTrue(row, "volkswagen")) {
			org = ORG_ID_VWGUK;
		} else {
			org = ORG_ID_DEALER;
		}


		String position_id = null;
		try {
			position_id = vr.lookup("POSITIONS", row.getString("contact_position"));
		} catch(NoResultException err) {
			position_id = vr.newRow("POSITIONS", row.getString("contact_position"),
				"DESCRIPTION="+row.getString("contact_position"),
				"LEVEL="+ (isFranchiseGroupContact ? "4" : "5"),
				"VISIBLE_WITHIN_CONTACT_SCREEN=1",
				"ORGANISATION_ID="+org
			);
		}


		//try to lookup person details by email address if it exists
		PersonDTO userDetails = this.personDao.getPersonByEmailAddress(row.getString("contact_email"));

		String user_id = null;
		if(userDetails == null) {	//if not found
			user_id = nameToUserId.get(row.getString("contact_name").toLowerCase());
			if(user_id == null) {
				LOG.warn("Person data not found for contact "+row.getString("contact_name")+" -  email address: "+row.getString("contact_email"));
				return;		//if user has not already been added and we don't have details for their email address then skip them (as we are not able to find their user ID)
			}
			
		//otherwise, if email address found by person service
		} else {
			user_id = userDetails.getUsername().toLowerCase();
			if( ! nameToUserId.containsValue(user_id)) {	//if user has not been added already

				//create new user
				nameToUserId.put(userDetails.toString().toLowerCase(), user_id);
				
				vr.newRow("USER", userDetails.toString(), false,
					"USER_ID="+user_id,
					"EMAIL_ADDRESS="+row.getString("contact_email"),
					"LANDLINE="+row.getString("contact_telephone"),
					"MOBILE="+userDetails.getMobile(),
					"TYPE_OF_ORGANISATION="+org,
					"ORGANISATION_ID="+org,
					"POSITION_ID="+position_id,
					"FIRST_NAME="+userDetails.getFirstname(),
					"SUR_NAME="+userDetails.getSurname(),
					"IS_DISABLED=1",	//contact will not be able to log into the system by default
					association
				);				
				user_id = null;
			}
		}
		if(user_id != null) {	//if an existing user id was found, update it with the new information
				
			vr.updateRow("USER", "USER_ID='"+user_id+"'",
					"TYPE_OF_ORGANISATION="+org,
					"ORGANISATION_ID="+org,
					"POSITION_ID="+position_id,
					association
				);
		}
	}
	
	public void tbl_Address(SqlRowSet row) {//NOPMD
		
		//skip applicant and franchise group addresses
		if("A".equals(row.getString("AddressScope")) || "U".equals(row.getString("AddressScope"))) {
			return;
		}

		String houseNumber = "";
		String street1 = row.getString("Street");
		try {
			houseNumber = Integer.valueOf(street1.substring(0, street1.indexOf(' '))).toString();//NOPMD
			street1 = street1.substring(street1.indexOf(' ')).trim();
		} catch(Exception err) {//NOPMD
			//leave street1 as it is and leave houseNumber blank
		}
		if(street1 == null || street1.trim().length() == 0) {//NOPMD
			return;		//skip this address if street name is not filled out
		}
		street1 = street1.replace("\n", ", ");
		
		String dealer_id = lookupDealer(row.getString("AddressKey"));
		if(dealer_id == null) {
			return;
		}
		
		AddressType type = ADDR_TYPES.get(row.getString("AddressType"));
		
		String postcode = ((row.getString("PostCode") == null || row.getString("PostCode").length() == 0) ? "-" : row.getString("PostCode"));
		
		vr.newRow("ADDRESS", "",
			"COUNTRY=UK",
			"DEALERSHIP_ID="+dealer_id,
			"ADDRESS_TYPE="+type.getId(),
			"HOUSE_NUMBER="+houseNumber,
			"STREET_NAME1="+street1,
			"TOWN="+row.getString("Town"),
			"COUNTY="+row.getString("County"),
			"POST_CODE="+postcode,
			"DISTRICT="+row.getString("District")
		);
		
		if(type == AddressType.MAIN_TRADING) {
			vr.updateRow("DEALERSHIP", "ID="+dealer_id,
				//"TRADING_ADDRESS_ID="+addr_id,
				"MAINLINE_TELEPHONE='"+row.getString("Telephone")+"'",
				"FAX_NUMBER='"+row.getString("Fax")+"'"
			);
		} else {
			vr.updateRow("DEALERSHIP", "ID="+dealer_id,
				"MAINLINE_TELEPHONE='"+row.getString("Telephone")+"'",
				"FAX_NUMBER='"+row.getString("Fax")+"'"
			);
		}
	}
	
	/*
	 * Not migrated as these are not properly used in OpsPlan
	public void tbl_KPI(SqlRowSet row) {//NOPMD
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(row.getDate("TargetUpdateDate").getTime());
		
		boolean national = "00000".equals(row.getString("RetailerNumber"));
		String dealer_id = "";
		if( ! national) {
			dealer_id = lookupDealer(row.getString("RetailerNumber"));
		}
		
		for(int i=0; i<9; i++) {
			
			vr.newRow("DEALER_KPI_TARGET", "",
				"KPI_ID="+i,
				"YEAR="+cal.get(GregorianCalendar.YEAR),
				"DEALERSHIP_ID="+dealer_id,
				cal.getDisplayName(GregorianCalendar.MONTH, GregorianCalendar.LONG, Locale.UK).toUpperCase() + "_SCORE=" + row.getString("KPITarget0"+(i+1))
			);
			
			vr.newRow("DEALER_KPI_ACTUAL", "",
				"KPI_ID="+i,
				"SCORE_MONTH="+(cal.get(GregorianCalendar.MONTH)+1),
				"SCORE_YEAR="+cal.get(GregorianCalendar.YEAR),
				"DEALERSHIP_ID="+dealer_id,
				(national ? "NATIONAL_ACTUAL_SCORE=" : "DEALER_SCORE=") + row.getString("KPIActual0"+(i+1))
			);
		}
	}
	*/
	
	/*
	 * not migrated as these actions are related to franchise applications
	public void tbl_Process(SqlRowSet row) {//NOPMD
		
		String topic_ref_if = vr.newRow("STANDARD_TOPIC_REF", row.getString("Category"),
			"TITLE="+row.getString("Category"),
			"DESCRIPTION=Type: "+row.getString("Type")
		);
		
		String topic_id = vr.newRow("STANDARD_TOPIC", row.getString("Category"),
			"ORGANISATION_ID="+ORG_ID_VWGUK,
			"STANDARD_TOPIC_REF_TYPE=0",					//todo: create topic type enum
			"EFFECTIVE_START_DATE="+SYS_START_DATE,
			"STANDARD_TOPIC_REF_ID="+topic_ref_if,
			"CREATED_BY="+row.getString("Author")
		);
		
		procIdToTopic.put(row.getString("ProcessID"), topic_id);
	}

	public void tbl_ActiveTaskStep(SqlRowSet row) {//NOPMD
		
		String status_id = ACTION_STATUS_OPEN;
		if(isTrue(row, "Completed")) {
			status_id = ACTION_STATUS_CLOSED;
		}
		
		String milestone = (Strings.isNullOrEmpty(row.getString("Milestone")) ? "" : ". Milestone: "+row.getString("Milestone"));
		String notes = (Strings.isNullOrEmpty(row.getString("Notes")) ? "" : ". Notes: "+row.getString("Notes"));
		
		String action_id = vr.newRow("ACTION", row.getString("TaskID"),
			"DESCRIPTION="+row.getString("Description") + milestone + notes,
			"STATUS="+status_id,
			"DUE_DATE=toDate:"+row.getString("DueDate"),
			"CONFIDENTIAL_FLAG=1",
			"ORGANISATION_ID="+ORG_ID_VWGUK,
			"MODIFIED_BY="+row.getString("CompletedBy")
		);
		
		vr.newRow("ACTION_PROGRESS", "",
			"ACTION_ID="+action_id,
			"PROGRESS_DATE=toDate:"+row.getString("CompletedDate"),
			"CREATED_BY="+row.getString("CompletedBy"),
			"DESCRIPTION=Completed"+milestone
		);
		
		if( ! Strings.isNullOrEmpty(row.getString("CompletedBy"))) {
			vr.newRow("ACTION_OWNER", "", false,
				"ACTION_ID="+action_id,
				"OWNER_USER_ID="+row.getString("CompletedBy")
			);
		}
	}	
	
	public void tbl_ActiveTask(SqlRowSet row) {//NOPMD
		
		String scope = row.getString("TaskScope");
		if("A".equals(scope)) {	//applicant
			return;
		}
		
		String action_id = vr.lookup("ACTION", row.getString("TaskID"));
		String dealer_id = lookupDealer(row.getString("TaskKey"));
		String topic_id = procIdToTopic.get(row.getString("ProcessID"));
		if(dealer_id == null) {
			return;
		}
		
		vr.newRow("ACTION_DEALERSHIP", "", false,
			"DEALERSHIP_ID="+dealer_id,
			"ACTION_ID="+action_id
		);
		vr.updateRow("ACTION", action_id,
			"BRAND_ID=(SELECT BRAND_ID FROM DEALERSHIP WHERE DEALERSHIP_ID="+dealer_id+")",
			"STANDARD_TOPIC_ID="+topic_id,
			"START_DATE=toDate:"+row.getString("DateCreated"),
			"DUE_DATE=toDate:"+row.getString("TargetCompletionDate"),
			"CREATED_BY="+row.getString("Author"),
			"CREATED_DATE=toDate:"+row.getString("DateCreated"),
			"MODIFIED_DATE=toDate:"+row.getString("DateUpdated")
		);
	}
	*/
	
	public void tbl_Common_Agenda(SqlRowSet row) {//NOPMD
		tbl_Common_AgendaLookup.put(row.getString("CAID")+"_catype",    row.getString("CAType"));
		tbl_Common_AgendaLookup.put(row.getString("CAID")+"_effective", row.getString("date_effective"));
		tbl_Common_AgendaLookup.put(row.getString("CAID")+"_expiry",    row.getString("date_expiry"));
		tbl_Common_AgendaLookup.put(row.getString("CAID")+"_usrGrp",    row.getString("UserGroup"));
	}
	
	public void tbl_Common_Agenda_Tasks(SqlRowSet row) {//NOPMD
		
		//skip survey common agendas
		String caType = tbl_Common_AgendaLookup.get(row.getString("CAID")+"_catype");
		if("SURVEY".equals(caType) || "RSESURVEY".equals(caType)) {
			return;
		}
		
		String topic_ref_id = vr.newRow("STANDARD_TOPIC_REF", row.getString("item_title"),
			"TITLE="+(row.getString("item_title") == null ? row.getString("item_description") : row.getString("item_title")),
			"DESCRIPTION="+(row.getString("item_description") == null ? row.getString("item_title") : row.getString("item_description"))
		);
		
		String topic_id = vr.newRow("STANDARD_TOPIC", row.getString("item_title"),
			"EFFECTIVE_START_DATE=toDate:"+tbl_Common_AgendaLookup.get(row.getString("CAID")+"_effective"),
			"EFFECTIVE_END_DATE=toDate:"+tbl_Common_AgendaLookup.get(row.getString("CAID")+"_expiry"),
			"STANDARD_TOPIC_REF_ID="+topic_ref_id,
			"ORGANISATION_ID="+ORG_ID_VWGUK,
			"STANDARD_TOPIC_REF_TYPE="+StandardTopicRefType.STANDARD_TOPIC.getId()
		);
		
		vr.newRow("STANDARD_TOPIC_BRAND", "", false,
			"STANDARD_TOPIC_ID="+topic_id,
			"BRAND_ID="+brandId
		);

		vr.newRow("STANDARD_TOPIC_BUSINESS_AREA", "", false,
			"STANDRAD_TOPIC_ID="+topic_id,
			"BUSINESS_AREA_ID="+toBusArea(null, tbl_Common_AgendaLookup.get(row.getString("CAID")+"_usrGrp"))
		);
	}
	
	public void tbl_MeetingType(SqlRowSet row) {//NOPMD
		vr.newRow("MEETING_TYPE", row.getString("MeetingCode"),
			"NAME="+row.getString("Description"),
			"IS_DISABLED=0"
		);
	}
	
	public void tbl_MeetingMaster(SqlRowSet row) {//NOPMD

		String scope = row.getString("MeetingScope");
		if("A".equals(scope)) {
			return;		//skip meetings with applicant
		}
		if(row.getString("Attendance") == null) {
			return;		//if no attendees listed then skip row
		}
		if(row.getString("RetailerNumber") == null) {
			return;		//if meeting not related to a dealer then skip this row
		}

		
		//get all dealers associated to this meeting
		List<String> dealers = new ArrayList<String>();
		if("R".equals(scope)) {	//retailer
			String dealer_id = lookupDealer(row.getString("MeetingKey"));
			if(dealer_id != null) {
				dealers.add(dealer_id);
			}
		} else if("U".equals(scope) || "G".equals(scope)) {	//U=franchise group || G=franchise group (in a given area)
			String UPCKey = row.getString("RetailerNumber");
			Set<String> dealer_ids = franchiseGroupDealers.get(UPCKey);
			if(dealer_ids == null) {
				dealer_ids = franchiseGroupDealers.get(row.getString("MeetingKey"));
			}
			if(dealer_ids != null) {
				for(String dealer_id : dealer_ids) {
					dealers.add(dealer_id);
				}
			}
		}
		if(dealers.isEmpty()) {
			return;	//skip row if all dealers/franchise groups have been terminated
		}
		
		//boolean confidential = isTrue(row, "Restricted");
		boolean agendaConfidential = false;		//agenda is confidential by default
		boolean visitReportConfidential = true;	//visit report is confidential by default
		
		String bus_Area_id = toBusArea(null, row.getString("MeetingCode")).toString();
		String meetingNumber = row.getString("MeetingNumber");
		
		String meetingType = "";
		try {
			meetingType = vr.lookup("MEETING_TYPE", row.getString("MeetingCode"));
		} catch(NoResultException err) {
			try {
				meetingType = vr.lookup("MEETING_TYPE", "OPS");
			} catch(NoResultException e) {
				meetingType = vr.lookup("MEETING_TYPE", "ADH");
			}
		}
		
		String location = null;
		if(row.getString("Meeting Location") == null || row.getString("Meeting Location").length() == 0) {
			if(row.getString("Meeting Venue") == null || row.getString("Meeting Venue").length() == 0) {
				location = "-";
			} else {
				location = row.getString("Meeting Venue");
			}
		} else {
			if(row.getString("Meeting Venue") == null || row.getString("Meeting Venue").length() == 0) {
				location = row.getString("Meeting Location");
			} else {
				location = row.getString("Meeting Location") + " - " + row.getString("Meeting Venue");
			}
		}
		
		String title = (row.getString("Meeting Title") == null || row.getString("Meeting Title").trim().length() == 0) ? "-" : row.getString("Meeting Title");
		
		String agenda_id = vr.newRow("AGENDA", meetingNumber,
			"DATE_OF_MEETING=toDate:"+row.getString("Meeting date"),
			"MEETING_ORGANISER_USER_ID="+row.getString("Owner"),
			"CONFIDENTIAL="+(agendaConfidential ? "1" : "0"),
			"MEETING_TYPE_ID="+meetingType,
			"MEETING_START_TIME=toTime:"+row.getString("Start Time"),
			"MEETING_END_TIME=toTime:"+row.getString("Finish Time"),
			"LOCATION_OF_MEETING="+location,
			"BUSINESS_AREA_ID="+bus_Area_id,
			"TITLE_OF_MEETING="+title
		);
		
		//add meeting invitees
		String[] invitees = row.getString("Attendance").split("\n");
		for(String invitee : invitees) {
			String inviteeUser = annoymousUserId;
			if(nameToUserId.containsKey(invitee.toLowerCase())) {
				inviteeUser = nameToUserId.get(invitee.toLowerCase());
			}
			
			vr.newRow("AGENDA_INVITEE", "",
				"INVITEE_USER_ID="+inviteeUser,
				"INVITEE_NAME="+invitee,
				"AGENDA_ID="+agenda_id
			);
		}
		
		//associate meeting with dealers
		agendaDealers.put(meetingNumber, new ArrayList<String>());
		for(String dealer_id : dealers) {
			vr.newRow("AGENDA_DEALER", "", false,
				"AGENDA_ID="+agenda_id,
				"DEALERSHIP_ID="+dealer_id
			);
			agendaDealers.get(meetingNumber).add(dealer_id);
		}
		
		//if meeting has occurred then create visit report
		if("Minutes".equals(row.getString("status"))) {
			
			String visit_report_id = vr.newRow("VISIT_REPORT", meetingNumber,
				"AGENDA_ID="+agenda_id,
				"SUMMARY_OF_DISCUSSION=-",//+getQuestionaireSummaryFor(row.getString("MeetingNumber")),
				"CONFIDENTIAL="+(visitReportConfidential ? "1" : "0"),
				"LOCATION_OF_MEETING="+location,
				"TITLE_OF_MEETING="+title,
				"MEETING_START_TIME=toTime:"+row.getString("Start Time"),
				"MEETING_END_TIME=toTime:"+row.getString("Finish Time"),
				"MEETING_ORGANISER_USER_ID="+row.getString("Owner")
			);
			
			//add meeting attendees
			for(String attendee : invitees) {
				String attendeeUser = annoymousUserId;
				if(nameToUserId.containsKey(attendee.toLowerCase())) {
					attendeeUser = nameToUserId.get(attendee.toLowerCase());
				}

				vr.newRow("VISIT_REPORT_ATTENDEE", "",
					"ATTENDEE_USER_ID="+attendeeUser,
					"ATTENDEE_NAME="+attendee,
					"VISIT_REPORT_ID="+visit_report_id
				);
			}
		}
	}
	
	public void tbl_MeetingDetail(SqlRowSet row) {//NOPMD
		
		agendaItemToMeetingNumber.put(row.getString("AgendaItemID"), row.getString("MeetingNumber"));
		
		String agenda_id = null;
		try {
			agenda_id = vr.lookup("AGENDA", "MeetingNumber");
		} catch(NoResultException err) {
			return;		//skip this entry if cannot link back to meeting
		}
		
		String topic_id = null;
		try {
			topic_id = vr.lookup("STANDARD_TOPIC", row.getString("Subject"));
		} catch(NoResultException err) { topic_id = null; }
		
		if(topic_id == null) {
			
			String agenda_topic_id = vr.newRow("AGENDA_ADHOC_TOPIC", "",
				"ADHOC_TOPIC_TITLE="+row.getString("Subject"),
				"ADHOC_TOPIC_DESCRIPTION="+row.getString("Agenda Detail")+(row.getString("Gen_Agenda Detail") == null ? "" : ". "+row.getString("Gen_Agenda Detail")),
				"AGENDA_ID="+agenda_id
			);
			
			agendaItemToTopic.put(row.getString("AgendaItemID"), "ADHOC_TOPIC_ID="+agenda_topic_id);
			
		} else {
			
			String agenda_topic_id = vr.newRow("AGENDA_STANDARD_TOPIC", "", false,
				"STANDARD_TOPIC_ID="+topic_id,
				"AGENDA_ID="+vr.lookup("AGENDA", "MeetingNumber")
			);

			agendaItemToTopic.put(row.getString("AgendaItemID"), "STANDARD_TOPIC_ID="+agenda_topic_id);
		}
	}

	public void tbl_MeetingOutcome(SqlRowSet row) {//NOPMD
		
		if(row.getString("Description") == null || row.getString("Description").trim().length() == 0) {
			return;		//skip rows with no description
		}
		
		String meetingNumber = agendaItemToMeetingNumber.get(row.getString("AgendaItemID"));
		String visit_report_id = null;
		try {
			visit_report_id = vr.lookup("VISIT_REPORT", meetingNumber);
		} catch(NoResultException err) {
			return;		//skip this row if visit report cannot be found
		}
		
		String topicMapping = agendaItemToTopic.get(row.getString("AgendaItemID"));
		
		String dispSeq = "DISPLAY_SEQUENCE="+row.getString("Sequence");
		String desc = row.getString("Description");	// + (row.getString("Gen_Description") == null ? "" : ". "+row.getString("Gen_Description"));
		
		//split description into topic discussion and actions
		desc = desc.replace('\r', ' ').replace('\n', '\t');
		List<String> actionItems = new ArrayList<String>();
		if(CREATE_ACTIONS_FOR_MEETING_OUTCOMES && ((isTrue(row, "action_vw") && row.getString("person_actioned_vw") != null) || (isTrue(row, "action_retailer") && row.getString("person_actioned_retailer") != null))) {
			
			String[] items = desc.split("\t", 0);
			for(String item : items) {
				
				desc = null;
				if(item.startsWith("AP") && item.charAt(2) >= '0' && item.charAt(2) <= '9') {
					String actionItem = item.substring(3).trim();
					if(actionItem.charAt(0) == ':') {
						actionItem = actionItem.substring(1).trim();
					}
					if(actionItem.length() > 0) {
						actionItems.add(actionItem);
					}
				} else {
					if(item.trim().length() > 0) {
						desc += (desc == null) ? item : "\t"+item;
					}
				}
			}
		}

		//add topics to meeting
		String stdTopicId = "";
		if(topicMapping != null) {
			if(topicMapping.contains("ADHOC")) {
				
				vr.newRow("VISIT_REPORT_ADHOC_TOPIC", "",
					"VISIT_REPORT_ID="+visit_report_id,
					"TOPIC_DESCRIPTION="+desc,
					dispSeq,
					"SUMMARY_OF_TOPIC_DISCUSSION="+desc
				);
				
			} else {
				
				stdTopicId = vr.newRow("VISIT_REPORT_STANDARD_TOPIC", "",
					"VISIT_REPORT_ID="+visit_report_id,
					topicMapping,
					dispSeq,
					"SUMMARY_OF_TOPIC_DISCUSSION="+desc
				);
			}
		}
		
		//boolean confidential = ! isTrue(row, "show_on_retailer_reports");
		boolean confidential = true;	//everything is confidential by default
		
		//action data is not well structured so may just leave as topics and topic discussions
		if(CREATE_ACTIONS_FOR_MEETING_OUTCOMES) {
			for(String actionItem : actionItems) {
			
				if(isTrue(row, "action_vw") && row.getString("person_actioned_vw") != null) {
					
					String owner = nameToUserId.get(row.getString("person_actioned_vw").toLowerCase());
					Integer busArea = this.toBusArea(owner, null);
					
					String action_id_vwg = vr.newRow("ACTION", "",
						"BRAND_ID="+brandId,
						"ORGANISATION_ID="+ORG_ID_VWGUK,
						"STANDARD_TOPIC_ID="+stdTopicId,
						"DESCRIPTION="+actionItem,
						"DUE_DATE=toDate:"+row.getString("target_date_vw"),
						"STATUS="+ (isTrue(row, "completed_vw") ? ACTION_STATUS_CLOSED : ACTION_STATUS_OPEN),
						"CONFIDENTIAL_FLAG="+ (confidential ? "1" : "0"),
						"START_DATE=toDate:"+row.getString("Time created")
					);
					if(owner != null) {
						vr.newRow("ACTION_OWNER", "", false,
							"ACTION_ID="+action_id_vwg,
							"OWNER_USER_ID="+owner
						);
					}
					if(busArea != null) {
						vr.newRow("ACTION_BUSINESS_AREA", "", false,
							"ACTION_ID="+action_id_vwg,
							"BUSINESS_AREA_ID="+busArea
						);
					}
					for(String dealer_id : agendaDealers.get(meetingNumber)) {
						vr.newRow("ACTION_DEALERSHIP", "", false,
							"ACTION_ID="+action_id_vwg,
							"DEALERSHIP_ID="+dealer_id
						);
					}
				}
				if(isTrue(row, "action_retailer") && row.getString("person_actioned_retailer") != null) {
	
					String owner = nameToUserId.get(row.getString("person_actioned_retailer").toLowerCase());
					Integer busArea = this.toBusArea(owner, null);
	
					String action_id_vwg = vr.newRow("ACTION", "",
						"BRAND_ID="+brandId,
						"ORGANISATION_ID="+ORG_ID_VWGUK,
						"STANDARD_TOPIC_ID="+stdTopicId,
						"DESCRIPTION="+actionItem,
						"DUE_DATE=toDate:"+row.getString("target_date_retailer"),
						"STATUS="+ (isTrue(row, "completed_retailer") ? ACTION_STATUS_CLOSED : ACTION_STATUS_OPEN),
						"CONFIDENTIAL_FLAG="+ (confidential ? "1" : "0"),
						"START_DATE=toDate:"+row.getString("Time created")
					);
					if(owner != null) {
						vr.newRow("ACTION_OWNER", "", false,
							"ACTION_ID="+action_id_vwg,
							"OWNER_USER_ID="+owner
						);
					}
					if(busArea != null) {
						vr.newRow("ACTION_BUSINESS_AREA", "", false,
							"ACTION_ID="+action_id_vwg,
							"BUSINESS_AREA_ID="+busArea
						);
					}
					for(String dealer_id : agendaDealers.get(meetingNumber)) {
						vr.newRow("ACTION_DEALERSHIP", "", false,
							"ACTION_ID="+action_id_vwg,
							"DEALERSHIP_ID="+dealer_id
						);
					}
				}
			}
		}
	}
	
	public void tbl_Recommendation(SqlRowSet row) {//NOPMD
		recomendationLookup.put(row.getString("recommendation_id"), row.getString("description"));
	}
	
	public void tbl_Recommendation_Action(SqlRowSet row) {//NOPMD
		recomendationLookup.put(
			row.getString("question_id")+"_"+row.getString("answer_number"),
			recomendationLookup.get(row.getString("recommendation_id"))
		);
	}
	
	public void tbl_QuestionnaireAnswer(SqlRowSet row) {//NOPMD
		
		String visit_report_id = null;
		try {
			visit_report_id = vr.lookup("VISIT_REPORT", row.getString("MeetingNumber"));
		} catch(NoResultException err) {
			return;		//skip row if cannot find visit report
		}
		
		String recomendation = recomendationLookup.get(row.getString("question_id")+"_"+row.getString("answer_number"));
		
		String summary =   "Q: "+row.getString("question_text")
				        +"  A: "+row.getString("answer_given")
				        +"  (Recomend: "+recomendation+")";

		summary = summary.replace("'", "").replace("\r", "").replace("\n", "  ");
		
		vr.updateRow("VISIT_REPORT", "ID="+visit_report_id,
			"SUMMARY_OF_DISCUSSION=(SELECT SUMMARY_OF_DISCUSSION FROM VISIT_REPORT WHERE ID="+visit_report_id+")||char(13)||'"+summary+"'"
		);
	}
	
	public void tbl_ProposedActivities(SqlRowSet row) {//NOPMD

		String visit_report_id = null;
		try {
			visit_report_id = vr.lookup("VISIT_REPORT", row.getString("MeetingNumber"));
		} catch(NoResultException err) {
			return;		//skip row if cannot find visit report
		}

		String recomendation = recomendationLookup.get(row.getString("recommendation_id"));
		if(recomendation == null) {
			return;		//skip if recommendation cannot be found
		}
		recomendation = recomendation.replace("'", "").replace("\r", "").replace("\n", "  ");
		
		vr.updateRow("VISIT_REPORT", "ID="+visit_report_id,
			"SUMMARY_OF_DISCUSSION=(SELECT SUMMARY_OF_DISCUSSION FROM VISIT_REPORT WHERE ID="+visit_report_id+")||char(13)||'"+recomendation+"'"
		);
	}

	
	
	
	private String fixDealerNumber(String number) {
		if(number == null) {
			return null;
		} else if(number.length() <= 5) {
			return "00000".substring(number.length()) + number;
		} else {
			return number;
		}
	}
	private String lookupDealer(String retailerNumber) {
		retailerNumber = fixDealerNumber(retailerNumber);
		int counter = 0;
		while(dealerRemapping.containsKey(retailerNumber)) {
			retailerNumber = dealerRemapping.get(retailerNumber);
			if(counter++ > 10) {
				//throw new RuntimeException("Circular referene for retailer "+retailerNumber+" in tbl_Event audit table");
				return null;
			}
		}
		try {
			return vr.lookup("DEALERSHIP", retailerNumber);
		} catch(NoResultException err) {
			return null;	//not found is normally due to retailer being terminated
		}
	}
	
	private boolean isTrue(SqlRowSet row, String colName) {
		String val = row.getString(colName);
		if("0".equals(val)) {
			return false;
		} else if("false".equals(val)) {
			return false;
		} else if("No".equals(val)) {//NOPMD
			return false;
		} else {
			return true;
		}
	}
	
	private Integer toBusArea(String userId, String busAreaStr) {
		if(userId == null) {
			if(busAreaStr == null || busAreaStr.trim().isEmpty()) {
				return BUS_AREA_CODES.get("Operations");
			} else if(BUS_AREA_CODES.containsKey(busAreaStr)) {
				return BUS_AREA_CODES.get(busAreaStr);
			} else {
				return null;
			}
		} else {
			if(USER_BUS_AREA.containsKey(userId)) {
				if(BUS_AREA_CODES.containsKey(USER_BUS_AREA.get(userId))) {
					return BUS_AREA_CODES.get(USER_BUS_AREA.get(userId));
				} else {
					return null;
				}
			} else {
				return BUS_AREA_CODES.get("Operations");
			}
		}
	}
}
