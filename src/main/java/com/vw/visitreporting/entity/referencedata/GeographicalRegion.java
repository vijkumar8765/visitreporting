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


/**
 * The UK is divided into many regions to allow responsibility for franchisee
 * management to be delegated down the management chain.
 */
@Entity
@Table(schema = "PUBLIC", name = "GEOGRAPHICAL_REGION")
public class GeographicalRegion implements BaseVREntity,Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "DESCRIPTION", length = 100, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String description;

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
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "GEOGRAPHICAL_STRUCTURE_CATEGORY_ID", referencedColumnName = "ID", nullable = false) })
	private GeographicalStructureCategory geographicalStructureCategory;
	
	@OneToMany(mappedBy = "geographicalRegion", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	private Set<GeographicalArea> geographicalAreas;

	
	/**
	 */
	public GeographicalRegion() {
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
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 */
	public String getDescription() {
		return this.description;
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
	public void setGeographicalStructureCategory(GeographicalStructureCategory geographicalStructureCategory) {
		this.geographicalStructureCategory = geographicalStructureCategory;
	}

	/**
	 */
	public GeographicalStructureCategory getGeographicalStructureCategory() {
		return geographicalStructureCategory;
	}

	/**
	 */
	public void setGeographicalAreas(Set<GeographicalArea> geographicalAreas) {
		this.geographicalAreas = geographicalAreas;
	}

	/**
	 */
	public Set<GeographicalArea> getGeographicalAreas() {
		return geographicalAreas;
	}


	/**
	 * Returns a string representation of this object.
	 */
	public String toString() {
		return description;
	}

	
	/**
	 */
	@Override
	public int hashCode() {
		return description.hashCode() | geographicalStructureCategory.hashCode();
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
		if (!(obj instanceof GeographicalRegion)){
			return false;
		}
		if( ! this.getDescription().equals(((GeographicalRegion)obj).getDescription())){
			return false;
		}
		if( ! this.getGeographicalStructureCategory().equals(((GeographicalRegion)obj).getGeographicalStructureCategory())){
			return false;
		}
		return true;
	}
}
