package com.vw.visitreporting.entity.referencedata;	//NOPMD

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.BrandRelated;
import com.vw.visitreporting.entity.ConfidentialityRelated;
import com.vw.visitreporting.entity.DealerRelated;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.DocumentStatus;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * An agenda/plan for a meeting.
 */
@Entity
@Table(schema = "PUBLIC", name = "AGENDA")
public class Agenda implements BaseVREntity, Serializable, BrandRelated, DealerRelated, ConfidentialityRelated {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumns({ @JoinColumn(name = "VISIT_REPORT_ID", referencedColumnName = "ID", nullable = true) })
	private VisitReport visitReport;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_MEETING", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	private Date dateOfMeeting;

	@Column(name = "LOCATION_OF_MEETING", length = 200, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	@Size(min=2, max=200)
	private String locationOfMeeting;

	@Column(name = "TITLE_OF_MEETING", length = 200, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	@Size(min=2, max=200)
	private String titleOfMeeting;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MEETING_START_TIME")
	@Basic(fetch = FetchType.EAGER)
	private Date meetingStartTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MEETING_END_TIME")
	@Basic(fetch = FetchType.EAGER)
	private Date meetingEndTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "MEETING_TYPE_ID", referencedColumnName = "ID", nullable = true) })
	private MeetingType meetingType;
	
	@Column(name = "MEETING_ORGANISER_USER_ID", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	@Size(min=2, max=50)
	private String meetingOrganiserUserId;

	@Column(name = "CONFIDENTIAL")
	@Basic(fetch = FetchType.EAGER)
	private Byte confidential;

	@Column(name = "STATUS_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Integer statusId;

	@Column(name = "CURRENT_VERSION_NUMBER")
	@Basic(fetch = FetchType.EAGER)
	private Float currentVersionNumber;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="AGENDA_ACTION",
		joinColumns=@JoinColumn(name="AGENDA_ID", referencedColumnName="ID"),
		inverseJoinColumns=@JoinColumn(name="ACTION_ID", referencedColumnName="ID")
	)
	private Set<Action> actions;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="AGENDA_BUSINESS_AREA",
		joinColumns=@JoinColumn(name="AGENDA_ID", referencedColumnName="ID"),
		inverseJoinColumns=@JoinColumn(name="BUSINESS_AREA_ID", referencedColumnName="ID")
	)
	private Set<BusinessArea> businessAreas;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name="AGENDA_BRAND", joinColumns=@JoinColumn(name="AGENDA_ID"))
	@Column(name="BRAND_ID")
	private Set<Integer> brands;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="AGENDA_DEALER",
		joinColumns=@JoinColumn(name="AGENDA_ID", referencedColumnName="ID"),
		inverseJoinColumns=@JoinColumn(name="DEALERSHIP_ID", referencedColumnName="ID")
	)
	private Set<Dealership> dealerships;

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval=true)
	@JoinColumn(name="AGENDA_ID")
	private Set<AgendaInvitee> invitees;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="AGENDA_STANDARD_TOPIC",
		joinColumns=@JoinColumn(name="AGENDA_ID", referencedColumnName="ID"),
		inverseJoinColumns=@JoinColumn(name="STANDARD_TOPIC_ID", referencedColumnName="ID")
	)
	private Set<StandardTopic> standardTopic;

	@Column(name = "CREATED_BY", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	@Basic(fetch = FetchType.EAGER)
	private Date modifiedDate;

	@Column(name = "MODIFIED_BY", length = 50)
	@Basic(fetch = FetchType.EAGER)
	private String modifiedBy;


	/**
	 * Constructor. Creates an empty instance of this entity.
	 */
	public Agenda() {
	}
	

	/**
	 * Sets the database primary key for this entity.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the database primary key for this entity.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Gets the minutes for this meeting if it has already happened.
	 */
	public VisitReport getVisitReport() {
		return visitReport;
	}

	/**
	 * Sets the minutes for this meeting if it has already happened.
	 */
	public void setVisitReport(VisitReport visitReport) {
		this.visitReport = visitReport;
	}

	/**
	 * Gets the date that the meeting will take place
	 */
	public Date getDateOfMeeting() {
		return dateOfMeeting;
	}

	/**
	 * Sets the date that the meeting will take place
	 */
	public void setDateOfMeeting(Date dateOfMeeting) {
		this.dateOfMeeting = dateOfMeeting;
	}

	/**
	 * Gets the location when the meeting will take place
	 */
	public String getLocationOfMeeting() {
		return locationOfMeeting;
	}

	/**
	 * Sets the location when the meeting will take place
	 */
	public void setLocationOfMeeting(String locationOfMeeting) {
		this.locationOfMeeting = locationOfMeeting;
	}

	/**
	 * Gets a short description of the subject of the meeting.
	 */
	public String getTitleOfMeeting() {
		return titleOfMeeting;
	}

	/**
	 * Sets a short description of the subject of the meeting.
	 */
	public void setTitleOfMeeting(String titleOfMeeting) {
		this.titleOfMeeting = titleOfMeeting;
	}

	/**
	 * Gets the time at which the meeting will start.
	 */
	public Date getMeetingStartTime() {
		return meetingStartTime;
	}

	/**
	 * Sets the time at which the meeting will start.
	 */
	public void setMeetingStartTime(Date meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}

	/**
	 * Gets the time at which the meeting will finish.
	 */
	public Date getMeetingEndTime() {
		return meetingEndTime;
	}

	/**
	 * Sets the time at which the meeting will finish.
	 */
	public void setMeetingEndTime(Date meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
	}

	/**
	 * Gets the type of this meeting (meeting types are user-configurable)
	 */
	public MeetingType getMeetingType() {
		return meetingType;
	}

	/**
	 * Sets the type of this meeting (meeting types are user-configurable)
	 */
	public void setMeetingType(MeetingType meetingType) {
		this.meetingType = meetingType;
	}

	/**
	 * Gets the username (primary key into the USER table) of the user that arranged this meeting.
	 */
	public String getMeetingOrganiserUserId() {
		return meetingOrganiserUserId;
	}

	/**
	 * Sets the username (primary key into the USER table) of the user that arranged this meeting.
	 */
	public void setMeetingOrganiserUserId(String meetingOrganiserUserId) {
		this.meetingOrganiserUserId = meetingOrganiserUserId;
	}

	/**
	 * Used for auditing purposed to maintain a history of this meeting agenda.
	 * Gets the current version of this entity.
	 */
	public Float getCurrentVersionNumber() {
		return currentVersionNumber;
	}

	/**
	 * Used for auditing purposed to maintain a history of this meeting agenda.
	 * Sets the current version of this entity.
	 */
	public void setCurrentVersionNumber(Float currentVersionNumber) {
		this.currentVersionNumber = currentVersionNumber;
	}

	/**
	 * Gets whether or not this this agenda is confidential (only viewable by owners and creator).
	 */
	public Byte getConfidential() {
		return confidential;
	}
	
	/**
	 * Gets whether or not this this agenda is confidential (only viewable by owners and creator).
	 */
	public boolean isConfidential() {
		return (confidential != null && confidential != 0);
	}

	/**
	 * Gets the status of this agenda; either DRAFT or PUBLISHED.
	 * Note that the requirements docoument refers to this status as the status
	 * of the 'agenda document'. In this system, the agenda document is an entity
	 * in the database, rather than a file.
	 */
	public void setStatus(DocumentStatus status) {
		this.statusId = status.getId();
	}

	/**
	 * Sets the status of this agenda; either DRAFT or PUBLISHED.
	 * Note that the requirements docoument refers to this status as the status
	 * of the 'agenda document'. In this system, the agenda document is an entity
	 * in the database, rather than a file.
	 */
	public DocumentStatus getStatus() {
		return EnumUtil.getEnumObject(this.statusId, DocumentStatus.class);
	}

	/**
	 * Sets whether or not this this agenda is confidential (only viewable by owners and creator).
	 */
	public void setConfidential(Byte confidential) {
		this.confidential = confidential;
	}

	/**
	 * Sets whether or not this this agenda is confidential (only viewable by owners and creator).
	 */
	public void setConfidential(boolean confidential) {
		this.confidential = (byte)(confidential ? 1 : 0);
	}

	/**
	 * Gets the actions brought forward from a previous meeting to be discussed in this meeting.
	 */
	public Set<Action> getActions() {
		return actions;
	}

	/**
	 * Sets the actions brought forward from a previous meeting to be discussed in this meeting.
	 */
	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}

	/**
	 * Gets the business areas that this meetigng will relate to.
	 */
	public Set<BusinessArea> getBusinessAreas() {
		return businessAreas;
	}

	/**
	 * Sets the business areas that this meetigng will relate to.
	 */
	public void setBusinessAreas(Set<BusinessArea> businessAreas) {
		this.businessAreas = businessAreas;
	}

	/**
	 * Sets the brands that this meeting will relate to.
	 */
	public void setBrands(Set<Brand> brands) {
		this.brands = EnumUtil.convertFromEnum(brands);
	}

	/**
	 * Gets the brands that this meeting will relate to.
	 */
	public EnumSet<Brand> getBrands() {
		return EnumUtil.convertToEnum(this.brands, Brand.class);
	}
	
	/**
	 * Gets the dealerships related to the users involved in this meeting.
	 */
	public Set<Dealership> getDealerships() {
		return dealerships;
	}

	/**
	 * Sets the dealerships related to the users involved in this meeting.
	 */
	public void setDealerships(Set<Dealership> dealerships) {
		this.dealerships = dealerships;
	}

	/**
	 * Gets the users that were invited to attend this meeting.
	 */
	public Set<AgendaInvitee> getInvitees() {
		return invitees;
	}

	/**
	 * Sets the users that were invited to attend this meeting.
	 */
	public void setInvitees(Set<AgendaInvitee> invitees) {
		this.invitees = invitees;
	}
	
	/**
	 * Gets the standard topic that will be discussed during this meeting.
	 */
	public Set<StandardTopic> getStandardTopic() {
		return standardTopic;
	}

	/**
	 * Sets the standard topic that will be discussed during this meeting.
	 */
	public void setStandardTopic(Set<StandardTopic> standardTopic) {
		this.standardTopic = standardTopic;
	}

	/**
	 * Sets the username (primary key of user table) of the user that created this entity.
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets the username (primary key of user table) of the user that created this entity.
	 */
	public String getCreatedBy() {
		return this.createdBy;
	}

	/**
	 * Sets the timestamp of when this entity was created.
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the timestamp of when this entity was created.
	 */
	public Date getCreatedDate() {
		return this.createdDate;
	}

	/**
	 * Sets the timestamp of when this entity was last modified.
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Gets the timestamp of when this entity was last modified.
	 */
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	/**
	 * Sets the username (primary key of user table) of the user that last updated this entity.
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * Gets the username (primary key of user table) of the user that last updated this entity.
	 */
	public String getModifiedBy() {
		return this.modifiedBy;
	}
	
	/**
	 * Gets a collection of dealer instances that this entity instance is related to.
	 */
	public Collection<Dealership> getRelatedDealers() {
		return this.dealerships;
	}

	/**
	 * Gets a collection of franchise group instances that this entity instance is related to.
	 */
	public Collection<FranchiseGroup> getRelatedFranchiseGroups() {
		HashSet<FranchiseGroup> result = new HashSet<FranchiseGroup>();
		for(Dealership dealer : this.dealerships) {
			result.add(dealer.getFranchiseGroup());
		}
		return result;
	}

	
	/**
	 * Gets a collection of brands that this entity instance is related to.
	 */
	public EnumSet<Brand> getRelatedBrands() {
		return this.getBrands();
	}

	/**
	 * Returns a string representation of this object.
	 */
	public String toString() {
		return this.titleOfMeeting;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((id == null) ? 0 : id.hashCode()));
		return result;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Agenda)) {
			return false;
		}
		Agenda equalCheck = (Agenda) obj;
		if ((id == null && equalCheck.id != null) || (id != null && equalCheck.id == null)) {
			return false;
		} else if (id != null && !id.equals(equalCheck.id)) {
			return false;
		}
		return true;
	}
}
