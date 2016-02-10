package com.vw.visitreporting.entity.referencedata.enums;

import com.vw.visitreporting.entity.referencedata.Dealership;
import com.vw.visitreporting.entity.referencedata.User;
import com.vw.visitreporting.entity.referencedata.Organisation;
import com.vw.visitreporting.entity.referencedata.Positions;
import com.vw.visitreporting.entity.referencedata.UserProfile;


/**
 * Refers to a function within this system that is access controlled.
 * Each entry is commented with the requirement that describes the
 * authorisation rules for that function. 
 */
public enum Function implements EnumEntity {
	
    //Functions for organisation reference data
    //a user can see any organisation related data but can edit only if
    //edit access has been provided(provided other rules are satisfied)
    MAINTAIN_ORGANISATION          (1, "Maintain Organisation", Organisation.class),	//6.1.6.1.1,2
    EDIT_ORGANISATION_FUNCTIONS    (2, "Maintain Organisation Functions", Brand.class),	//6.1.6.1.1,2
    EDIT_ORGANISATION_SHARING_RULES(3, "Maintain Organisation Sharing Rules", Brand.class),	//6.1.6.1.1,2

    MAINTAIN_POSITIONS(107,"Maintain Positions",Positions.class),
    
    CREATE_USER_PROFILE   (101,  "Create user Profile"),  
    CREATE_GEOGRAPHICAL_STRUCTURE_CATEGORY(102, "Create geographical strucutre category"),
    CREATE_BUSINESS_AREA(103, "Create Business Area"),
    

    //global functions
    ACCESS_MESSAGE_BOARD   (91,  "Access Message Board"),   //6.1.7.5.1
    SEARCH_DEALERS         (92,  "Search for Dealerships"), //6.1.1.1, 6.1.1.2
    ACCESS_DEALER_INFO     (93,  "Access Dealer Information"),    //6.1.2.1.1
    SEARCH_ACTIONS         (94,  "Search for Actions"),     //6.1.3.1.1
    CREATE_AGENDA          (95,  "Create Agenda"),    //6.1.4.1.1
    CREATE_VISIT_REPORT    (96,  "Create Visit Report"),    //6.1.4.2.1.1
    CREATE_ACTION          (97,  "Create Action"),    //6.1.3.3.1
    CREATE_DEALER          (98,  "Create Dealer"),    //6.1.5.1.1-2
    CREATE_FRANCHISE_GROUP (99,  "Create Franchise Group"), //6.1.5.6.1
    CREATE_USER            (100, "Create User"),      //6.1.7.1.1,2
    ACCESS_REPORTING       (104, "Access Reporting"), //5.1.1.1.1
    CREATE_REPORT          (105, "Create Report"), //5.1.1.4.1
    EDIT_MEETING_TYPE      (106, "Edit Meeting Types"),		//6.1.6.7.1,2
    
    //functions related to an entity object
//    LIST_DEALER                    (20, "View Dealer In a List", Dealership.class),   //6.1.1.3
    MAINTAIN_DEALERSHIP            (20, "Maintain Dealership", Dealership.class),     //TODO this needs to follow the pattern for function naming
    VIEW_DEALER                    (21, "View Dealer Details", Dealership.class),      //6.1.2.1.1
    EDIT_DEALER                    (22, "Edit Dealer Details", Dealership.class),      //6.1.5.2.1,2
//    EDIT_DEALER_GEOG               (23, "Edit Dealer Region/Area", Dealership.class),      //6.1.5.3.1,2
//    EDIT_DEALER_K_P_IS             (24, "Edit Dealer KPIs, Including Upload File", Dealership.class),      //6.1.5.4.1,2 + 6.1.5.5.1-2
//    CREATE_AGENDA_FOR_DEALER       (25, "Create Agenda For Dealer", Dealership.class),      //6.1.4.1.2
//    CREATE_VISIT_REPORT_FOR_DEALER (26, "Create Visit Report For Dealer", Dealership.class),      //6.1.4.2.1.2
//    CREATE_ACTION_ON_DEALER        (27, "Create Action On Dealer", Dealership.class),      //6.1.3.3.2
    
    VIEW_FRANCHISE_GROUP           (28, "View Franchise Group"),
    EDIT_FRANCHISE_GROUP           (29, "Edit Franchise Group"),    //6.1.5.6.1,2
    
    //EDIT_ORGANISATION              (32, "Edit Organisation", Organisation.class),      //6.1.6.1.1,2
    EDIT_USER_PROFILE              (33, "Edit User Profile", UserProfile.class),      //6.1.6.2.1,2
    EDIT_BUS_AREA                  (34, "Edit Business Area"),      //6.1.6.3.1,2
    EDIT_POSITIONS                 (35, "Edit Position", Positions.class),      //6.1.6.4.1,2
    VIEW_POSITIONS				   (77, "View Position", Positions.class),  
    EDIT_K_P_I                     (36, "Edit KPI"),    //6.1.6.5.1,2
    VIEW_TOPIC                     (37, "View Topic"), //6.1.2.7.1
    EDIT_TOPIC                     (38, "Edit Topic"),  //6.1.6.6.1,2
    EDIT_DRAFT_DEL_RULE            (39, "Edit Draft Document Deletion Rule"),   //6.1.6.8.1,2
    EDIT_DOC_TMPL                  (40, "Edit Document Template"),  //6.1.6.10.1.1,2
    
