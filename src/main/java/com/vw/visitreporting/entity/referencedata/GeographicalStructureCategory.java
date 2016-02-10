package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * Different organinsations and business areas within Volkswagen Group UK
 * have partitioned the UK into different geographic structures. This category
 * labels different geographic structures.
 */
@Entity
@Table(schema = "PUBLIC", name = "GEOGRAPHICAL_STRUCTURE_CATEGORY")
public class GeographicalStructureCategory implements BaseVREntity,Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "BRAND_ID")
	@Basic(fetch = FetchType.EAGER)
	private Integer brand;

	@Column(name = "NAME", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String name;

	@Column(name = "IS_DEFAULT")
	@Basic(fetch = FetchType.EAGER)
	private Byte isDefault;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "ORGANISATION_ID", referencedColumnName = "ID", nullable = false) })
	private Organisation organisation;
	
	@OneToMany(mappedBy = "geographicalStructureCategory", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	private Set<GeographicalRegion> geographicalRegions;

	
	/**
	 */
	public GeographicalStructureCategory() {
	}
	
	/**
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 */
	public void setBrand(Brand brand) {
		this.brand = brand.getId();
	}

	/**
	 */
	public Brand getBrandId() {
		return EnumUtil.getEnumObject(this.brand, Brand.class);
	}

	/**
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 */
	public String getName() {
		return this.name;
	}

	/**
	 */
	public void setIsDefault(Byte isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 */
	public void setIsDefault(boolean isDefault) {
		this.isDefault = (byte)(isDefault ? 1 : 0);
	}

	/**
	 */
	public Byte getIsDefault() {
		return this.isDefault;
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
	 */
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	/**
	 */
	public Organisation getOrganisation() {
		return organisation;
	}

	/**
	 */
	public void setGeographicalRegions(Set<GeographicalRegion> geographicalRegions) {
		this.geographicalRegions = geographicalRegions;
	}

	/**
	 */
	public Set<GeographicalRegion> getGeographicalRegions() {
		return geographicalRegions;
	}

	
	/**
	 * Returns a string representation of this object.
	 */
	public String toString() {
		return name;
	}

	
	/**
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
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
		if (!(obj instanceof GeographicalStructureCategory)){
			return false;
		}
		return this.getName().equals(((GeographicalStructureCategory)obj).getName());
	}
}
