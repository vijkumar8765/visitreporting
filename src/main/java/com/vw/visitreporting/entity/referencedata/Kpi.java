package com.vw.visitreporting.entity.referencedata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.KpiStatus;
import com.vw.visitreporting.entity.referencedata.enums.VarianceColor;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * A Key Performance Indicator that quantifies the performance of a dealership
 * in a specific area of business of importance to the Volkswagen Group.
 */
@Entity
@Table(schema = "PUBLIC", name = "KPI")
public class Kpi implements BaseVREntity,Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "ORGANISATION_ID", referencedColumnName = "ID", nullable = false) })
	private Organisation organisation;

	@Column(name = "KPI_DESCRIPTION", length = 100, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String kpiDescription;

	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECTIVE_START_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Date effectiveStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECTIVE_END_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private Date effectiveEndDate;
	
	@Column(name = "STATUS")
	@Basic(fetch = FetchType.EAGER)
	private Integer status;
	
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

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFIED_DATE")
	@Basic(fetch = FetchType.EAGER)
	private Date modifiedDate;

	@Column(name = "NEW_STATUS")
	@Basic(fetch = FetchType.EAGER)
	private Integer newStatus;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "NEW_STATUS_EFFECTIVE_DATE")
	@Basic(fetch = FetchType.EAGER)
	private Date newStatusEffectiveDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "KPI_CATEGORY_ID", referencedColumnName = "ID") })
	private KpiCategory kpiCategory;
	
	@OneToMany(mappedBy = "kpi", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	private Set<KpiBusinessRule> kpiBusinessRules;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(schema = "PUBLIC", name = "KPI_BUSINESS_AREA", joinColumns = { @JoinColumn(name = "KPI_ID", referencedColumnName = "ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "BUSINESS_AREA_ID", referencedColumnName = "ID", nullable = false, updatable = false) })
	private Set<BusinessArea> businessAreas;
	
	@OneToMany(mappedBy = "kpi", cascade = { CascadeType.ALL }, orphanRemoval=true, fetch = FetchType.EAGER) 
	private Set<KpiBrand> kpiBrands;

	@Column(name = "IS_PERCENTAGE")
	@Basic(fetch = FetchType.EAGER)
	private Byte percentage;

	@Column(name = "DISPLAY_SEQUENCE")
	@Basic(fetch = FetchType.EAGER)
	private Short displaySequence;
	
	@Transient	//this is a holder for one of the related dealer targets. DealerKpiTarget entities should be persisted directly.
	private DealerKpiTarget dealershipTarget;

	@Transient	//this is a holder for one of the related dealer scores. DealershipActual entities should be persisted directly.
	private DealerKpiActual dealershipActual;

	@Transient	//temporarily store the colour that the actual score for a specific dealership should be rendered as
	private VarianceColor dealershipActualColour;

	/**
	 * All the variables marked @Transiant will be used in jsp page not for persisting to database
	 */
	@Transient
	private Brand[] brands;
	
	@SuppressWarnings("unchecked")
	@Transient
	private List<DynamicKpiBrand> dynamicKpiBrands = LazyList.decorate(new ArrayList<DynamicKpiBrand>(), new Factory() {		
		@Override
		public Object create() {
			return new DynamicKpiBrand();
		}
	});
	
	@SuppressWarnings("unchecked")
	@Transient
	private List<DynamicKpiBusinessRule> dynamicKpiBusinessRules = LazyList.decorate(new ArrayList<DynamicKpiBusinessRule>(), new Factory() {		
		@Override
		public Object create() {
			return new DynamicKpiBusinessRule();
		}
	});	
	
	public List<DynamicKpiBusinessRule> getDynamicKpiBusinessRules() {
		return dynamicKpiBusinessRules;
	}

	public void setDynamicKpiBusinessRules(
			List<DynamicKpiBusinessRule> dynamicKpiBusinessRules) {
		this.dynamicKpiBusinessRules = dynamicKpiBusinessRules;
	}

	public List<DynamicKpiBrand> getDynamicKpiBrands() {
		return dynamicKpiBrands;
	}

	public void setDynamicKpiBrands(List<DynamicKpiBrand> dynamicKpiBrands) {
		this.dynamicKpiBrands = dynamicKpiBrands;
	}

	public Brand[] getBrands() {
		return brands;
	}

	public void setBrands(Brand[] brands) {
		this.brands = brands;
	}


	/**
	 */
	public Kpi() {
	}
	
	/**
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 */
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	/**
	 */
	public Organisation getOrganisation() {
		return this.organisation;
	}

	/**
	 */
	public void setKpiDescription(String kpiDescription) {
		this.kpiDescription = kpiDescription;
	}

	/**
	 */
	public String getKpiDescription() {
		return this.kpiDescription;
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
	public void setStatus(KpiStatus status) {
		this.status = status.getId();
	}

	/**
	 */
	public KpiStatus getStatus() {
		return EnumUtil.getEnumObject(this.status, KpiStatus.class);
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
	public void setNewStatus(KpiStatus newStatus) {
		if(newStatus != null){
			this.newStatus = newStatus.getId();
		}
	}

	/**
	 */
	public KpiStatus getNewStatus() {
		return EnumUtil.getEnumObject(this.newStatus, KpiStatus.class);
	}

	/**
	 */
	public void setNewStatusEffectiveDate(Date newStatusEffectiveDate) {
		this.newStatusEffectiveDate = newStatusEffectiveDate;
	}

	/**
	 */
	public Date getNewStatusEffectiveDate() {
		return this.newStatusEffectiveDate;
	}

	/**
	 */
	public void setKpiCategory(KpiCategory kpiCategory) {
		this.kpiCategory = kpiCategory;
	}

	/**
	 */
	public KpiCategory getKpiCategory() {
		return kpiCategory;
	}

	/**
	 */
	public void setKpiBusinessRules(Set<KpiBusinessRule> kpiBusinessRules) {
		this.kpiBusinessRules = kpiBusinessRules;
	}

	/**
	 */
	public Set<KpiBusinessRule> getKpiBusinessRules() {
		return kpiBusinessRules;
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
	public void setKpiBrands(Set<KpiBrand> kpiBrands) {
		this.kpiBrands = kpiBrands;
	}

	/**
	 */
	public Set<KpiBrand> getKpiBrands() {
		return kpiBrands;
	}

	/**
	 * Gets whether or not this this KPI is a percentage value.
	 */
	public Byte getPercentage() {
		return percentage;
	}
	
	/**
	 * Gets whether or not this this KPI is a percentage value.
	 */
	public boolean isPercentage() {
		return (percentage != null && percentage != 0);
	}

	/**
	 * Sets whether or not this this KPI is a percentage value.
	 */
	public void setPercentage(Byte percentage) {
		this.percentage = percentage;
	}

	/**
	 * Sets whether or not this this KPI is a percentage value.
	 */
	public void setPercentage(boolean percentage) {
		this.percentage = (byte)(percentage ? 1 : 0);
	}
	
	/**
	 * Gets the position that this KPI should be displayed in a list.
	 * Lists of KPIs will be sorted by this property, usually after being sorted by organisation.
	 * This value does not have to be unique among all KPIs. 
	 */
	public Short getDisplaySequence() {
		return displaySequence;
	}

	/**
	 * Sets the position that this KPI should be displayed in a list.
	 * Lists of KPIs will be sorted by this property, usually after being sorted by organisation.
	 * This value does not have to be unique among all KPIs. 
	 */
	public void setDisplaySequence(Short displaySequence) {
		this.displaySequence = displaySequence;
	}
	
	/**
	 * Gets the target scores for dealers against this KPI.
	 */
	public DealerKpiTarget getDealershipTarget() {
		return dealershipTarget;
	}

	/**
	 * Sets the target scores for dealers against this KPI.
	 */
	public void setDealershipTarget(DealerKpiTarget dealershipTarget) {
		this.dealershipTarget = dealershipTarget;
	}

	/**
	 * Gets the actual scores for dealers against this KPI.
	 */
	public DealerKpiActual getDealershipActual() {
		return dealershipActual;
	}

	/**
	 * Sets the actual scores for dealers against this KPI.
	 */
	public void setDealershipActual(DealerKpiActual dealershipActual) {
		this.dealershipActual = dealershipActual;
	}
	
	/**
	 * Gets the colour that the actual score for a specific dealership should be rendered as.
	 */
	public VarianceColor getDealershipActualColour() {
		return dealershipActualColour;
	}

	/**
	 * Sets the colour that the actual score for a specific dealership should be rendered as.
	 */
	public void setDealershipActualColour(VarianceColor dealershipActualColour) {
		this.dealershipActualColour = dealershipActualColour;
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
		if (!(obj instanceof Kpi)){
			return false;
		}
		if(this.getId().equals(((Kpi)obj).getId())){
			return true;
		}
		return false;
	}
}
