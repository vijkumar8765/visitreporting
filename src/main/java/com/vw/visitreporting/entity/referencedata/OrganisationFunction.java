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
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.Function;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * Defines a rule that allows another organisation and brand to view any entity
 * created by a specified organisation and brand.
 */
@Entity
@Table(schema = "PUBLIC", name = "ORGANISATION_FUNCTION")
public class OrganisationFunction implements BaseVREntity, Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {})
	@JoinColumns({ @JoinColumn(name = "ORGANISATION_ID", referencedColumnName = "ID", nullable = false) })
	private Organisation organisation;
	
	@Column(name = "BRAND_ID")
	@Basic(fetch = FetchType.EAGER)
	private Integer brand;

	@Column(name = "FUNCTION")
	@Basic(fetch = FetchType.EAGER)
	private Integer function;

	
	/**
	 * Constructor. Sets all properties to default (null) values.
	 */
	public OrganisationFunction() {
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
	 * Sets the organisation that will have access to the system function.
	 */
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	/**
	 * Gets the organisation that will have access to the system function.
	 */
	public Organisation getOrganisation() {
		return organisation;
	}

	/**
	 * Sets the brand that will have access to the system function.
	 */
	public void setBrand(Brand brand) {
		this.brand = (brand == null) ? null : brand.getId();
	}

	/**
	 * Gets the brand that will have access to the system function.
	 */
	public Brand getBrand() {
		return EnumUtil.getEnumObject(this.brand, Brand.class);
	}

	/**
	 * Sets the system function that the organisation and brand will have access to.
	 */
	public void setFunction(Function function) {
		this.function = function.getId();
	}

	/**
	 * Gets the system function that the organisation and brand will have access to.
	 */
	public Function getFunction() {
		return EnumUtil.getEnumObject(this.function, Function.class);
	}

	public void setCreatedBy(String createdBy) {
		//this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		//return this.createdBy;
		return null;
	}

	public void setCreatedDate(Date createdDate) {
		//this.createdDate = createdDate;
	}

	public Date getCreatedDate() {
		//return this.createdDate;
		return null;
	}

	public void setModifiedBy(String modifiedBy) {
		//this.modifiedBy = modifiedBy;
	}
	
	public String getModifiedBy() {
		//return this.modifiedBy;
		return null;
	}

	public void setModifiedDate(Date modifiedDate) {
		//this.modifiedDate = modifiedDate;
	}

	public Date getModifiedDate() {
		//return this.modifiedDate;
		return null;
	}

	public int hashCode() {
		return (id == null) ? 0 : id;
	}

	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Organisation)){
			return false;
		}
		if(id == null) {
			if(((Organisation)obj).getId() != null) {
				return false;
			}
		} else if(this.getId().equals(((Organisation)obj).getId())) {
			return true;
		}
		return false;
	}
}