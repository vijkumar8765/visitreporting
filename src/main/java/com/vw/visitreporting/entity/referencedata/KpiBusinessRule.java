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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.vw.visitreporting.entity.BaseVREntity;
import com.vw.visitreporting.entity.referencedata.enums.Brand;
import com.vw.visitreporting.entity.referencedata.enums.VarianceColor;
import com.vw.visitreporting.entity.referencedata.util.EnumUtil;


/**
 * A rule that determines whether an actual KPI value meets, exceeds, falls short
 * of the expected performance measured by the KPI.
 */
@Entity
@Table(schema = "PUBLIC", name = "KPI_BUSINESS_RULE")
public class KpiBusinessRule implements BaseVREntity,Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "BRAND_ID")
	@Basic(fetch = FetchType.EAGER)
	private Integer brand;
	
	@Column(name = "BUSINESS_RULE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	private String businessRule;
	
	@Column(name = "VARIANCE_COLOUR")
	@Basic(fetch = FetchType.EAGER)
	private Integer varianceColour;

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
	@JoinColumns({ @JoinColumn(name = "KPI_ID", referencedColumnName = "ID", nullable = false) })
	private Kpi kpi;


	/**
	 */
	public KpiBusinessRule() {
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
	public Brand getBrand() {
		return EnumUtil.getEnumObject(this.brand, Brand.class);
	}

	/**
	 */
	public void setBusinessRule(String businessRule) {
		this.businessRule = businessRule;
	}

	/**
	 */
	public String getBusinessRule() {
		return this.businessRule;
	}

	/**
	 */
	public void setVarianceColour(VarianceColor varianceColour) {
		this.varianceColour = varianceColour.getId();
	}

	/**
	 */
	public VarianceColor getVarianceColour() {
		return EnumUtil.getEnumObject(this.varianceColour, VarianceColor.class);
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
	public void setKpi(Kpi kpi) {
		this.kpi = kpi;
	}

	/**
	 */
	public Kpi getKpi() {
		return kpi;
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
		if (!(obj instanceof KpiBusinessRule)){
			return false;
		}
		if(this.getId().equals(((KpiBusinessRule)obj).getId())){
			return true;
		}
		return false;
	}
}
