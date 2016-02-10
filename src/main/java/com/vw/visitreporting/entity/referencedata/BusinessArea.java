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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * A type of business area within the Volkswagen Group, e.g. Sales or Parts.
 */
@Entity
@Table(schema = "PUBLIC", name = "BUSINESS_AREA")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class BusinessArea implements BaseVREntity,Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "NAME", length = 100, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private String name;
	
	@Column(name = "CREATED_BY", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@NotNull
	private Date createdDate;

	@Column(name = "MODIFIED_BY", length = 50)
	@Basic(fetch = FetchType.EAGER)
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	@Basic(fetch = FetchType.EAGER)
	private Date modifiedDate;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name="BUSINESS_AREA_BRAND", joinColumns=@JoinColumn(name="BUSINESS_AREA_ID"))
	@Column(name="BRAND_ID")
	private Set<Integer> brands;
	
	@ManyToMany(mappedBy = "businessAreas", fetch = FetchType.LAZY)
	private Set<StandardTopic> standardTopics;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(schema = "PUBLIC", name = "BUSINESS_AREA_ORGANISATION", 
		joinColumns = { @JoinColumn(name = "BUSINESS_AREA_ID", referencedColumnName = "ID", nullable = false, updatable = false) }, 
		inverseJoinColumns = { @JoinColumn(name = "ORGANISATION_ID", referencedColumnName = "ID", nullable = false, updatable = false) })
	private Set<Organisation> organisations;
	
	@ManyToMany(mappedBy = "businessAreas", fetch = FetchType.LAZY)
	private Set<Kpi> kpis;

	/**
	 */
	public BusinessArea() {
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
	public void setBrands(Set<Brand> brands) {
		this.brands = EnumUtil.convertFromEnum(brands);
	}

	/**
	 */
	public Set<Brand> getBrands() {
		return EnumUtil.convertToEnum(this.brands, Brand.class);
	}

	/**
	 */
	public void setStandardTopics(Set<StandardTopic> standardTopics) {
		this.standardTopics = standardTopics;
	}

	/**
	 */
	public Set<StandardTopic> getStandardTopics() {
		return standardTopics;
	}

	/**
	 */
	public void setOrganisations(Set<Organisation> organisations) {
		this.organisations = organisations;
	}

	/**
	 */
	public Set<Organisation> getOrganisations() {
		return organisations;
	}

	/**
	 */
	public void setKpis(Set<Kpi> kpis) {
		this.kpis = kpis;
	}

	/**
	 */
	public Set<Kpi> getKpis() {
		return kpis;
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
		if (!(obj instanceof BusinessArea)){
			return false;
		}
		if(this.getId().equals(((BusinessArea)obj).getId())){
			return true;
		}
		return false;
	}
}