    EDIT_USER                 (41, "Edit User", User.class),
//    EDIT_USER_NAME                 (41, "Edit User Full Name", User.class),     //6.1.7.3.2.1,3
//    EDIT_USER_EMAIL                (42, "Edit User Email Adrdress", User.class),      //6.1.7.3.2.1,3
//    EDIT_USER_TEL                  (43, "Edit User Telephone Number", User.class),      //6.1.7.3.1.1,3
//    EDIT_USER_POS                  (44, "Edit User Position", User.class),      //6.1.7.3.3.1,3
//    EDIT_USERS_PROFILE             (45, "Edit User User-Profile", User.class),  //6.1.7.3.4.1,3
//    EDIT_USER_GEOG                 (46, "Edit User Region/Area", User.class),   //6.1.7.3.5.1,3
//    DISABLE_USER                   (47, "Disable User", User.class),      //6.1.7.2.1,2
    
    VIEW_GEOG                      (48, "View Geographical Structure"),   //6.1.6.9.1.1,2
    EDIT_GEOG                      (49, "Edit Geographical Structure"),   //6.1.6.9.2.1,2

    VIEW_MESSAGE_BOARD_RULES       (50, "View Message Board Rules"),      //6.1.6.11.4
    EDIT_MESSAGE_BOARD_RULE        (51, "Edit Message Board Rule"), //6.1.6.11.1,2

    VIEW_AGENDA                    (52, "View Agenda"), //6.1.3.2.1, 6.1.2.4.1, p11
    EDIT_AGENDA                    (53, "Edit Agenda"), //6.1.4.5.1, 6.1.4.5.2, 6.1.4.5.4, p11
//    EDIT_AGENDA_DEALERS            (54, "Edit Agenda Dealer List"), //6.1.4.3.1
//    EDIT_AGENDA_BUS_AREAS          (55, "Edit Agenda Business Area List"),      //6.1.4.3.2
//    EDIT_AGENDA_ADD_DOC            (56, "Publish Document For Agenda"),   //6.1.4.4.3
    
    VIEW_VISIT_REPORT              (57, "View Visit Report"), //6.1.2.4.1, p11
    EDIT_VISIT_REPORT              (58, "Edit Visit Report"), //6.1.4.5.1, 6.1.4.5.2, 6.1.4.5.4, 6.1.4.2.2.3-6,13, p11
//    EDIT_VISIT_REPORT_DEALERS      (59, "Edit Visit Report Dealer List"), //6.1.4.3.1
//    EDIT_VISIT_REPORT_BUS_AREAS    (60, "Edit Visit Report Business Area List"),      //6.1.4.3.2
//    EDIT_VISIT_REPORT_ADD_DOC      (61, "Publish Document For Visit Report"),   //6.1.4.4.3
    
    VIEW_ACTION                    (62, "View Action"), //6.1.2.6.1, 6.1.7.5.5, p11
    EDIT_ACTION                    (63, "Edit Action"), //6.1.3.4.1.1, 6.1.3.4, p11
//    EDIT_ACTION_PROGRESS           (64, "Edit Action Progress"),    //6.1.3.4.1.5
//    EDIT_ACTION_DUE_DATE           (65, "Edit Action Due Date"),    //6.1.3.4.2.1, 6.1.3.4.2.2, 6.1.3.4.2.4
//    EDIT_ACTION_OWNER              (66, "Edit Action Owner"), //6.1.3.4.3.1, 6.1.3.4.3.2, 6.1.3.4.3.3
//    EDIT_ACTION_VISIBILITY         (67, "Edit Action Visibility"),  //6.1.3.4.4.1, 6.1.3.4.4.2, 6.1.3.4.4.3
//    REQUEST_ACTION_CLOSURE         (68, "Request Action Closure"),  //6.1.3.4.5.1, 6.1.3.4.5.2, 6.1.3.4.5.3, 6.1.3.4.5.4
//    PROCESS_CLOSE_REQUEST          (69, "Process Action Close Request"),  //6.1.3.4.6.1, 6.1.3.4.6.2, 6.1.3.4.6.3
//    CLOSE_ACTION                   (70, "Close Action"),      //6.1.3.4.7.1, 6.1.3.4.7.2, 6.1.3.4.7.3, 6.1.3.4.7.4
//    RE_OPEN_ACTION                 (71, "ReOpen Action"),     //6.1.3.4.7.1, 6.1.3.4.7.2, 6.1.3.4.7.3, 6.1.3.4.7.4      
    
//    VIEW_REPORT                    (72, "View Report"), //5.1.1.1.2
//    REPORT_ON_DEALER               (73, "Report On Dealer", Dealership.class),  //5.1.1.2.1
//    REPORT_ON_AGENDA               (74, "Report On Agenda"),  //5.1.1.2.3
//    REPORT_ON_VISIT_REPORT         (75, "Report On Visit Report"),  //5.1.1.2.3
//    REPORT_ON_ACTION               (76, "Report On Action")  //5.1.1.2.3
    ;

    
	private final Integer id;
	private final String name;
	private final Class<?> entityClass;


	/**
	 * Constructor. Initialises a global system function that is not related to an entity instance.
	 */
	private Function(int id, String name) {
		this(id, name, null);
	}

	/**
	 * Constructor. Sets all properties to their final values.
	 */
	private Function(int id, String name, Class<?> entityClass) {
		this.id = id;
		this.name = name;
		this.entityClass = entityClass;
	}

	/**
	 * Gets the id that will be stored in the database to reference this instance.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Gets the name/description of this system function.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the class of the entity type this operation is performed on,
	 * or null if the operation is not performed on an existing entity instance.
	 */
	public Class<?> getEntityClass() {
		return entityClass;
	}
}

