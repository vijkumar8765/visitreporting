package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * An update to an action to provide information about progress that has
 * been made toward completing the action so far.
 */
@Entity
@Table(schema = "PUBLIC", name = "ACTION_PROGRESS")
public class ActionProgress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "ACTION_ID", referencedColumnName = "ID", nullable = false) })
	private Action action;

	@Column(name = "DESCRIPTION", length = 255, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	@Size(min=2, max=255)
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(name = "PROGRESS_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private Date progressDate;

	@Column(name = "CREATED_BY", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Date createdDate;


	/**
	 * Constructor. Creates an empty instance of this entity.
	 */
	public ActionProgress() {
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
	 * Gets the action that this progress entry relates to.
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Sets the action that this progress entry relates to.
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * Gets the description of this progress update.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of this progress update.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the date that this progress update was entered.
	 */
	public Date getProgressDate() {
		return progressDate;
	}

	/**
	 * Sets the date that this progress update was entered.
	 */
	public void setProgressDate(Date progressDate) {
		this.progressDate = progressDate;
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
		} else if (!(obj instanceof ActionProgress)) {
			return false;
		}
		ActionProgress equalCheck = (ActionProgress) obj;
		if ((id == null && equalCheck.id != null) || (id != null && equalCheck.id == null)) {
			return false;
		} else if (id != null && !id.equals(equalCheck.id)) {
			return false;
		}
		return true;
	}
}
