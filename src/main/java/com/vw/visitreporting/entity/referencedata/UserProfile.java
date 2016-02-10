package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vw.visitreporting.auditing.Audited;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.accesscontrolled.AccessControlled;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * A role that can be used to grant access to functions within the system.
 */
@Entity
@Table(schema = "PUBLIC", name = "USER_PROFILE")
@Audited("User Profile")
public class UserProfile implements BaseVREntity,Serializable,AccessControlled {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "NAME", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String name;
	
	@Column(name = "CREATED_BY", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	@Basic(fetch = FetchType.EAGER)
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY", length = 50)
	@Basic(fetch = FetchType.EAGER)
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	@Basic(fetch = FetchType.EAGER)
	private Date modifiedDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="USER_PROFILE_FUNCTION", joinColumns=@JoinColumn(name="PROFILE_ID"))
	@Column(name="FUNCTION")
	private Set<Integer> allowedFunctions;
	

	/**
	 * Constructor. Sets all properties to default (null) values.
	 */
	public UserProfile() {
	}
	

	/**
	 * Sets the database primary key for this entity.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the database primary key for this entity.
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * Sets the name/description of this profile.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name/description of this profile.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 */
	public String getCreatedBy() {
		return this.createdBy;
	}

	/**
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 */
	public Date getCreatedDate() {
		return this.createdDate;
	}

	/**
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 */
	public String getModifiedBy() {
		return this.modifiedBy;
	}

	/**
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 */
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	/**
	 * Sets the granted system functions for this profile.	
	 */
	@Audited("permissions")
	public void setAllowedFunctions(Set<Function> allowedFunctions) {
		this.allowedFunctions = EnumUtil.convertFromEnum(allowedFunctions);
	}

	/**
	 * Gets the granted system functions for this profile.	
	 */
	public Set<Function> getAllowedFunctions() {
		return EnumUtil.convertToEnum(allowedFunctions, Function.class);
	}
	
	/**
	 * Returns a string representation of this object.
	 */
	public String toString() {
		return this.name;
	}
	
	/**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((id == null) ? 0 : id.hashCode()));
		return result;
	}

	/**
	 */
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if (obj == this){
			return true;
		}
		if (!(obj instanceof UserProfile)){
			return false;
		}
		if(this.getId().equals(((UserProfile)obj).getId())){
			return true;
		}
		return false;
	}
}
