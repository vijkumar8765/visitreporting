package com.vw.visitreporting.entity.referencedata;	//NOPMD

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.BrandRelated;
import com.vw.visitreporting.entity.ConfidentialityRelated;
import com.vw.visitreporting.entity.DealerRelated;
import com.vw.visitreporting.entity.OrganisationRelated;
import com.vw.visitreporting.entity.referencedata.enums.ActionStatus;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * An action on someone to complete a task, resulting from a meeting.
 */
@Entity
@Table(schema = "PUBLIC", name = "ACTION")
public class Action implements BaseVREntity, Serializable, BrandRelated, DealerRelated, ConfidentialityRelated, OrganisationRelated {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "DESCRIPTION", length = 100, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	@Size(min=2, max=100)
	private String description;

	@Column(name = "BRAND_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Integer brandId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "STANDARD_TOPIC_ID", referencedColumnName = "ID", nullable = true) })
	private StandardTopic standardTopic;

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "DUE_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private Date dueDate;

	@Column(name = "CONFIDENTIAL_FLAG")
	@Basic(fetch = FetchType.EAGER)
	private Byte confidential;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "ORGANISATION_ID", referencedColumnName = "ID") })
	private Organisation organisation;

	@Column(name = "STATUS", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private Integer status;

/*	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name="ACTION_OWNER", joinColumns=@JoinColumn(name="ACTION_ID", referencedColumnName="ID"))
	@Column(name="OWNER_USER_ID")
*/	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="ACTION_OWNER",
		joinColumns=@JoinColumn(name="ACTION_ID", referencedColumnName="ID"),
		inverseJoinColumns=@JoinColumn(name="OWNER_USER_ID", referencedColumnName="USER_ID")
	)
	private Set<User> owners;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="ACTION_BUSINESS_AREA",
		joinColumns=@JoinColumn(name="ACTION_ID", referencedColumnName="ID"),
		inverseJoinColumns=@JoinColumn(name="BUSINESS_AREA_ID", referencedColumnName="ID")
	)
	private Set<BusinessArea> businessAreas;
	
	@OneToMany(fetch = FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="ACTION_ID")
	private List<ActionProgress> actionProgresses;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="ACTION_DEALERSHIP",
		joinColumns=@JoinColumn(name="ACTION_ID", referencedColumnName="ID"),
		inverseJoinColumns=@JoinColumn(name="DEALERSHIP_ID", referencedColumnName="ID")
	)
	private Set<Dealership> dealerships;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="ACTION_FRANCHISE_GROUP",
		joinColumns=@JoinColumn(name="ACTION_ID", referencedColumnName="ID"),
		inverseJoinColumns=@JoinColumn(name="FRANCHISE_GROUP_ID", referencedColumnName="ID")
	)
	private Set<FranchiseGroup> franchiseGroups;

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
	public Action() {
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
	 * Gets a description of this action.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets a description of this action.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the brand that this action relates to.
	 */
	public Brand getBrand() {
		return EnumUtil.getEnumObject(this.brandId, Brand.class);
	}

	/**
	 * Gets the brand that this action relates to.
	 */
	public void setBrand(Brand brand) {
		this.brandId = brand.getId();
	}

	/**
	 * Gets the topic that this action relates to.
	 */
	public StandardTopic getStandardTopic() {
		return standardTopic;
	}

	/**
	 * Sets the topic that this action relates to.
	 */
	public void setStandardTopic(StandardTopic standardTopic) {
		this.standardTopic = standardTopic;
	}

	/**
	 * Gets the date on which this action was opened.
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the date on which this action was opened.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the desired date on which this action should be completed by.
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * Sets the desired date on which this action should be completed by.
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	/**
	 * Gets whether or not the dueDate of this action is for today.
	 */
	public boolean isDueToday() {
		Calendar cal = GregorianCalendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		cal.setTime(this.dueDate);
		return day == cal.get(Calendar.DAY_OF_MONTH)
			&& month == cal.get(Calendar.MONTH) 
			&& year == cal.get(Calendar.YEAR);
	}

	/**
	 * Gets whether or not the dueDate of this action is in the past.
	 */
	public boolean isDueInPast() {
		Calendar cal = GregorianCalendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(this.dueDate);

		return (new Date()).getTime() > this.dueDate.getTime()
			&& day != cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Gets whether or not this this action is confidential (only viewable by owners and creator).
	 */
	public Byte getConfidential() {
		return confidential;
	}
	
	/**
	 * Gets whether or not this this action is confidential (only viewable by owners and creator).
	 */
	public boolean isConfidential() {
		return (confidential != null && confidential != 0);
	}

	/**
	 * Sets whether or not this this action is confidential (only viewable by owners and creator).
	 */
	public void setConfidential(Byte confidential) {
		this.confidential = confidential;
	}

	/**
	 * Sets whether or not this this action is confidential (only viewable by owners and creator).
	 */
	public void setConfidential(boolean confidential) {
		this.confidential = (byte)(confidential ? 1 : 0);
	}

	/**
	 * Gets the organisation that this action relates to.
	 */
	public Organisation getOrganisation() {
		return organisation;
	}

	/**
	 * Sets the organisation that this action relates to.
	 */
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	
	/**
	 * Gets the collection of users responsible for completing this action.
	 */
	public Set<User> getOwners() {
		return owners;
	}

	/**
	 * Sets the collection of users responsible for completing this action.
	 */
	public void setOwners(Set<User> owners) {
		this.owners = owners;
	}

	/**
	 * Gets the collection of business areas that this action relates to.
	 */
	public Set<BusinessArea> getBusinessAreas() {
		return businessAreas;
	}

	/**
	 * Sets the collection of business areas that this action relates to.
	 */
	public void setBusinessAreas(Set<BusinessArea> businessAreas) {
		this.businessAreas = businessAreas;
	}

	/**
	 * Gets the progress update entries for this action.
	 */
	public List<ActionProgress> getActionProgresses() {
		if(this.actionProgresses != null) {
			Collections.sort(this.actionProgresses, new Comparator<ActionProgress>() {
				public int compare(ActionProgress o1, ActionProgress o2) {
					return - o1.getProgressDate().compareTo(o2.getProgressDate());
				}
			});
		}
		return actionProgresses;
	}

	/**
	 * Sets the progress update entries for this action.
	 */
	public void setActionProgresses(List<ActionProgress> actionProgresses) {
		this.actionProgresses = actionProgresses;
	}
	
	/**
	 * Gets the most recent action progress entry, or null if no progress entries have been added.
	 */
	public ActionProgress getLatestProgress() {
		List<ActionProgress> progress = getActionProgresses();
		if(progress == null || progress.isEmpty()) {
			return null;
		} else {
			return progress.get(0);
		}
	}

	/**
	 * Gets the first action progress entry, or null if no progress entries have been added.
	 */
	public ActionProgress getFirstProgress() {
		List<ActionProgress> progress = getActionProgresses();
		if(progress == null || progress.isEmpty()) {
			return null;
		} else {
			return progress.get(progress.size() - 1);
		}
	}

	/**
	 * Gets the collection of dealerships that this action relates to.
	 */
	public Set<Dealership> getDealerships() {
		return dealerships;
	}

	/**
	 * Sets the collection of dealerships that this action relates to.
	 */
	public void setDealerships(Set<Dealership> dealerships) {
		this.dealerships = dealerships;
	}

	/**
	 * Gets the collection of franchise groups that this action relates to.
	 */
	public Set<FranchiseGroup> getFranchiseGroups() {
		return franchiseGroups;
	}

	/**
	 * Sets the collection of franchise groups that this action relates to.
	 */
	public void setFranchiseGroups(Set<FranchiseGroup> franchiseGroups) {
		this.franchiseGroups = franchiseGroups;
	}


	/**
	 * Gets the status of this action, i.e. OPEN | CLOSED | REOPENED
	 */
	public ActionStatus getStatus() {
		return EnumUtil.getEnumObject(status, ActionStatus.class);
	}

	/**
	 * Gets the status of this action, i.e. OPEN | CLOSED | REOPENED
	 */
	public void setStatus(ActionStatus status) {
		this.status = status.getId();
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
		HashSet<Dealership> result = new HashSet<Dealership>();
		result.addAll(this.dealerships);
		for(FranchiseGroup group : this.franchiseGroups) {
			result.addAll(group.getDealerships());
		}
		return result;
	}

	/**
	 * Gets a collection of franchise group instances that this entity instance is related to.
	 */
	public Collection<FranchiseGroup> getRelatedFranchiseGroups() {
		HashSet<FranchiseGroup> result = new HashSet<FranchiseGroup>();
		result.addAll(this.franchiseGroups);
		for(Dealership dealer : this.dealerships) {
			result.add(dealer.getFranchiseGroup());
		}
		return result;
	}

	
	/**
	 * Gets a collection of brands that this entity instance is related to.
	 */
	public EnumSet<Brand> getRelatedBrands() {
		return EnumSet.of(this.getBrand());
	}

	/**
	 * Gets the Organisation that this entity instance is related to.
	 */
	public Organisation getRelatedOrganisation() {
		return this.organisation;
	}
	
	
	/**
	 * Returns a string representation of this object.
	 */
	public String toString() {
		return this.description;
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
		} else if (!(obj instanceof Action)) {
			return false;
		}
		Action equalCheck = (Action) obj;
		if ((id == null && equalCheck.id != null) || (id != null && equalCheck.id == null)) {
			return false;
		} else if (id != null && !id.equals(equalCheck.id)) {
			return false;
		}
		return true;
	}
}
