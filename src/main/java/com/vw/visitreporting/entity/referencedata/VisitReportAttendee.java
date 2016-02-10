package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
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
import javax.validation.constraints.Size;


/**
 * A user of the system that attended a meeting in the past.
 */
@Entity
@Table(schema = "PUBLIC", name = "VISIT_REPORT_ATTENDEE")
public class VisitReportAttendee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "VISIT_REPORT_ID", referencedColumnName = "ID", nullable = false) })
	private VisitReport visitReport;

	@Column(name = "ATTENDEE_USER_ID", length = 50, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	@Size(min=2, max=50)
	private String userId;

	@Column(name = "ATTENDEE_NAME", length = 200, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	@Size(min=2, max=200)
	private String name;


	/**
	 * Constructor. Creates an empty instance of this entity.
	 */
	public VisitReportAttendee() {
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
	 * Gets the minutes of the meeting that this user attended.	
	 */
	public VisitReport getVisitReport() {
		return visitReport;
	}

	/**
	 * Sets the minutes of the meeting that this user attended.	
	 */
	public void setVisitReport(VisitReport visitReport) {
		this.visitReport = visitReport;
	}

	/**
	 * Gets the username (primary key of user table) of the user that attended this meeting.	
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the username (primary key of user table) of the user that attended this meeting.	
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the display-name of the user that attended this meeting.	
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the display-name of the user that attended this meeting.	
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Returns a string representation of this object.
	 */
	public String toString() {
		return this.name;
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
		} else if (!(obj instanceof AgendaInvitee)) {
			return false;
		}
		VisitReportAttendee equalCheck = (VisitReportAttendee) obj;
		if ((id == null && equalCheck.id != null) || (id != null && equalCheck.id == null)) {
			return false;
		} else if (id != null && !id.equals(equalCheck.id)) {
			return false;
		}
		return true;
	}	
}
