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
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.StandardTopicRefType;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * A common topic of discussion for a meeting.
 */
@Entity
@Table(schema = "PUBLIC", name = "STANDARD_TOPIC")
public class StandardTopic implements BaseVREntity,Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "STANDARD_TOPIC_REF_TYPE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Integer standardTopicRefType; 
	
	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECTIVE_START_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Date effectiveStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECTIVE_END_DATE")
	@Basic(fetch = FetchType.EAGER)
	private Date effectiveEndDate;
	
	@Column(name = "CREATED_BY", length = 50, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
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
	@JoinColumns({ @JoinColumn(name = "ORGANISATION_ID", referencedColumnName = "ID") })
	private Organisation organisation;
	
	//TODO this relationship may be inappropriate 
	//if we need to point to a KPI for StandardTopic then this is wrong
	//If the KPIs are automatically added as standard topics to standard topic ref then this is correct.
	//If this is wrong, I just have to remove the relationship
	//FURTHER THOUGHTS: Need to handle this in dao. If it is a kpi then I need to get the kpi else I need to get the standard topic ref
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "STANDARD_TOPIC_REF_ID", referencedColumnName = "ID", nullable = false) })
	private StandardTopicRef standardTopicRef;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(schema = "PUBLIC", name = "STANDARD_TOPIC_BUSINESS_AREA", joinColumns = { @JoinColumn(name = "STANDARD_TOPIC_ID", referencedColumnName = "ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "BUSINESS_AREA_ID", referencedColumnName = "ID", nullable = false, updatable = false) })
	private Set<BusinessArea> businessAreas;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name="STANDARD_TOPIC_BRAND", joinColumns=@JoinColumn(name="STANDARD_TOPIC_ID"))
	@Column(name="BRAND_ID")
	private Set<Integer> brands;

	
	/**
	 */
	public StandardTopic() {
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
	public void setStandardTopicRefType(StandardTopicRefType standardTopicRefType) {
		this.standardTopicRefType = standardTopicRefType.getId();
	}

	/**
	 */
	public StandardTopicRefType getStandardTopicRefType() {
		return EnumUtil.getEnumObject(this.standardTopicRefType, StandardTopicRefType.class);
	}

	/**
	 */
	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	/**
	 */
	public Date getEffectiveStartDate() {
		return this.effectiveStartDate;
	}

	/**
	 */
	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	/**
	 */
	public Date getEffectiveEndDate() {
		return this.effectiveEndDate;
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
	public void setStandardTopicRef(StandardTopicRef standardTopicRef) {
		this.standardTopicRef = standardTopicRef;
	}

	/**
	 */
	public StandardTopicRef getStandardTopicRef() {
		return standardTopicRef;
	}

	/**
	 */
	public void setBusinessAreas(Set<BusinessArea> businessAreas) {
		this.businessAreas = businessAreas;
	}

	/**
	 */
	public Set<BusinessArea> getBusinessAreas() {
		return businessAreas;
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
		if (!(obj instanceof StandardTopic)){
			return false;
		}
		if(this.getId().equals(((StandardTopic)obj).getId())){
			return true;
		}
		return false;
	}

}
