package com.vw.visitreporting.entity.security;

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

import com.vw.visitreporting.entity.BaseVREntity;

/**
	 */
@Entity
@Table(schema = "PUBLIC", name = "GENERIC_ACCESS_GRANT")
public class GenericAccessGrant implements BaseVREntity,Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 */
	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Userid of person who got the access
	 */
	@Column(name = "USER_ID", length = 50)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private String userId;

	/**
	 * name of entity class on which this access has been granted
	 */
	@Column(name = "ENTITY_CLASS_NAME")
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private String entityClassName;
	
	/**
	 * id of entity for which this access has been granted
	 */
	@Column(name = "ENTITY_ID")
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private Long entityId;

	/**
	 * 
	*/
	@Column(name = "FUNCTION")
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private Integer function;

	/**
	 * 
	*/
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_ACCESS_GRANTED_FROM")
	@Basic(fetch = FetchType.EAGER)
	private Date dateAccessGrantedFrom;

	/**
	 * 
	*/
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_ACCESS_GRANTED_UPTO")
	@Basic(fetch = FetchType.EAGER)
	private Date dateAccessGrantedUpto;

	/**
	 * 
	*/
	@Column(name = "CREATED_BY", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private String createdBy;

	/**
	 * 
	*/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private Date createdDate;

	/**
	 * 
	*/
	@Column(name = "MODIFIED_BY", length = 50)
	@Basic(fetch = FetchType.EAGER)
	private String modifiedBy;

	/**
	 * 
	*/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	@Basic(fetch = FetchType.EAGER)
	private Date modifiedDate;

	
	public GenericAccessGrant() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEntityClassName() {
		return entityClassName;
	}

	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}

	public Integer getFunction() {
		return function;
	}

	public void setFunction(Integer function) {
		this.function = function;
	}

	public Date getDateAccessGrantedFrom() {
		return dateAccessGrantedFrom;
	}

	public void setDateAccessGrantedFrom(Date dateAccessGrantedFrom) {
		this.dateAccessGrantedFrom = dateAccessGrantedFrom;
	}

	public Date getDateAccessGrantedUpto() {
		return dateAccessGrantedUpto;
	}

	public void setDateAccessGrantedUpto(Date dateAccessGrantedUpto) {
		this.dateAccessGrantedUpto = dateAccessGrantedUpto;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * 
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
		if (obj == this)
			return true;
		if (!(obj instanceof GenericAccessGrant))
			return false;
		GenericAccessGrant equalCheck = (GenericAccessGrant) obj;
		if ((id == null && equalCheck.id != null)
				|| (id != null && equalCheck.id == null))
			return false;
		if (id != null && !id.equals(equalCheck.id))
			return false;
		return true;
	}
}
