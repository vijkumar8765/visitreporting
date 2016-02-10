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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.vw.visitreporting.auditing.Audited;
import com.vw.visitreporting.entity.BaseVREntity;


/**
 * A type of meeting, for example phone call or questionnaire/survey
 */
@Entity
@Table(schema = "PUBLIC", name = "MEETING_TYPE")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Audited("Meeting Type")
public class MeetingType implements BaseVREntity, Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "NAME", length = 200, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
    @Size(min = 2, max = 200)
	private String name;

	@Column(name = "IS_DISABLED", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private Byte disabled;

	@Column(name = "CREATED_BY", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
    @Size(min = 0, max = 50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Date createdDate;

	@Column(name = "MODIFIED_BY", length = 50)
	@Basic(fetch = FetchType.EAGER)
	@Size(min = 0, max = 50)
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	@Basic(fetch = FetchType.EAGER)
	private Date modifiedDate;

	
	/**
	 * Constructor. Creates an empty instance of this class that is not disabled.
	 */
	public MeetingType() {
		disabled = (byte)0;
	}
	
	/**
	 * Sets the primary key of this entity in the database.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the primary key of this entity in the database.
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * Sets the name/desciption of this meeting type.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name/desciption of this meeting type.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets whether or not this entity is disabled, i.e. hidden from view
	 * but still present in database to main referential integrity.
	 */
	@Audited("disabled")
	public void setDisabled(Byte disabled) {
		this.disabled = disabled;
	}

	/**
	 * Gets whether or not this entity is disabled, i.e. hidden from view
	 * but still present in database to main referential integrity.
	 */
	public Byte getDisabled() {
		return this.disabled;
	}

	/**
	 * Sets whether or not this entity is disabled, i.e. hidden from view
	 * but still present in database to main referential integrity.
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = (byte)(disabled ? 1 : 0);
	}

	/**
	 * Gets whether or not this entity is disabled, i.e. hidden from view
	 * but still present in database to main referential integrity.
	 */
	public boolean isDisabled() {
		return (disabled != null && disabled > 0);
	}
	
	/**
	 * Set the username of the user that created this entity.
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Get the username of the user that created this entity.
	 */
	public String getCreatedBy() {
		return this.createdBy;
	}

	/**
	 * Set the timestamp when this entity was created.
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Get the timestamp when this entity was created.
	 */
	public Date getCreatedDate() {
		return this.createdDate;
	}

	/**
	 * Set the username of the user that last updated this entity.
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * Get the username of the user that last updated this entity.
	 */
	public String getModifiedBy() {
		return this.modifiedBy;
	}

	/**
	 * Set the timestamp when this entity was last updated.
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Get the timestamp when this entity was last updated.
	 */
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	
	/**
	 * Return a string representation of this entity.
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
		if(obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof MeetingType)){
			return false;
		}
		if(this.getId().equals(((MeetingType)obj).getId())) {
			return true;
		}
		return false;
	}
}
