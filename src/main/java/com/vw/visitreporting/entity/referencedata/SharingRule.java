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
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * Defines a rule that allows another organisation and brand to view any entity
 * created by a specified organisation and brand.
 */
@Entity
@Table(schema = "PUBLIC", name = "SHARING_RULE")
public class SharingRule implements BaseVREntity,Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "ROOT_BRAND_ID")
	@Basic(fetch = FetchType.EAGER)
	private Integer rootBrand;
	
	@Column(name = "TARGET_BRAND_ID")
	@Basic(fetch = FetchType.EAGER)
	private Integer targetBrand;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "ROOT_ORGANISATION_ID", referencedColumnName = "ID", nullable = false) })
	private Organisation rootOrganisation;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "TARGET_ORGANISATION_ID", referencedColumnName = "ID", nullable = false) })
	private Organisation targetOrganisation;

	/*
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
	*/
	
	
	/**
	 * Constructor. Sets all properties to default (null) values.
	 */
	public SharingRule() {
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

	/**
	 * Sets the brand associated to the entity that will provide the information to share.
	 */
	public void setRootBrand(Brand rootBrand) {
		this.rootBrand = (rootBrand == null) ? null : rootBrand.getId();
	}

	/**
	 * Gets the brand associated to the entity that will provide the information to share.
	 */
	public Brand getRootBrand() {
		return EnumUtil.getEnumObject(this.rootBrand, Brand.class);
	}

	/**
	 * Sets the brand associated to the entity that will receiving the shared information.
	 */
	public void setTargetBrand(Brand targetBrand) {
		this.targetBrand = (targetBrand == null) ? null : targetBrand.getId();
	}

	/**
	 * Gets the brand associated to the entity that will receiving the shared information.
	 */
	public Brand getTargetBrand() {
		return EnumUtil.getEnumObject(this.targetBrand, Brand.class);
	}

	/**
	 * Sets the organisation associated to the entity that will provide the information to share.
	 */
	public void setRootOrganisation(Organisation rootOrganisation) {
		this.rootOrganisation = rootOrganisation;
	}

	/**
	 * Gets the organisation associated to the entity that will provide the information to share.
	 */
	public Organisation getRootOrganisation() {
		return rootOrganisation;
	}

	/**
	 * Sets the organisation associated to the entity that will receiving the shared information.
	 */
	public void setTargetOrganisation(Organisation targetOrganisation) {
		this.targetOrganisation = targetOrganisation;
	}

	/**
	 * Gets the organisation associated to the entity that will receiving the shared information.
	 */
	public Organisation getTargetOrganisation() {
		return targetOrganisation;
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
		if (!(obj instanceof SharingRule)){
			return false;
		}
		if(this.getId() == null) {
			if(((SharingRule)obj).getId() != null) {
				return false;
			}
		} else if(this.getId().equals(((SharingRule)obj).getId())){
			return true;
		}
		return false;
	}

}
