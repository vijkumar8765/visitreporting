package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.ConfidentialityRelated;
import com.vw.visitreporting.entity.referencedata.enums.DocumentStatus;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * the minutes for a meeting that has taken place.
 */
@Entity
@Table(schema = "PUBLIC", name = "VISIT_REPORT")
public class VisitReport implements BaseVREntity, Serializable, ConfidentialityRelated { //BrandRelated, DealerRelated, 
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(fetch = FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="VISIT_REPORT_ID")
	private Set<VisitReportAttendee> attendees;

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
	@JoinTable(name="VISIT_REPORT_ACTION",
		joinColumns=@JoinColumn(name="VISIT_REPORT_ID", referencedColumnName="ID"),
		inverseJoinColumns=@JoinColumn(name="ACTION_ID", referencedColumnName="ID")
	)
	private Set<Action> actions;

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
	public VisitReport() {
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
	 * Note that the requirements document refers to this status as the status
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
	 * Gets the users that attended this meeting.
	 */
	public Set<VisitReportAttendee> getAttendees() {
		return attendees;
	}

	/**
	 * Sets the users that attended this meeting.
	 */
	public void setAttendees(Set<VisitReportAttendee> attendees) {
		this.attendees = attendees;
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
	 *
	public Collection<Dealership> getRelatedDealers() {
		return this.dealerships;
	}

	/**
	 * Gets a collection of franchise group instances that this entity instance is related to.
	 *
	public Collection<FranchiseGroup> getRelatedFranchiseGroups() {
		HashSet<FranchiseGroup> result = new HashSet<FranchiseGroup>();
		for(Dealership dealer : this.dealerships) {
			result.add(dealer.getFranchiseGroup());
		}
		return result;
	}*/

	
	/**
	 * Gets a collection of brands that this entity instance is related to.
	 *
	public EnumSet<Brand> getRelatedBrands() {
		return this.getBrands();
	}

	/**
	 * Returns a string representation of this object.
	 *
	public String toString() {
		return this.titleOfMeeting;
	}*/

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
		VisitReport equalCheck = (VisitReport) obj;
		if ((id == null && equalCheck.id != null) || (id != null && equalCheck.id == null)) {
			return false;
		} else if (id != null && !id.equals(equalCheck.id)) {
			return false;
		}
		return true;
	}
}
